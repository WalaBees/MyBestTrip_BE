package com.example.mybesttrip_be.domain.stats.repository;

import com.example.mybesttrip_be.domain.stats.entity.UserStats;
import com.example.mybesttrip_be.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserStatsRepository extends JpaRepository<UserStats, Long> {
    Optional<UserStats> findByUser(User user);
}