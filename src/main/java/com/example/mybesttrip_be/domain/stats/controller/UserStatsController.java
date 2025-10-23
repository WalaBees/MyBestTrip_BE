package com.example.mybesttrip_be.domain.stats.controller;

import com.example.mybesttrip_be.domain.stats.entity.UserStats;
import com.example.mybesttrip_be.domain.stats.repository.UserStatsRepository;
import com.example.mybesttrip_be.domain.user.entity.User;
import com.example.mybesttrip_be.domain.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Stats API", description = "사용자 리뷰 수 / 여행 수 제공")
public class UserStatsController {
    private final UserRepository userRepo;
    private final UserStatsRepository statsRepo;

    @GetMapping("/{userId}/stats")
    @Operation(summary = "사용자 통계 조회", description = "reviewCount = 작성 리뷰 수, travelCount = reviewCount (정책)")
    public UserStats get(@PathVariable Long userId) {
        User u = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return statsRepo.findByUser(u).orElseGet(() -> statsRepo.save(
                UserStats.builder().user(u).reviewCount(0).travelCount(0).build()
        ));
    }
}
