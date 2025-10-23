package com.example.mybesttrip_be.domain.balance.entity;

import com.example.mybesttrip_be.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"question_id","user_id"}))
public class BalanceAnswer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    private com.example.mybesttrip_be.domain.balance.entity.BalanceQuestion question;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    private User user;

    @Column(nullable=false)
    private Integer choice; // 1 or 2
}
