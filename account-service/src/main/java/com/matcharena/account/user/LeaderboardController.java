package com.matcharena.account.user;

import com.matcharena.account.user.dto.LeaderboardEntryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/leaderboard")
@RequiredArgsConstructor
public class LeaderboardController {

    private final UserRepository userRepository;

    @GetMapping
    public List<LeaderboardEntryResponse> leaderboard() {
        List<User> topUsers = userRepository.findTop50ByOrderByRatingDesc();

        List<LeaderboardEntryResponse> entries = new ArrayList<>();
        for (int i = 0; i < topUsers.size(); i++) {
            User user = topUsers.get(i);
            entries.add(new LeaderboardEntryResponse(user.getUsername(), i + 1, user.getRating()));
        }

        return entries;
    }
}
