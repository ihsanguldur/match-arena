package main

import (
	"log"
	"net/http"
	"net/http/httputil"
	"net/url"
	"os"

	"github.com/ihsanguldur/match-arena/game-service/internal/gatewayws"
)

func main() {
	gameServiceAddr := os.Getenv("GAME_SERVICE_ADDR")
	if gameServiceAddr == "" {
		gameServiceAddr = "localhost:9091"
	}

	accountServiceHTTPAddr := os.Getenv("ACCOUNT_SERVICE_HTTP_ADDR")
	if accountServiceHTTPAddr == "" {
		accountServiceHTTPAddr = "http://localhost:8080"
	}

	webDir := os.Getenv("WEB_CLIENT_DIR")
	if webDir == "" {
		webDir = "../client/web"
	}

	wsHandler, err := gatewayws.NewHandler(gameServiceAddr)
	if err != nil {
		log.Fatalf("failed to connect to game-service: %v", err)
	}

	accountTarget, err := url.Parse(accountServiceHTTPAddr)
	if err != nil {
		log.Fatalf("invalid ACCOUNT_SERVICE_HTTP_ADDR: %v", err)
	}
	accountProxy := httputil.NewSingleHostReverseProxy(accountTarget)

	mux := http.NewServeMux()
	mux.Handle("/ws", wsHandler)
	mux.Handle("/api/", accountProxy)
	mux.Handle("/", http.FileServer(http.Dir(webDir)))

	addr := ":8081"
	log.Printf("gateway listening on %s (web client dir: %s, game-service: %s, account-service: %s)",
		addr, webDir, gameServiceAddr, accountServiceHTTPAddr)
	if err := http.ListenAndServe(addr, mux); err != nil {
		log.Fatalf("gateway server failed: %v", err)
	}
}