package com.example.mybesttrip_be.domain.bookmark.controller;

import com.example.mybesttrip_be.domain.bookmark.entity.Bookmark;
import com.example.mybesttrip_be.domain.bookmark.repository.BookmarkRepository;
import com.example.mybesttrip_be.domain.user.entity.User;
import com.example.mybesttrip_be.domain.user.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
@Tag(name = "Bookmark API", description = "사용자 북마크(즐겨찾기) 관리 API")
public class BookmarkController {
    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;

    @PostMapping("/{userId}/{destinationId}/toggle")
    @Operation(summary = "북마크 토글", description = "해당 여행지가 이미 북마크되어 있으면 해제, 없으면 북마크로 추가합니다.")
    public boolean toggle(@PathVariable Long userId, @PathVariable Long destinationId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        var existing = bookmarkRepository.findByUserAndDestinationId(user, destinationId);
        if (existing.isPresent()) {
            bookmarkRepository.delete(existing.get());
            return false; // now unbookmarked
        }
        bookmarkRepository.save(Bookmark.builder().user(user).destinationId(destinationId).build());
        return true; // now bookmarked
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자 북마크 전체 조회", description = "특정 사용자가 등록한 모든 북마크를 조회합니다.")
    public Map<String, Object> listByUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        var items = bookmarkRepository.findAllByUser(user).stream()
                .map(b -> Map.of(
                        "id", b.getId(),
                        "destinationId", b.getDestinationId()
                ))
                .toList();
        return Map.of("items", items);
    }

    @DeleteMapping("/{userId}/{destinationId}")
    @Operation(summary = "북마크 삭제", description = "사용자가 특정 여행지에 대해 등록한 북마크를 삭제합니다.")
    public void delete(@PathVariable Long userId, @PathVariable Long destinationId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        bookmarkRepository.deleteByUserAndDestinationId(user, destinationId);
    }
}
