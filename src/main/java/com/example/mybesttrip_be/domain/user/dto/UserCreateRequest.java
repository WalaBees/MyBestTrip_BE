package com.example.mybesttrip_be.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateRequest {
    private String email;            // optional for pure social
    private String password;         // optional for social
    
    @NotBlank(message = "Nickname is required")
    @Size(max = 40, message = "Nickname must be less than 40 characters")
    private String nickname;
    
    @Size(max = 4, message = "MBTI must be 4 characters or less")
    private String mbti;
    
    private Boolean tosAgreed;
    private String profileImageUrl;
    private String oneLiner;
    private String locationCode;
}
