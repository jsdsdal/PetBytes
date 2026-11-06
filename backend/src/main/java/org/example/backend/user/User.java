package org.example.backend.user;

import jakarta.persistence.*;
import org.example.backend.personality.user.UserPersonality;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String username;

    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserPersonality personality;

    @Column(nullable = false)
    private Instant createdAt;

    protected User() {} // JPA

    public User(String username,
                String email,
                String passwordHash,
                UserPersonality personality) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.personality = personality;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public UserPersonality getPersonality() { return personality; }
    public Instant getCreatedAt() { return createdAt; }

    public void setUsername (String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setPersonality(UserPersonality personality) { this.personality = personality; }
}