package com.example.mybesttrip_be.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = true) // allow social-only if needed
    private String email;

    private String passwordHash;

    @Column(unique = true, nullable = false, length = 40)
    private String nickname;

    @Column(length = 4)
    private String mbti; // e.g., INFP

    private Boolean tosAgreed;

    private String profileImageUrl;
    private String oneLiner;
    private String locationCode;
}