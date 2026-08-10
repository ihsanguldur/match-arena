package anticheat

import (
	"fmt"
	"time"

	matcharenav1 "github.com/ihsanguldur/match-arena/game-service/internal/gen/match_arena/v1"
)

const timestampDriftThreshold = 3 * time.Second

type timestampDriftRule struct{}

func (r timestampDriftRule) Name() string {
	return RuleTimestampDrift
}

func (r timestampDriftRule) Check(now time.Time, action *matcharenav1.PlayerAction, h *playerHistory) *matcharenav1.AntiCheatFlag {
	ts := action.GetClientTimestamp()
	if ts == nil {
		return nil
	}

	clientTime := ts.AsTime()
	drift := now.Sub(clientTime)
	if drift < 0 {
		drift = -drift
	}

	if drift <= timestampDriftThreshold {
		return nil
	}

	return &matcharenav1.AntiCheatFlag{
		PlayerId: action.GetPlayerId(),
		Rule:     r.Name(),
		Detail:   fmt.Sprintf("client timestamp drifted %s from server time", drift),
	}
}
