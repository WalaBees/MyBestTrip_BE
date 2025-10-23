package com.example.mybesttrip_be.domain.social.entity;

import com.example.mybesttrip_be.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SocialAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false, length = 20)
    private String provider; // GOOGLE, KAKAO, APPLE

    @Column(nullable = false, length = 255)
    private String providerUserId;
}
