package session

import (
	"context"
	"time"

	matcharenav1 "github.com/ihsanguldur/match-arena/game-service/internal/gen/match_arena/v1"
)

const (
	sessionDuration   = time.Minute*2 + time.Second*30
	inactivityTimeout = time.Second * 15
	inactivityCheck   = time.Second * 5
)

type sessionCmd interface {
	isSessionCmd()
}
type PlayerState struct {
	Score        int32
	LastActionAt time.Time
	Connected    bool
}

type Session struct {
	ID        string
	playerIDs []string
	players   map[string]*PlayerState
	subs      map[string]chan *matcharenav1.SessionUpdate

	inbox chan sessionCmd
	done  chan struct{}
}

type actionCmd struct {
	action *matcharenav1.PlayerAction
}

type subscribeCmd struct {
	playerID string
	ch       chan *matcharenav1.SessionUpdate
}

type unsubscribeCmd struct {
	playerID string
}

func (actionCmd) isSessionCmd()      {}
func (subscribeCmd) isSessionCmd()   {}
func (unsubscribeCmd) isSessionCmd() {}

func NewSession(id string, playerIDs []string) *Session {
	players := make(map[string]*PlayerState, len(playerIDs))
	for _, id := range playerIDs {
		players[id] = &PlayerState{}
	}

	return &Session{
		ID:        id,
		playerIDs: playerIDs,
		players:   players,
		subs:      make(map[string]chan *matcharenav1.SessionUpdate),
		inbox:     make(chan sessionCmd),
		done:      make(chan struct{}),
	}
}

func (s *Session) Done() <-chan struct{} { return s.done }

func (s *Session) HasPlayer(playerID string) bool {
	for _, id := range s.playerIDs {
		if id == playerID {
			return true
		}
	}
	return false
}

func (s *Session) Send(cmd sessionCmd) {
	select {
	case s.inbox <- cmd:
	case <-s.done:
	}
}

func (s *Session) Subscribe(playerID string, ch chan *matcharenav1.SessionUpdate) {
	s.Send(subscribeCmd{playerID: playerID, ch: ch})
}

func (s *Session) Unsubscribe(playerID string) {
	s.Send(unsubscribeCmd{playerID: playerID})
}

func (s *Session) Action(action *matcharenav1.PlayerAction) {
	s.Send(actionCmd{action: action})
}

func (s *Session) Run(ctx context.Context) {
	sessionTimer := time.NewTimer(sessionDuration)
	defer sessionTimer.Stop()

	inactivityTicker := time.NewTicker(inactivityCheck)
	defer inactivityTicker.Stop()

	for {
		select {
		case <-ctx.Done():
			s.end()
			return
		case <-sessionTimer.C:
			s.end()
			return
		case <-inactivityTicker.C:
			if s.anyPlayerInactive() {
				s.end()
				return
			}
		case cmd := <-s.inbox:
			s.handle(cmd)
		}
	}
}

func (s *Session) handle(cmd sessionCmd) {
	switch c := cmd.(type) {
	case actionCmd:
		player, ok := s.players[c.action.GetPlayerId()]
		if !ok {
			return
		}

		player.LastActionAt = time.Now()
		player.Score++
		s.broadcast(false)
	case subscribeCmd:
		s.subs[c.playerID] = c.ch
		if p, ok := s.players[c.playerID]; ok {
			p.Connected = true
			p.LastActionAt = time.Now()
		}
		s.broadcast(false)
	case unsubscribeCmd:
		delete(s.subs, c.playerID)
		if p, ok := s.players[c.playerID]; ok {
			p.Connected = false
		}
	}
}

func (s *Session) anyPlayerInactive() bool {
	for _, p := range s.players {
		if p.Connected && time.Since(p.LastActionAt) > inactivityTimeout {
			return true
		}
	}

	return false
}

func (s *Session) currentScores() []*matcharenav1.PlayerScore {
	scores := make([]*matcharenav1.PlayerScore, 0, len(s.players))
	for id, p := range s.players {
		scores = append(scores, &matcharenav1.PlayerScore{PlayerId: id, Score: p.Score})
	}

	return scores
}

func (s *Session) broadcast(ended bool) {
	update := &matcharenav1.SessionUpdate{
		SessionId:    s.ID,
		Scores:       s.currentScores(),
		SessionEnded: ended,
	}

	for _, ch := range s.subs {
		select {
		case ch <- update:
		default:
			select {
			case <-ch:
			default:
			}
			select {
			case ch <- update:
			default:
			}
		}
	}
}

func (s *Session) end() {
	s.broadcast(true)
	close(s.done)
}
