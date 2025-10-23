package com.example.mybesttrip_be.domain.balance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BalanceQuestionRequest {
    @NotBlank(message = "Question is required")
    @Size(max = 200, message = "Question must be less than 200 characters")
    private String question;
    
    @NotBlank(message = "Option1 is required")
    @Size(max = 100, message = "Option1 must be less than 100 characters")
    private String option1;
    
    @NotBlank(message = "Option2 is required")
    @Size(max = 100, message = "Option2 must be less than 100 characters")
    private String option2;
}
