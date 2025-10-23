package com.example.mybesttrip_be.domain.review.entity;

import com.example.mybesttrip_be.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long destinationId; // simple FK by id for now

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String body;

    @Column(nullable = false)
    private Integer rating; // 1-5

    @Builder.Default
    private Integer likeCount = 0;
}
