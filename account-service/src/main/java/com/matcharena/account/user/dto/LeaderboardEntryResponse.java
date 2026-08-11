package com.matcharena.account.user.dto;

public record LeaderboardEntryResponse(
        String username,
        int rank,
        int rating
) {
}
