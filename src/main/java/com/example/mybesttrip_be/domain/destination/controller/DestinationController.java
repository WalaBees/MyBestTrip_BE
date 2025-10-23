package com.example.mybesttrip_be.domain.destination.controller;

import com.example.mybesttrip_be.domain.destination.entity.Destination;
import com.example.mybesttrip_be.domain.destination.repository.DestinationRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/destinations")
@RequiredArgsConstructor
@Tag(name = "Destination API", description = "여행지 정보 및 통계")
public class DestinationController {
    private final DestinationRepository repo;

    @GetMapping("/{id}/stats")
    @Operation(summary = "여행지 통계 조회", description = "총 리뷰 수와 평균 평점을 제공합니다.")
    public Destination stats(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Destination not found"));
    }
}
