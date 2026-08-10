package anticheat

import (
	"time"

	matcharenav1 "github.com/ihsanguldur/match-arena/game-service/internal/gen/match_arena/v1"
)

type playerHistory struct {
	lastActionAt time.Time
	fastStreak   int
	intervals    []time.Duration
	recentTimes  []time.Time
}

type Rule interface {
	Name() string
	Check(now time.Time, action *matcharenav1.PlayerAction, h *playerHistory) *matcharenav1.AntiCheatFlag
}

type Engine struct {
	rules   []Rule
	history map[string]*playerHistory
}

func NewEngine() *Engine {
	return &Engine{
		rules:   defaultRules(),
		history: make(map[string]*playerHistory),
	}
}

func (e *Engine) Evaluate(action *matcharenav1.PlayerAction, now time.Time) []*matcharenav1.AntiCheatFlag {
	h, ok := e.history[action.GetPlayerId()]
	if !ok {
		h = &playerHistory{}
		e.history[action.GetPlayerId()] = h
	}

	var flags []*matcharenav1.AntiCheatFlag
	for _, rule := range e.rules {
		if flag := rule.Check(now, action, h); flag != nil {
			flags = append(flags, flag)
		}
	}

	h.lastActionAt = now

	return flags
}

func defaultRules() []Rule {
	return []Rule{
		reactionFloorRule{},
		burstRateRule{},
		timestampDriftRule{},
		botRegularityRule{},
	}
}
