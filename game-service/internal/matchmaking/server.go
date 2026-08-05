package matchmaking

import (
	"context"

	"github.com/ihsanguldur/match-arena/game-service/internal/auth"
	matcharenav1 "github.com/ihsanguldur/match-arena/game-service/internal/gen/match_arena/v1"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

type Server struct {
	matcharenav1.UnimplementedMatchmakingServiceServer
	queue *Queue
}

func NewServer(queue *Queue) *Server {
	return &Server{
		queue: queue,
	}
}

func (s *Server) JoinQueue(ctx context.Context, req *matcharenav1.JoinQueueRequest) (*matcharenav1.JoinQueueResponse, error) {
	playerID, ok := auth.PlayerIDFromContext(ctx)
	if !ok {
		return nil, status.Error(codes.Unauthenticated, "missing authenticated player")
	}

	ticketID, err := s.queue.Join(ctx, playerID, req.GetRating())
	if err != nil {
		return nil, status.Error(codes.Internal, "failed to join queue")
	}

	return &matcharenav1.JoinQueueResponse{QueueTicketId: ticketID}, nil
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
