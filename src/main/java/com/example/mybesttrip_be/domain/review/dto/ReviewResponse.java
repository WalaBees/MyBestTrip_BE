package com.example.mybesttrip_be.domain.review.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data @Builder
public class ReviewResponse {
    private Long id;
    private Long destinationId;
    private Long userId;
    private String title;
    private String body;
    private Integer rating;
    private Integer likeCount;
    private Instant createdAt;
    private Instant updatedAt;
    private List<String> images;
}
