package auth

import (
	"context"
	"strings"

	"google.golang.org/grpc"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/metadata"
	"google.golang.org/grpc/status"
)

type authenticatedStream struct {
	grpc.ServerStream
	ctx context.Context
}

func (s *authenticatedStream) Context() context.Context {
	return s.ctx
}

func StreamInterceptor() grpc.StreamServerInterceptor {
	return func(srv interface{}, ss grpc.ServerStream, info *grpc.StreamServerInfo, handler grpc.StreamHandler) error {
		playerID, err := authenticate(ss.Context())
		if err != nil {
			return err
		}

		return handler(srv, &authenticatedStream{
			ServerStream: ss,
			ctx:          WithPlayerID(ss.Context(), playerID),
		})
	}
}

func UnaryInterceptor() grpc.UnaryServerInterceptor {
	return func(ctx context.Context, req interface{}, info *grpc.UnaryServerInfo, handler grpc.UnaryHandler) (interface{}, error) {
		playerID, err := authenticate(ctx)
		if err != nil {
			return nil, err
		}

		return handler(WithPlayerID(ctx, playerID), req)
	}
}

func authenticate(ctx context.Context) (string, error) {
	md, ok := metadata.FromIncomingContext(ctx)
	if !ok {
		return "", status.Error(codes.Unauthenticated, "missing metadata")
	}

	values := md.Get("authorization")
	if len(values) == 0 {
		return "", status.Error(codes.Unauthenticated, "missing authorization header")
	}
	token := strings.TrimPrefix(values[0], "Bearer ")

	playerID, err := ValidateToken(token)
	if err != nil {
		return "", status.Error(codes.Unauthenticated, "invalid token")
	}

	return playerID, nil
}
