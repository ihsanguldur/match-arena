package matchmaking

import (
	"context"
	"time"

	"github.com/ihsanguldur/match-arena/game-service/internal/auth"
	matcharenav1 "github.com/ihsanguldur/match-arena/game-service/internal/gen/match_arena/v1"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

type Server struct {
	matcharenav1.UnimplementedMatchmakingServiceServer
	queue    *Queue
	notifier *Notifier
}

func NewServer(queue *Queue, notifier *Notifier) *Server {
	return &Server{
		queue:    queue,
		notifier: notifier,
	}
}

func (s *Server) JoinQueue(req *matcharenav1.JoinQueueRequest, stream matcharenav1.MatchmakingService_JoinQueueServer) error {
	ctx := stream.Context()
	playerID, ok := auth.PlayerIDFromContext(ctx)
	if !ok {
		return status.Error(codes.Unauthenticated, "missing authenticated player")
	}

	ticketID, err := s.queue.Join(ctx, playerID, req.GetRating())
	if err != nil {
		return status.Error(codes.Internal, "failed to join queue")
	}

	if err := stream.Send(&matcharenav1.JoinQueueUpdate{
		Update: &matcharenav1.JoinQueueUpdate_QueueTicketId{QueueTicketId: ticketID},
	}); err != nil {
		return err
	}

	matchCh := s.notifier.Subscribe(ticketID)

	select {
	case <-ctx.Done():
		s.notifier.Unsubscribe(ticketID)

		cleanupCtx, cancel := context.WithTimeout(context.Background(), time.Second*5)
		defer cancel()
		s.queue.Leave(cleanupCtx, ticketID, playerID)

		return ctx.Err()
	case match := <-matchCh:
		return stream.Send(&matcharenav1.JoinQueueUpdate{
			Update: &matcharenav1.JoinQueueUpdate_MatchFound{MatchFound: match},
		})
	}
}

func (s *Server) LeaveQueue(ctx context.Context, req *matcharenav1.LeaveQueueRequest) (*matcharenav1.LeaveQueueResponse, error) {
	playerID, ok := auth.PlayerIDFromContext(ctx)
	if !ok {
		return nil, status.Error(codes.Unauthenticated, "missing authenticated player")
	}

	success, err := s.queue.Leave(ctx, req.GetQueueTicketId(), playerID)
	if err != nil {
		return nil, status.Error(codes.Internal, "failed to leave queue")
	}

	return &matcharenav1.LeaveQueueResponse{Success: success}, nil
}
