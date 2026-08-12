package gamesession

import (
	"github.com/ihsanguldur/match-arena/game-service/internal/auth"
	matcharenav1 "github.com/ihsanguldur/match-arena/game-service/internal/gen/match_arena/v1"
	"github.com/ihsanguldur/match-arena/game-service/internal/session"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

type Server struct {
	matcharenav1.UnimplementedGameSessionServiceServer
	manager *session.Manager
}

func NewServer(manager *session.Manager) *Server {
	return &Server{
		manager: manager,
	}
}

func (s *Server) PlayGame(stream matcharenav1.GameSessionService_PlayGameServer) error {
	ctx := stream.Context()
	playerID, ok := auth.PlayerIDFromContext(ctx)
	if !ok {
		return status.Error(codes.Unauthenticated, "missing authenticated player")
	}

	first, err := stream.Recv()
	if err != nil {
		return err
	}
	if first.GetPlayerId() != playerID {
		return status.Error(codes.PermissionDenied, "player id mismatch")
	}

	sess, ok := s.manager.Get(first.GetSessionId())
	if !ok {
		return status.Error(codes.NotFound, "session not found")
	}
	if !sess.HasPlayer(playerID) {
		return status.Error(codes.PermissionDenied, "player not part of this session")
	}

	updates := make(chan *matcharenav1.SessionUpdate, 1)
	sess.Subscribe(playerID, updates)
	defer sess.Unsubscribe(playerID)

	recvErr := make(chan error, 1)
	go func() {
		for {
			action, err := stream.Recv()
			if err != nil {
				recvErr <- err
				return
			}
			if action.GetPlayerId() != playerID {
				continue
			}
			sess.Action(action)
		}
	}()

	for {
		select {
		case <-ctx.Done():
			return ctx.Err()
		case err := <-recvErr:
			return err
		case update := <-updates:
			if err := stream.Send(update); err != nil {
				return err
			}
			if update.GetSessionEnded() {
				return nil
			}
		}
	}
}
