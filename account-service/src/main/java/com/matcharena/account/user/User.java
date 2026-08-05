package com.matcharena.account.user;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Entity
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor //(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "rating", nullable = false)
    private int rating = 1000;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public static User create(String username, String email, String passwordHash) {
        User user = new User();

        user.username = username;
        user.email = email;
        user.passwordHash = passwordHash;

        return user;
    }

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }
}
