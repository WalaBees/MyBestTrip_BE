package com.example.mybesttrip_be.domain.inquiry.controller;

import com.example.mybesttrip_be.domain.inquiry.dto.*;
import com.example.mybesttrip_be.domain.inquiry.service.InquiryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/inquiries")
@RequiredArgsConstructor
@Tag(name = "Inquiry API", description = "문의 등록/수정/삭제 및 파일 첨부")
public class InquiryController {
    private final InquiryService service;

    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "문의 등록", description = "최대 5개 파일 첨부 가능 (png/jpg/jpeg/pdf, 최대 10MB).")
    public InquiryResponse create(
            @RequestPart("meta") @Valid InquiryCreateRequest meta,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws Exception {
        return service.create(meta, files);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "문의 수정", description = "카테고리/제목/내용/해결여부 수정.")
    public InquiryResponse update(@PathVariable Long id, @RequestBody InquiryUpdateRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "문의 삭제", description = "본인 문의 삭제. (관리자면 모든 문의 삭제 가능)")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping
    @Operation(summary = "문의 목록 조회", description = "admin=true이면 전체 조회, 아니면 userId의 문의만 조회.")
    public List<InquiryResponse> list(@RequestParam Long userId, @RequestParam(defaultValue = "false") boolean admin) {
        return service.listForUser(userId, admin);
    }
}
