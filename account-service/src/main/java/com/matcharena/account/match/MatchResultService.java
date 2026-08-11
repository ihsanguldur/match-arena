package com.matcharena.account.match;

import com.matcharena.account.user.User;
import com.matcharena.account.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchResultService {

    private static final int K_FACTOR = 32;

    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final MatchParticipantRepository matchParticipantRepository;

    private static int eloDelta(int ratingSelf, int ratingOpponent, double actualScore) {
        double expected = 1.0 / (1.0 + Math.pow(10, (ratingOpponent - ratingSelf) / 400.0));
        return (int) Math.round(K_FACTOR * (actualScore - expected));
    }

    private static String resultOf(double actualScore) {
        if (actualScore == 1.0) return "WIN";
        if (actualScore == 0.0) return "LOSE";
        return "DRAW";
    }

    @Transactional
    public boolean reportResult(String sessionId, List<com.matcharena.grpc.v1.MatchParticipant> participants, Instant playedAt) {
        if (participants.size() != 2) {
            return false;
        }

        Optional<User> maybeUserA = userRepository.findByUsername(participants.get(0).getPlayerId());
        Optional<User> maybeUserB = userRepository.findByUsername(participants.get(1).getPlayerId());
        if (maybeUserA.isEmpty() || maybeUserB.isEmpty()) {
            return false;
        }

        User userA = maybeUserA.get();
        User userB = maybeUserB.get();

        int scoreA = participants.get(0).getScore();
        int scoreB = participants.get(1).getScore();

        double actualA = scoreA == scoreB ? 0.5 : (scoreA > scoreB ? 1.0 : 0.0);
        double actualB = 1.0 - actualA;

        int deltaA = eloDelta(userA.getRating(), userB.getRating(), actualA);
        int deltaB = eloDelta(userB.getRating(), userA.getRating(), actualB);

        if (participants.get(0).getFlagCount() > 0 && deltaA > 0) {
            deltaA = 0;
        }

        if (participants.get(1).getFlagCount() > 0 && deltaB > 0) {
            deltaB = 0;
        }

        int ratingBeforeA = userA.getRating();
        int ratingBeforeB = userB.getRating();

        userA.applyRatingDelta(deltaA);
        userB.applyRatingDelta(deltaB);

        matchRepository.save(Match.create(sessionId, playedAt));
        matchParticipantRepository.save(MatchParticipant.create(
                sessionId, userA.getUsername(), scoreA, resultOf(actualA), ratingBeforeA, userA.getRating(), participants.get(0).getFlagCount()
        ));
        matchParticipantRepository.save(MatchParticipant.create(
                sessionId, userB.getUsername(), scoreB, resultOf(actualB), ratingBeforeB, userB.getRating(), participants.get(1).getFlagCount()
        ));

        return true;
    }
}
