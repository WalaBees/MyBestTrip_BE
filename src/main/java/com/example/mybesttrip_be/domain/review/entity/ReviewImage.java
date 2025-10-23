package com.example.mybesttrip_be.domain.review.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ReviewImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    private Review review;

    @Column(nullable=false, length=500)
    private String path;        // local path now

    @Column(length=120)
    private String originalName;
}
