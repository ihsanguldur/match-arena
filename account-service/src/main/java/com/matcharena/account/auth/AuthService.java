package com.matcharena.account.auth;

import com.matcharena.account.auth.dto.AuthResponse;
import com.matcharena.account.auth.dto.LoginRequestDto;
import com.matcharena.account.auth.dto.RegisterRequestDto;
import com.matcharena.account.security.JwtService;
import com.matcharena.account.user.User;
import com.matcharena.account.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequestDto request) {
        boolean isUsernameExists = userRepository.existsByUsername(request.username());
        boolean isEmailExists = userRepository.existsByEmail(request.email());

        if (isUsernameExists) {
            throw new IllegalArgumentException("Username already taken");
        }

        if (isEmailExists) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = User.create(
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password())
        );
        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequestDto request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token);
    }
}
