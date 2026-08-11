package session

import (
	"context"
	"log"
	"sync"
	"time"

	"github.com/google/uuid"
)

type MatchReporter interface {
	ReportMatchResult(ctx context.Context, sessionID string, results []PlayerResult, playedAt time.Time) error
}

type Manager struct {
	mu       sync.Mutex
	sessions map[string]*Session
	reporter MatchReporter
}

func NewManager(reporter MatchReporter) *Manager {
	return &Manager{
		sessions: make(map[string]*Session),
		reporter: reporter,
	}
}

func (m *Manager) Create(ctx context.Context, playerIDs []string) *Session {
	session := NewSession(uuid.NewString(), playerIDs)

	m.mu.Lock()
	m.sessions[session.ID] = session
	m.mu.Unlock()

	go session.Run(ctx)
	go func() {
		<-session.Done()

		m.mu.Lock()
		delete(m.sessions, session.ID)
		m.mu.Unlock()

		reportCtx, cancel := context.WithTimeout(context.Background(), time.Second*5)
		defer cancel()
		if err := m.reporter.ReportMatchResult(reportCtx, session.ID, session.Results(), time.Now()); err != nil {
			log.Printf("manager: failed to report match result for session %s: %v", session.ID, err)
		}
	}()

	return session
}

func (m *Manager) Get(sessionID string) (*Session, bool) {
	m.mu.Lock()
	defer m.mu.Unlock()
	session, ok := m.sessions[sessionID]
	return session, ok
}
