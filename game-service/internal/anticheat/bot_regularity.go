package anticheat

import (
	"fmt"
	"math"
	"time"

	matcharenav1 "github.com/ihsanguldur/match-arena/game-service/internal/gen/match_arena/v1"
)

const (
	botRegularitySampleSize = 6
	botRegularityStdDevMax  = 15 * time.Millisecond
)

type botRegularityRule struct{}

func (r botRegularityRule) Name() string {
	return RuleBotRegularity
}

func (r botRegularityRule) Check(now time.Time, action *matcharenav1.PlayerAction, h *playerHistory) *matcharenav1.AntiCheatFlag {
	if h.lastActionAt.IsZero() {
		return nil
	}

	interval := now.Sub(h.lastActionAt)
	h.intervals = append(h.intervals, interval)
	if len(h.intervals) > botRegularitySampleSize {
		h.intervals = h.intervals[1:]
	}

	if len(h.intervals) < botRegularitySampleSize {
		return nil
	}

	stddev := stdDevOf(h.intervals)
	if stddev > botRegularityStdDevMax {
		return nil
	}

	h.intervals = nil

	return &matcharenav1.AntiCheatFlag{
		PlayerId: action.GetPlayerId(),
		Rule:     r.Name(),
		Detail:   fmt.Sprintf("last %d intervals had stddev %s (too uniform)", botRegularitySampleSize, stddev),
	}
}

func stdDevOf(durations []time.Duration) time.Duration {
	var sum time.Duration
	for _, d := range durations {
		sum += d
	}
	mean := float64(sum) / float64(len(durations))

	var variance float64
	for _, d := range durations {
		diff := float64(d) - mean
		variance += diff * diff
	}

	variance /= float64(len(durations))

	return time.Duration(math.Sqrt(variance))
}
