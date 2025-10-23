package com.example.mybesttrip_be.domain.inquiry.dto;

import com.example.mybesttrip_be.domain.inquiry.entity.InquiryCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InquiryCreateRequest {
    @NotNull private Long userId;
    @NotNull private InquiryCategory category;
    @NotBlank private String title;
    private String details;
    // files via multipart (max 5)
}
