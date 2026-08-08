package session

import (
	"context"
	"sync"

	"github.com/google/uuid"
)

type Manager struct {
	mu       sync.Mutex
	sessions map[string]*Session
}

func NewManager() *Manager {
	return &Manager{
		sessions: make(map[string]*Session),
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
	}()

	return session
}

func (m *Manager) Get(sessionID string) (*Session, bool) {
	m.mu.Lock()
	defer m.mu.Unlock()
	session, ok := m.sessions[sessionID]
	return session, ok
}
