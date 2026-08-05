package auth

import "context"

type contextKey int

const playerIDKey contextKey = iota

func WithPlayerID(ctx context.Context, playerID string) context.Context {
	return context.WithValue(ctx, playerIDKey, playerID)
}

func PlayerIDFromContext(ctx context.Context) (string, bool) {
	playerID, ok := ctx.Value(playerIDKey).(string)
	return playerID, ok
}
