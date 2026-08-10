package anticheat

import (
	"fmt"
	"time"

	matcharenav1 "github.com/ihsanguldur/match-arena/game-service/internal/gen/match_arena/v1"
)

const (
	burstWindow    = time.Second
	burstThreshold = 10
)

type burstRateRule struct{}

func (r burstRateRule) Name() string {
	return RuleBurstRate
}

func (r burstRateRule) Check(now time.Time, action *matcharenav1.PlayerAction, h *playerHistory) *matcharenav1.AntiCheatFlag {
	h.recentTimes = append(h.recentTimes, now)

	cutoff := now.Add(-burstWindow)
	pruned := h.recentTimes[:0]
	for _, t := range h.recentTimes {
		if t.After(cutoff) {
			pruned = append(pruned, t)
		}
	}
	h.recentTimes = pruned

	if len(h.recentTimes) < burstThreshold {
		return nil
	}

	count := len(h.recentTimes)
	h.recentTimes = nil

	return &matcharenav1.AntiCheatFlag{
		PlayerId: action.GetPlayerId(),
		Rule:     r.Name(),
		Detail:   fmt.Sprintf("%d actions within %s", count, burstWindow),
	}
}
