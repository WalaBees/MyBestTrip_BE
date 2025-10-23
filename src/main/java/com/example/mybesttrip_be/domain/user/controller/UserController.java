package com.example.mybesttrip_be.domain.user.controller;

import com.example.mybesttrip_be.domain.user.dto.UserCreateRequest;
import com.example.mybesttrip_be.domain.user.dto.UserProfileResponse;
import com.example.mybesttrip_be.domain.user.dto.UserUpdateRequest;
import com.example.mybesttrip_be.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "회원 정보 관리 API (회원가입, 조회, 수정)")
public class UserController {
    private final UserService userService;

    @PostMapping
    @Operation(summary = "회원 생성", description = "새로운 사용자를 생성합니다. (회원가입)")
    public ResponseEntity<?> create(@Valid @RequestBody UserCreateRequest req) {
        try {
            UserProfileResponse response = userService.create(req);
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            Map<String, String> error = new HashMap<>();
            if (e.getMessage().contains("nickname")) {
                error.put("error", "Nickname already exists");
            } else if (e.getMessage().contains("email")) {
                error.put("error", "Email already exists");
            } else {
                error.put("error", "Data integrity violation: " + e.getMessage());
            }
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create user: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "회원 프로필 조회", description = "사용자 ID를 이용해 프로필 정보를 조회합니다.")
    public UserProfileResponse get(@PathVariable Long id) {
        return userService.get(id);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "회원 프로필 수정", description = "회원 자신의 프로필 정보를 수정합니다. (닉네임, MBTI, 한줄소개 등)")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest req) {
        try {
            UserProfileResponse response = userService.update(id, req);
            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            Map<String, String> error = new HashMap<>();
            if (e.getMessage().contains("nickname")) {
                error.put("error", "Nickname already exists");
            } else {
                error.put("error", "Data integrity violation: " + e.getMessage());
            }
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update user: " + e.getMessage());
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
