package server

import (
	"context"
	"sync"

	"github.com/ihsanguldur/match-arena/game-service/internal/auth"
	"github.com/ihsanguldur/match-arena/game-service/internal/gamesession"
	matcharenav1 "github.com/ihsanguldur/match-arena/game-service/internal/gen/match_arena/v1"
	"github.com/ihsanguldur/match-arena/game-service/internal/matchmaking"
	"github.com/ihsanguldur/match-arena/game-service/internal/session"
	"github.com/redis/go-redis/v9"
	"google.golang.org/grpc"
	"google.golang.org/grpc/reflection"
)

func New(ctx context.Context, redisClient *redis.Client) (*grpc.Server, *sync.WaitGroup) {
	queue := matchmaking.NewQueue(redisClient)
	notifier := matchmaking.NewNotifier()
	matchmakingServer := matchmaking.NewServer(queue, notifier)

	matcher := matchmaking.NewMatcher(queue)
	sessionManager := session.NewManager()

	var wg sync.WaitGroup
	wg.Add(1)
	go func() {
		defer wg.Done()
		matcher.Start(ctx)
	}()

	wg.Add(1)
	go func() {
		defer wg.Done()
		for {
			select {
			case <-ctx.Done():
				return
			case match := <-matcher.Matches:
				session := sessionManager.Create(ctx, match.PlayerIDs)

				matchFound := &matcharenav1.MatchFound{
					SessionId: session.ID,
					PlayerIds: match.PlayerIDs,
				}

				for _, ticketID := range match.TicketIDs {
					notifier.Notify(ticketID, matchFound)
				}
			}
		}
	}()

	srv := grpc.NewServer(grpc.UnaryInterceptor(auth.UnaryInterceptor()), grpc.StreamInterceptor(auth.StreamInterceptor()))
	matcharenav1.RegisterGameSessionServiceServer(srv, gamesession.NewServer(sessionManager))
	matcharenav1.RegisterMatchmakingServiceServer(srv, matchmakingServer)
	reflection.Register(srv)

	return srv, &wg
}
