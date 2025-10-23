package com.example.mybesttrip_be.domain.inquiry.dto;

import com.example.mybesttrip_be.domain.inquiry.entity.InquiryCategory;
import lombok.Data;

@Data
public class InquiryUpdateRequest {
    private InquiryCategory category;
    private String title;
    private String details;
    private Boolean solved; // admin can toggle
}
