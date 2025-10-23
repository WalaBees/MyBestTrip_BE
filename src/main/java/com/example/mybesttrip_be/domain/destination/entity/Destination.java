package com.example.mybesttrip_be.domain.destination.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Destination {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=200)
    private String title;

    @Builder.Default
    private Integer reviewCount = 0;

    @Builder.Default
    private Double averageRating = 0.0;
}
