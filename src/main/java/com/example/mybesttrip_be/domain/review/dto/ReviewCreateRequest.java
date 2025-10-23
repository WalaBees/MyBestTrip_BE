package com.example.mybesttrip_be.domain.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewCreateRequest {
    @NotNull private Long userId;
    @NotNull private Long destinationId;
    @NotBlank private String title;
    private String body;
    @NotNull @Min(1) @Max(5)
    private Integer rating;
}
