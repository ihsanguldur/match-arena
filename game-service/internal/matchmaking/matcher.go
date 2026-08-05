package matchmaking

import (
	"context"
	"log"
	"math"
	"time"
)

const (
	baseWindow   = 50.0
	windowGrowth = 10.0
	maxWindow    = 300.0
	tickInterval = 500 * time.Millisecond
)

type Match struct {
	TicketIDs []string
	PlayerIDs []string
	Ratings   []float64
}

type Matcher struct {
	queue   *Queue
	Matches chan Match
}

func NewMatcher(queue *Queue) *Matcher {
	return &Matcher{
		queue:   queue,
		Matches: make(chan Match, 16),
	}
}

func (m *Matcher) Start(ctx context.Context) {
	ticker := time.NewTicker(tickInterval)
	defer ticker.Stop()

	for {
		select {
		case <-ctx.Done():
			return
		case <-ticker.C:
			m.tick(ctx)
		}
	}
}

func windowFor(joinedAt time.Time) float64 {
	elapsed := time.Since(joinedAt).Seconds()
	w := baseWindow + windowGrowth*elapsed
	return math.Min(w, maxWindow)
}

func (m *Matcher) tick(ctx context.Context) {
	candidates, err := m.queue.Candidates(ctx)
	if err != nil {
		log.Printf("matcher: failed to read queue: %v", err)
		return
	}

	for i := 0; i < len(candidates)-1; {
		a, b := candidates[i], candidates[i+1]
		window := math.Max(windowFor(a.JoinedAt), windowFor(b.JoinedAt))
		if math.Abs(a.Rating-b.Rating) > window {
			i++
			continue
		}

		if err := m.queue.Remove(ctx, a.TicketID, b.TicketID); err != nil {
			log.Printf("matcher: failed to remove matched tickets: %v", err)
			i++
			continue
		}

		log.Printf("matcher: paired %s (rating %.0f) with %s (rating %.0f)", a.PlayerID, a.Rating, b.PlayerID, b.Rating)
		m.Matches <- Match{
			TicketIDs: []string{a.TicketID, b.TicketID},
			PlayerIDs: []string{a.PlayerID, b.PlayerID},
			Ratings:   []float64{a.Rating, b.Rating},
		}

		i += 2
	}
}
