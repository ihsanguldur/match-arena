package com.matcharena.account.match;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Table(name = "match_participants")
public class MatchParticipant {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    @Column(name = "match_id", nullable = false)
    private String matchId;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "player_id", nullable = false)
    private String playerId;

    @ToString.Include
    @Column(name = "score", nullable = false)
    private int score;

    @ToString.Include
    @Column(name = "result", nullable = false)
    private String result;

    @Column(name = "rating_before", nullable = false)
    private int ratingBefore;

    @Column(name = "rating_after", nullable = false)
    private int ratingAfter;

    @Column(name = "flag_count", nullable = false)
    private int flagCount;

    public static MatchParticipant create(String matchId, String playerId, int score, String result,
                                          int ratingBefore, int ratingAfter, int flagCount) {
        MatchParticipant participant = new MatchParticipant();
        participant.matchId = matchId;
        participant.playerId = playerId;
        participant.score = score;
        participant.result = result;
        participant.ratingBefore = ratingBefore;
        participant.ratingAfter = ratingAfter;
        participant.flagCount = flagCount;
        return participant;
    }
}

