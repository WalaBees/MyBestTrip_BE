package com.example.mybesttrip_be.domain.inquiry.dto;

import com.example.mybesttrip_be.domain.inquiry.entity.InquiryCategory;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data @Builder
public class InquiryResponse {
    private Long id;
    private Long userId;
    private InquiryCategory category;
    private String title;
    private String details;
    private Boolean solved;
    private Instant createdAt;
    private Instant updatedAt;
    private List<FileItem> files;

    @Data @Builder
    public static class FileItem {
        private Long id;
        private String path;
        private String originalName;
    }
}
