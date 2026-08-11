package com.matcharena.account.match;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Entity
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Table(name = "matches")
public class Match {

    @Id
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "session_id")
    private String sessionId;

    @ToString.Include
    @Column(name = "played_at", nullable = false)
    private Instant playedAt;

    public static Match create(String sessionId, Instant playedAt) {
        Match match = new Match();
        match.sessionId = sessionId;
        match.playedAt = playedAt;

        return match;
    }
}
