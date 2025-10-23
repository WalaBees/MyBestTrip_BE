package com.example.mybesttrip_be.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class UserProfileResponse {
    private Long id;
    private String nickname;
    private String mbti;
    private String profileImageUrl;
    private String oneLiner;
    private String locationCode;
}
