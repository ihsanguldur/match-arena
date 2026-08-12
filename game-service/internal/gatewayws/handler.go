package gatewayws

import (
	"context"
	"log"
	"net/http"
	"sync"
	"time"

	matcharenav1 "github.com/ihsanguldur/match-arena/game-service/internal/gen/match_arena/v1"
	"github.com/gorilla/websocket"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"google.golang.org/grpc/metadata"
	"google.golang.org/protobuf/types/known/timestamppb"
)

var upgrader = websocket.Upgrader{
	CheckOrigin: func(r *http.Request) bool { return true },
}

type Handler struct {
	matchmakingClient matcharenav1.MatchmakingServiceClient
	gameSessionClient matcharenav1.GameSessionServiceClient
}

func NewHandler(gameServiceAddr string) (*Handler, error) {
	conn, err := grpc.NewClient(gameServiceAddr, grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		return nil, err
	}

	return &Handler{
		matchmakingClient: matcharenav1.NewMatchmakingServiceClient(conn),
		gameSessionClient: matcharenav1.NewGameSessionServiceClient(conn),
	}, nil
}

func (h *Handler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	ws, err := upgrader.Upgrade(w, r, nil)
	if err != nil {
		log.Printf("gateway: ws upgrade failed: %v", err)
		return
	}
	defer ws.Close()

	c := &connection{ws: ws, h: h}
	c.run()
}

// connection holds all state for a single browser tab's WebSocket session.
// The read loop in run() is the only reader of c.ws; writes to c.ws only ever
// happen from a single goroutine at a time (see handleJoinQueue) because
// gorilla/websocket connections allow one concurrent reader and one
// concurrent writer, but not multiple concurrent writers.
type connection struct {
	ws *websocket.Conn
	h  *Handler

	token    string
	playerID string

	mu          sync.Mutex
	queuing     bool
	ticketID    string
	sessionID   string
	playStream  matcharenav1.GameSessionService_PlayGameClient
	cancelQueue context.CancelFunc
}

type clientMessage struct {
	Type     string `json:"type"`
	Token    string `json:"token,omitempty"`
	PlayerID string `json:"player_id,omitempty"`
	Rating   int32  `json:"rating,omitempty"`
}

type scoreDTO struct {
	PlayerID string `json:"player_id"`
	Score    int32  `json:"score"`
}

type flagDTO struct {
	PlayerID string `json:"player_id"`
	Rule     string `json:"rule"`
	Detail   string `json:"detail"`
}

type serverMessage struct {
	Type         string     `json:"type"`
	TicketID     string     `json:"ticket_id,omitempty"`
	SessionID    string     `json:"session_id,omitempty"`
	PlayerIDs    []string   `json:"player_ids,omitempty"`
	Scores       []scoreDTO `json:"scores,omitempty"`
	Flags        []flagDTO  `json:"flags,omitempty"`
	SessionEnded bool       `json:"session_ended,omitempty"`
	Message      string     `json:"message,omitempty"`
}

func (c *connection) run() {
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()

	if !c.authenticate() {
		return
	}

	for {
		var msg clientMessage
		if err := c.ws.ReadJSON(&msg); err != nil {
			return
		}

		switch msg.Type {
		case "join_queue":
			c.mu.Lock()
			alreadyQueuing := c.queuing
			if !alreadyQueuing {
				c.queuing = true
			}
			c.mu.Unlock()
			if !alreadyQueuing {
				go c.handleJoinQueue(ctx, msg.Rating)
			}
		case "leave_queue":
			c.handleLeaveQueue(ctx)
		case "action":
			c.handleAction()
		}
	}
}

// authenticate blocks for the connection's first message, which must be an
// auth message carrying the JWT the browser already has from account-service
// login. The browser's native WebSocket API can't set an Authorization header
// on the handshake, so the token travels as the first application message
// instead.
func (c *connection) authenticate() bool {
	_ = c.ws.SetReadDeadline(time.Now().Add(5 * time.Second))
	var msg clientMessage
	err := c.ws.ReadJSON(&msg)
	_ = c.ws.SetReadDeadline(time.Time{})

	if err != nil || msg.Type != "auth" || msg.Token == "" || msg.PlayerID == "" {
		c.sendError("authentication required")
		return false
	}

	c.token = msg.Token
	c.playerID = msg.PlayerID
	return true
}

func (c *connection) authContext(ctx context.Context) context.Context {
	return metadata.AppendToOutgoingContext(ctx, "authorization", "Bearer "+c.token)
}

func (c *connection) sendError(message string) {
	_ = c.ws.WriteJSON(serverMessage{Type: "error", Message: message})
}

func (c *connection) handleJoinQueue(ctx context.Context, rating int32) {
	defer func() {
		c.mu.Lock()
		c.queuing = false
		c.mu.Unlock()
	}()

	queueCtx, cancel := context.WithCancel(ctx)
	c.mu.Lock()
	c.cancelQueue = cancel
	c.mu.Unlock()
	defer cancel()

	stream, err := c.h.matchmakingClient.JoinQueue(c.authContext(queueCtx), &matcharenav1.JoinQueueRequest{Rating: rating})
	if err != nil {
		c.sendError("failed to join queue")
		return
	}

	for {
		update, err := stream.Recv()
		if err != nil {
			return
		}

		switch u := update.GetUpdate().(type) {
		case *matcharenav1.JoinQueueUpdate_QueueTicketId:
			c.mu.Lock()
			c.ticketID = u.QueueTicketId
			c.mu.Unlock()
			_ = c.ws.WriteJSON(serverMessage{Type: "queued", TicketID: u.QueueTicketId})
		case *matcharenav1.JoinQueueUpdate_MatchFound:
			c.mu.Lock()
			c.sessionID = u.MatchFound.GetSessionId()
			c.mu.Unlock()
			_ = c.ws.WriteJSON(serverMessage{
				Type:      "match_found",
				SessionID: u.MatchFound.GetSessionId(),
				PlayerIDs: u.MatchFound.GetPlayerIds(),
			})
			c.startPlayGame(ctx)
			return
		}
	}
}

func (c *connection) handleLeaveQueue(ctx context.Context) {
	c.mu.Lock()
	ticketID := c.ticketID
	cancel := c.cancelQueue
	c.mu.Unlock()

	if ticketID == "" {
		return
	}
	if cancel != nil {
		cancel()
	}

	_, _ = c.h.matchmakingClient.LeaveQueue(c.authContext(ctx), &matcharenav1.LeaveQueueRequest{QueueTicketId: ticketID})
}

// startPlayGame runs on the same goroutine as handleJoinQueue (it's called
// directly, not spawned) so it never writes to c.ws concurrently with
// handleJoinQueue's own writes above.
func (c *connection) startPlayGame(ctx context.Context) {
	stream, err := c.h.gameSessionClient.PlayGame(c.authContext(ctx))
	if err != nil {
		c.sendError("failed to start game")
		return
	}

	c.mu.Lock()
	c.playStream = stream
	sessionID := c.sessionID
	c.mu.Unlock()

	first := &matcharenav1.PlayerAction{
		PlayerId:        c.playerID,
		SessionId:       sessionID,
		ActionType:      "join",
		ClientTimestamp: timestamppb.New(time.Now()),
	}
	if err := stream.Send(first); err != nil {
		c.sendError("failed to join session")
		return
	}

	for {
		update, err := stream.Recv()
		if err != nil {
			return
		}

		_ = c.ws.WriteJSON(toServerMessage(update))

		if update.GetSessionEnded() {
			return
		}
	}
}

func (c *connection) handleAction() {
	c.mu.Lock()
	stream := c.playStream
	sessionID := c.sessionID
	c.mu.Unlock()

	if stream == nil {
		return
	}

	_ = stream.Send(&matcharenav1.PlayerAction{
		PlayerId:        c.playerID,
		SessionId:       sessionID,
		ActionType:      "click",
		ClientTimestamp: timestamppb.New(time.Now()),
	})
}

func toServerMessage(update *matcharenav1.SessionUpdate) serverMessage {
	scores := make([]scoreDTO, 0, len(update.GetScores()))
	for _, s := range update.GetScores() {
		scores = append(scores, scoreDTO{PlayerID: s.GetPlayerId(), Score: s.GetScore()})
	}

	flags := make([]flagDTO, 0, len(update.GetFlags()))
	for _, f := range update.GetFlags() {
		flags = append(flags, flagDTO{PlayerID: f.GetPlayerId(), Rule: f.GetRule(), Detail: f.GetDetail()})
	}

	return serverMessage{
		Type:         "session_update",
		SessionID:    update.GetSessionId(),
		Scores:       scores,
		Flags:        flags,
		SessionEnded: update.GetSessionEnded(),
	}
}