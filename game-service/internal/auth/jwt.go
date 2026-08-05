package auth

import (
	"fmt"
	"os"

	"github.com/golang-jwt/jwt/v5"
)

func ValidateToken(tokenString string) (string, error) {
	token, err := jwt.Parse(tokenString, func(t *jwt.Token) (interface{}, error) {
		if _, ok := t.Method.(*jwt.SigningMethodHMAC); !ok {
			return nil, fmt.Errorf("unexpected signing method: %v", t.Header["alg"])
		}
		secret, ok := os.LookupEnv("JWT_SECRET")
		if !ok {
			return nil, fmt.Errorf("JWT_SECRET environment variable not set")
		}
		return []byte(secret), nil
	})
	if err != nil {
		return "", err
	}

	claims, ok := token.Claims.(jwt.MapClaims)
	if !ok || !token.Valid {
		return "", fmt.Errorf("invalid token")
	}

	return claims.GetSubject()
}
