package main

import (
	"log"
	"net"

	matcharenav1 "github.com/ihsanguldur/match-arena/game-service/internal/gen/match_arena/v1"
	"google.golang.org/grpc"
)

func main() {
	lis, err := net.Listen("tcp", ":9091")
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}

	srv := grpc.NewServer()
	matcharenav1.RegisterGameSessionServiceServer(srv, &matcharenav1.UnimplementedGameSessionServiceServer{})
	matcharenav1.RegisterMatchmakingServiceServer(srv, &matcharenav1.UnimplementedMatchmakingServiceServer{})

	log.Println("game-service gRPC listening on :9091")
	if err := srv.Serve(lis); err != nil {
		log.Fatalf("serve failed: %v", err)
	}
}