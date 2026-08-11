package main

import (
	"context"
	"log"
	"net"
	"os/signal"
	"syscall"

	"github.com/ihsanguldur/match-arena/game-service/internal/accountclient"
	"github.com/ihsanguldur/match-arena/game-service/internal/redis"
	"github.com/ihsanguldur/match-arena/game-service/internal/server"
)

func main() {
	ctx, stop := signal.NotifyContext(context.Background(), syscall.SIGINT, syscall.SIGTERM)
	defer stop()

	lis, err := net.Listen("tcp", ":9091")
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}

	redisClient := redis.NewClient()

	accountClient, err := accountclient.NewClient()
	if err != nil {
		log.Fatalf("failed to connect account-service: %v", err)
	}

	srv, wg := server.New(ctx, redisClient, accountClient)

	go func() {
		log.Println("game-service gRPC listening on :9091")
		if err := srv.Serve(lis); err != nil {
			log.Fatalf("serve failed: %v", err)
		}
	}()

	<-ctx.Done()
	log.Println("shutting down gRPC server")

	srv.GracefulStop()

	wg.Wait()

	if err := redisClient.Close(); err != nil {
		log.Fatalf("failed to close redis client: %v", err)
	}

	if err := accountClient.Close(); err != nil {
		log.Fatalf("failed to close account-service client: %v", err)
	}

	log.Println("gRPC server stopped")
}
