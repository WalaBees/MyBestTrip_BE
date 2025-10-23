package com.example.mybesttrip_be.domain.balance.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BalanceQuestion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=200)
    private String question;

    @Column(nullable=false, length=100)
    private String option1;

    @Column(nullable=false, length=100)
    private String option2;

    private Boolean isActive = true;
}
