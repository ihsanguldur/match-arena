package matchmaking

import (
	"context"
	"errors"
	"fmt"
	"strconv"
	"time"

	"github.com/google/uuid"
	"github.com/redis/go-redis/v9"
)

const queueKey = "matchmaking:queue"

type Queue struct {
	client *redis.Client
}

type Candidate struct {
	TicketID string
	PlayerID string
	Rating   float64
	JoinedAt time.Time
}

func ticketKey(ticketID string) string {
	return fmt.Sprintf("matchmaking:ticket:%s", ticketID)
}

func NewQueue(client *redis.Client) *Queue {
	return &Queue{
		client: client,
	}
}

func (q *Queue) Join(ctx context.Context, playerID string, rating int32) (string, error) {
	ticketID := uuid.NewString()

	err := q.client.HSet(ctx, ticketKey(ticketID), map[string]interface{}{
		"player_id": playerID,
		"rating":    rating,
		"joined_at": time.Now().UnixMilli(),
	}).Err()
	if err != nil {
		return "", err
	}

	err = q.client.ZAdd(ctx, queueKey, redis.Z{
		Score:  float64(rating),
		Member: ticketID,
	}).Err()
	if err != nil {
		return "", err
	}

	return ticketID, nil
}

func (q *Queue) Leave(ctx context.Context, ticketID string, callerPlayerID string) (bool, error) {
	storedPlayerID, err := q.client.HGet(ctx, ticketKey(ticketID), "player_id").Result()
	if errors.Is(err, redis.Nil) {
		return false, nil
	}
	if err != nil {
		return false, err
	}
	if storedPlayerID != callerPlayerID {
		return false, nil
	}

	removed, err := q.client.ZRem(ctx, queueKey, ticketID).Result()
	if err != nil {
		return false, err
	}

	q.client.Del(ctx, ticketKey(ticketID))

	return removed > 0, nil
}

func (q *Queue) Candidates(ctx context.Context) ([]Candidate, error) {
	entries, err := q.client.ZRangeWithScores(ctx, queueKey, 0, -1).Result()
	if err != nil {
		return nil, err
	}

	candidates := make([]Candidate, 0, len(entries))
	for _, entry := range entries {
		ticketID := entry.Member.(string)

		values, err := q.client.HMGet(ctx, ticketKey(ticketID), "player_id", "joined_at").Result()
		if err != nil {
			return nil, err
		}
		if values[0] == nil || values[1] == nil {
			continue
		}

		playerID := values[0].(string)
		joinedAtMs, err := strconv.ParseInt(values[1].(string), 10, 64)
		if err != nil {
			continue
		}

		candidates = append(candidates, Candidate{
			TicketID: ticketID,
			PlayerID: playerID,
			Rating:   entry.Score,
			JoinedAt: time.UnixMilli(joinedAtMs),
		})
	}

	return candidates, nil
}

func (q *Queue) Remove(ctx context.Context, ticketIDs ...string) error {
	pipe := q.client.Pipeline()

	members := make([]interface{}, len(ticketIDs))
	for i, id := range ticketIDs {
		members[i] = id
		pipe.Del(ctx, ticketKey(id))
	}
	pipe.ZRem(ctx, queueKey, members...)

	_, err := pipe.Exec(ctx)
	return err
}
