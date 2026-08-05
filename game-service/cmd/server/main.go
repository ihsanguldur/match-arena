package main

import (
	"context"
	"log"
	"net"
	"os/signal"
	"syscall"

	"github.com/ihsanguldur/match-arena/game-service/internal/redis"
	"github.com/ihsanguldur/match-arena/game-service/internal/server"
)

// for p1 d0c12b2c-4d7b-43df-8ab1-60e74aded683
// for p2 fa73c833-7d6f-4431-80e3-f32b69f35baf
func main() {
	ctx, stop := signal.NotifyContext(context.Background(), syscall.SIGINT, syscall.SIGTERM)
	defer stop()

	lis, err := net.Listen("tcp", ":9091")
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}

	redisClient := redis.NewClient()

	srv, wg := server.New(ctx, redisClient)

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

	log.Println("gRPC server stopped")
}
