package com.example.mybesttrip_be.domain.stats.service;

import com.example.mybesttrip_be.domain.stats.entity.UserStats;
import com.example.mybesttrip_be.domain.stats.repository.UserStatsRepository;
import com.example.mybesttrip_be.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class UserStatsService {
    private final UserStatsRepository repo;

    @Transactional
    public UserStats initIfAbsent(User user) {
        return repo.findByUser(user).orElseGet(() -> repo.save(UserStats.builder().user(user).build()));
    }
}
