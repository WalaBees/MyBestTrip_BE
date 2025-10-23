package com.example.mybesttrip_be.domain.stats.entity;

import com.example.mybesttrip_be.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "user_stats")
public class UserStats {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    private Integer reviewCount = 0;
    private Integer travelCount = 0;
}
