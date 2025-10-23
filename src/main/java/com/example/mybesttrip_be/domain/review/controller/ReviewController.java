package com.example.mybesttrip_be.domain.review.controller;

import com.example.mybesttrip_be.domain.review.dto.ReviewCreateRequest;
import com.example.mybesttrip_be.domain.review.dto.ReviewListResponse;
import com.example.mybesttrip_be.domain.review.dto.ReviewResponse;
import com.example.mybesttrip_be.domain.review.dto.ReviewUpdateRequest;
import com.example.mybesttrip_be.domain.review.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Review API", description = "리뷰 작성, 조회, 수정, 삭제 API")
public class ReviewController {
    private final ReviewService reviewService;


    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자 리뷰 조회", description = "특정 사용자가 작성한 모든 리뷰를 조회합니다.")
    public ReviewListResponse byUser(@PathVariable Long userId) {
        return reviewService.listByUser(userId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "리뷰 단건 조회", description = "리뷰 ID로 특정 리뷰 상세를 조회합니다.")
    public ReviewResponse getOne(@PathVariable Long id) {
        return reviewService.getOne(id);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "리뷰 수정", description = "리뷰 작성자 본인만 자신의 리뷰 내용을 수정할 수 있습니다.")
    public ReviewResponse updateOwnerOnly(@PathVariable Long id,
                                          @RequestParam Long userId,
                                          @RequestBody ReviewUpdateRequest req) {
        return reviewService.updateOwnerOnly(id, userId, req);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "리뷰 삭제", description = "리뷰 작성자 본인만 자신의 리뷰를 삭제할 수 있습니다.")
    public ResponseEntity<Void> deleteOwnerOnly(@PathVariable Long id, @RequestParam Long userId) {
        reviewService.deleteOwnerOnly(id, userId);
        return ResponseEntity.noContent().build(); // 204
    }


    // create with images (multipart)
    @PostMapping(value = "", consumes = "multipart/form-data")
    @Operation(summary = "리뷰 작성(이미지 포함)", description = "이미지는 최대 5개, png/jpg/jpeg, 파일당 10MB 제한.")
    public ReviewResponse createMultipart(
            @RequestPart("meta") @Valid ReviewCreateRequest meta,
            @RequestPart(value = "images", required = false) List<MultipartFile> images
    ) throws Exception {
        return reviewService.createWithImages(meta, images);
    }

    // create without images (JSON only)
    @PostMapping(value = "/json", consumes = "application/json")
    @Operation(summary = "리뷰 작성(JSON)", description = "이미지 없이 텍스트만으로 리뷰를 작성합니다.")
    public ReviewResponse createJson(@Valid @RequestBody ReviewCreateRequest req) {
        return reviewService.create(req);
    }

    // destination list with sort
    @GetMapping("/destination/{destinationId}")
    @Operation(summary = "여행지 리뷰 조회(정렬)", description = "sort=recent(기본) 또는 popular(좋아요 순)")
    public ReviewListResponse byDestination(@PathVariable Long destinationId,
                                            @RequestParam(defaultValue = "recent") String sort) {
        return reviewService.listByDestinationSorted(destinationId, sort);
    }

    // like toggle
    @PostMapping("/{id}/like")
    @Operation(summary = "리뷰 좋아요 토글", description = "userId로 좋아요/해제 토글")
    public void likeToggle(@PathVariable Long id, @RequestParam Long userId) {
        reviewService.toggleLike(id, userId);
    }

}
