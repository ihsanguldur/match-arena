package server

import (
	"context"
	"sync"

	"github.com/ihsanguldur/match-arena/game-service/internal/auth"
	matcharenav1 "github.com/ihsanguldur/match-arena/game-service/internal/gen/match_arena/v1"
	"github.com/ihsanguldur/match-arena/game-service/internal/matchmaking"
	"github.com/redis/go-redis/v9"
	"google.golang.org/grpc"
	"google.golang.org/grpc/reflection"
)

func New(ctx context.Context, redisClient *redis.Client) (*grpc.Server, *sync.WaitGroup) {
	queue := matchmaking.NewQueue(redisClient)
	matchmakingServer := matchmaking.NewServer(queue)

	matcher := matchmaking.NewMatcher(queue)

	var wg sync.WaitGroup
	wg.Add(1)
	go func() {
		defer wg.Done()
		matcher.Start(ctx)
	}()

	srv := grpc.NewServer(grpc.UnaryInterceptor(auth.UnaryInterceptor()))
	matcharenav1.RegisterGameSessionServiceServer(srv, &matcharenav1.UnimplementedGameSessionServiceServer{})
	matcharenav1.RegisterMatchmakingServiceServer(srv, matchmakingServer)
	reflection.Register(srv)

	return srv, &wg
}
