package anticheat

import (
	"fmt"
	"time"

	matcharenav1 "github.com/ihsanguldur/match-arena/game-service/internal/gen/match_arena/v1"
)

const (
	reactionFloorThreshold = time.Millisecond * 100
	reactionFloorStreak    = 3
)

type reactionFloorRule struct{}

func (r reactionFloorRule) Name() string {
	return RuleReactionFloor
}

func (r reactionFloorRule) Check(now time.Time, action *matcharenav1.PlayerAction, h *playerHistory) *matcharenav1.AntiCheatFlag {
	if h.lastActionAt.IsZero() {
		return nil
	}

	interval := now.Sub(h.lastActionAt)
	if interval >= reactionFloorThreshold {
		h.fastStreak = 0
		return nil
	}

	h.fastStreak++
	if h.fastStreak < reactionFloorStreak {
		return nil
	}

	h.fastStreak = 0
	return &matcharenav1.AntiCheatFlag{
		PlayerId: action.GetPlayerId(),
		Rule:     r.Name(),
		Detail:   fmt.Sprintf("%d consecutive actions under %s (last interval: %s)", reactionFloorStreak, reactionFloorThreshold, interval),
	}
}
