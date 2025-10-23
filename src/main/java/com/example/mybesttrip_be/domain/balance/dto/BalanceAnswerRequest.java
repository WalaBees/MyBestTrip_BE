package com.example.mybesttrip_be.domain.balance.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BalanceAnswerRequest {
    @NotNull private Long userId;
    @NotNull private Long questionId;
    @NotNull @Min(1) @Max(2)
    private Integer choice;
}
