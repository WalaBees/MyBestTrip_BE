package com.example.mybesttrip_be.domain.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @Size(max = 40, message = "Nickname must be less than 40 characters")
    private String nickname;
    
    @Size(max = 4, message = "MBTI must be 4 characters or less")
    private String mbti;
    
    private String profileImageUrl;
    private String oneLiner;
    private String locationCode;
}
