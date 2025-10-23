package com.example.mybesttrip_be.domain.review.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ReviewUpdateRequest {
    private String title;     // optional
    private String body;      // optional
    @Min(1) @Max(5)
    private Integer rating;   // optional (1..5)
}
