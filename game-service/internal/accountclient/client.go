package accountclient

import (
	"context"
	"os"
	"time"

	matcharenav1 "github.com/ihsanguldur/match-arena/game-service/internal/gen/match_arena/v1"
	"github.com/ihsanguldur/match-arena/game-service/internal/session"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"google.golang.org/protobuf/types/known/timestamppb"
)

type Client struct {
	conn   *grpc.ClientConn
	client matcharenav1.AccountLookupServiceClient
}

func NewClient() (*Client, error) {
	addr := os.Getenv("ACCOUNT_SERVICE_ADDR")
	if addr == "" {
		addr = "localhost:9090"
	}

	conn, err := grpc.NewClient(addr, grpc.WithTransportCredentials(insecure.NewCredentials()))
	if err != nil {
		return nil, err
	}

	client := matcharenav1.NewAccountLookupServiceClient(conn)

	pingCtx, cancel := context.WithTimeout(context.Background(), time.Second*5)
	defer cancel()
	if _, err := client.Ping(pingCtx, &matcharenav1.PingRequest{Message: "game-service"}); err != nil {
		conn.Close()
		return nil, err
	}

	return &Client{conn: conn, client: client}, nil
}

func (c *Client) Close() error {
	return c.conn.Close()
}

func (c *Client) ReportMatchResult(ctx context.Context, sessionID string, results []session.PlayerResult, playedAt time.Time) error {
	req := &matcharenav1.ReportMatchResultRequest{
		SessionId: sessionID,
		PlayedAt:  timestamppb.New(playedAt),
	}

	for _, r := range results {
		req.Participants = append(req.Participants, &matcharenav1.MatchParticipant{
			PlayerId:  r.PlayerID,
			Score:     r.Score,
			FlagCount: r.FlagCount,
		})
	}

	_, err := c.client.ReportMatchResult(ctx, req)
	return err
}
