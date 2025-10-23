package com.example.mybesttrip_be.domain.balance.controller;

import com.example.mybesttrip_be.domain.balance.dto.BalanceAnswerRequest;
import com.example.mybesttrip_be.domain.balance.dto.BalanceQuestionRequest;
import com.example.mybesttrip_be.domain.balance.dto.BalanceResultResponse;
import com.example.mybesttrip_be.domain.balance.entity.BalanceQuestion;
import com.example.mybesttrip_be.domain.balance.repository.BalanceQuestionRepository;
import com.example.mybesttrip_be.domain.balance.service.BalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/balance")
@RequiredArgsConstructor
@Tag(name = "Balance Game API", description = "밸런스 게임(질문/답변/결과) 관리 API")
public class BalanceController {
    private final BalanceService balanceService;
    private final BalanceQuestionRepository qRepo;

    @PostMapping("/questions")
    @Operation(summary = "질문 등록", description = "새로운 밸런스 게임 질문을 등록합니다.")
    public ResponseEntity<?> create(@Valid @RequestBody BalanceQuestionRequest req) {
        try {
            BalanceQuestion question = balanceService.createQuestion(req);
            return ResponseEntity.ok(question);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create question: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/questions")
    @Operation(summary = "질문 전체 조회", description = "등록된 모든 밸런스 게임 질문을 조회합니다.")
    public List<BalanceQuestion> list() {
        return qRepo.findAll();
    }

    @PostMapping("/answers")
    @Operation(summary = "답변 제출", description = "사용자가 밸런스 게임 질문에 대해 선택한 답변(1 또는 2)을 저장합니다. 한 사용자당 한 번만 답변 가능.")
    public ResponseEntity<?> answer(@Valid @RequestBody BalanceAnswerRequest req) {
        try {
            balanceService.answer(req);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (IllegalStateException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to submit answer: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/results/{questionId}")
    @Operation(summary = "결과 조회", description = "특정 질문에 대한 전체 투표 결과(각 선택지별 카운트)를 조회합니다.")
    public ResponseEntity<?> result(@PathVariable Long questionId) {
        try {
            BalanceResultResponse result = balanceService.result(questionId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to get results: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }
}
