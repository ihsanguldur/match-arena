package matchmaking

import (
	"sync"

	matcharenav1 "github.com/ihsanguldur/match-arena/game-service/internal/gen/match_arena/v1"
)

type Notifier struct {
	mu   sync.Mutex
	subs map[string]chan *matcharenav1.MatchFound
}

func NewNotifier() *Notifier {
	return &Notifier{
		subs: make(map[string]chan *matcharenav1.MatchFound),
	}
}

func (n *Notifier) Subscribe(ticketID string) <-chan *matcharenav1.MatchFound {
	ch := make(chan *matcharenav1.MatchFound, 1)
	n.mu.Lock()
	n.subs[ticketID] = ch
	n.mu.Unlock()
	return ch
}

func (n *Notifier) Notify(ticketID string, mf *matcharenav1.MatchFound) {
	n.mu.Lock()
	ch, ok := n.subs[ticketID]
	if ok {
		delete(n.subs, ticketID)
	}
	n.mu.Unlock()
	if ok {
		ch <- mf
	}
}

func (n *Notifier) Unsubscribe(ticketID string) {
	n.mu.Lock()
	delete(n.subs, ticketID)
	n.mu.Unlock()
}
