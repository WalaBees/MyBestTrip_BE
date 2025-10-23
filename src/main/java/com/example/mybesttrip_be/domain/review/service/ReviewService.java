package com.example.mybesttrip_be.domain.review.service;

import com.example.mybesttrip_be.domain.common.FileStorage;
import com.example.mybesttrip_be.domain.destination.entity.Destination;
import com.example.mybesttrip_be.domain.destination.repository.DestinationRepository;
import com.example.mybesttrip_be.domain.review.dto.ReviewCreateRequest;
import com.example.mybesttrip_be.domain.review.dto.ReviewListResponse;
import com.example.mybesttrip_be.domain.review.dto.ReviewResponse;
import com.example.mybesttrip_be.domain.review.dto.ReviewUpdateRequest;
import com.example.mybesttrip_be.domain.review.entity.Review;
import com.example.mybesttrip_be.domain.review.entity.ReviewImage;
import com.example.mybesttrip_be.domain.review.entity.ReviewLike;
import com.example.mybesttrip_be.domain.review.repository.ReviewImageRepository;
import com.example.mybesttrip_be.domain.review.repository.ReviewLikeRepository;
import com.example.mybesttrip_be.domain.review.repository.ReviewRepository;
import com.example.mybesttrip_be.domain.stats.entity.UserStats;
import com.example.mybesttrip_be.domain.stats.repository.UserStatsRepository;
import com.example.mybesttrip_be.domain.user.entity.User;
import com.example.mybesttrip_be.domain.user.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository imageRepo;
    private final ReviewLikeRepository reviewLikeRepository;
    private final UserRepository userRepository;
    private final DestinationRepository destRepo;
    private final UserStatsRepository statsRepo;
    private final FileStorage storage;

    /* ---------------------------
     * Create (JSON only – no images)
     * --------------------------- */
    @Transactional
    public ReviewResponse create(ReviewCreateRequest req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        // ensure destination exists
        Destination dest = destRepo.findById(req.getDestinationId())
                .orElseThrow(() -> new IllegalArgumentException("Destination not found"));

        Review review = Review.builder()
                .user(user)
                .destinationId(dest.getId())
                .title(req.getTitle())
                .body(req.getBody())
                .rating(req.getRating())
                .likeCount(0)
                .build();

        reviewRepository.save(review);

        // stats: reviewCount & travelCount += 1
        bumpUserStats(user, +1);

        // recalc destination aggregates
        recalcDestinationStats(dest.getId());

        return toDto(review);
    }

    /* ---------------------------
     * Create with images (multipart)
     * --------------------------- */
    @Transactional
    public ReviewResponse createWithImages(ReviewCreateRequest req, List<MultipartFile> images) throws Exception {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Destination dest = destRepo.findById(req.getDestinationId())
                .orElseThrow(() -> new IllegalArgumentException("Destination not found"));

        Review review = Review.builder()
                .user(user)
                .destinationId(dest.getId())
                .title(req.getTitle())
                .body(req.getBody())
                .rating(req.getRating())
                .likeCount(0)
                .build();
        reviewRepository.save(review);

        // images (max 5, 10MB, png/jpg/jpeg)
        if (images != null) {
            if (images.size() > 5) throw new IllegalArgumentException("Max 5 images");
            for (MultipartFile f : images) {
                if (f.isEmpty()) continue;
                if (f.getSize() > 10 * 1024 * 1024L) throw new IllegalArgumentException("Image too large (10MB)");
                String name = f.getOriginalFilename() == null ? "" : f.getOriginalFilename().toLowerCase();
                if (!(name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg"))) {
                    throw new IllegalArgumentException("Only png/jpg/jpeg images allowed");
                }
                String path = storage.save("review/" + review.getId(), f);
                imageRepo.save(ReviewImage.builder()
                        .review(review)
                        .path(path)
                        .originalName(f.getOriginalFilename())
                        .build());
            }
        }

        // stats: reviewCount & travelCount += 1
        bumpUserStats(user, +1);

        // recalc destination aggregates
        recalcDestinationStats(dest.getId());

        return toDto(review);
    }

    /* ---------------------------
     * List by destination with sorting
     * --------------------------- */
    @Transactional
    public ReviewListResponse listByDestinationSorted(Long destinationId, String sort) {
        List<Review> list = ("popular".equalsIgnoreCase(sort))
                ? reviewRepository.findByDestinationIdOrderByLikeCountDescIdDesc(destinationId)
                : reviewRepository.findByDestinationIdOrderByIdDesc(destinationId);
        return new ReviewListResponse(list.stream().map(this::toDto).toList());
    }

    /* ---------------------------
     * List by user
     * --------------------------- */
    @Transactional
    public ReviewListResponse listByUser(Long userId) {
        var items = reviewRepository.findByUserIdOrderByIdDesc(userId)
                .stream().map(this::toDto).toList();
        return new ReviewListResponse(items);
    }

    /* ---------------------------
     * Get one
     * --------------------------- */
    @Transactional
    public ReviewResponse getOne(Long id) {
        Review r = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        return toDto(r);
    }

    /* ---------------------------
     * Update (owner-only)
     * --------------------------- */
    @Transactional
    public ReviewResponse updateOwnerOnly(Long id, Long userId, ReviewUpdateRequest req) {
        Review r = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        if (!r.getUser().getId().equals(userId)) {
            throw new IllegalStateException("Not the owner of this review");
        }
        if (req.getTitle() != null) r.setTitle(req.getTitle());
        if (req.getBody() != null) r.setBody(req.getBody());
        if (req.getRating() != null) r.setRating(req.getRating());

        // destination averages may change if rating changed
        recalcDestinationStats(r.getDestinationId());

        return toDto(r);
    }

    /* ---------------------------
     * Like toggle
     * --------------------------- */
    @Transactional
    public void toggleLike(Long reviewId, Long userId) {
        Review r = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        var existing = reviewLikeRepository.findByReviewAndUser(r, u);
        if (existing.isPresent()) {
            reviewLikeRepository.delete(existing.get());
            r.setLikeCount(Math.max(0, r.getLikeCount() - 1));
        } else {
            reviewLikeRepository.save(ReviewLike.builder().review(r).user(u).build());
            r.setLikeCount(r.getLikeCount() + 1);
        }
    }

    /* ---------------------------
     * Delete (owner-only) + cleanup + stats
     * --------------------------- */
    @Transactional
    public void deleteOwnerOnly(Long id, Long userId) {
        Review r = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        if (!r.getUser().getId().equals(userId)) {
            throw new IllegalStateException("Not the owner of this review");
        }

        User user = r.getUser();
        Long destId = r.getDestinationId();

        // 1) delete likes and images
        reviewLikeRepository.deleteByReview(r);
        imageRepo.deleteByReview(r);

        // 2) delete the review
        reviewRepository.delete(r);

        // 3) stats down
        bumpUserStats(user, -1);

        // 4) recalc destination aggregates
        recalcDestinationStats(destId);
    }

    /* ---------------------------
     * Helpers
     * --------------------------- */
    private ReviewResponse toDto(Review r) {
        var imgs = imageRepo.findByReview(r).stream()
                .map(ReviewImage::getPath)
                .toList();

        // If your Review entity later has createdAt/updatedAt, map them here.
        return ReviewResponse.builder()
                .id(r.getId())
                .destinationId(r.getDestinationId())
                .userId(r.getUser().getId())
                .title(r.getTitle())
                .body(r.getBody())
                .rating(r.getRating())
                .likeCount(r.getLikeCount())
                .createdAt(null)
                .updatedAt(null)
                .images(imgs)
                .build();
    }

    private void bumpUserStats(User user, int delta) {
        UserStats stats = statsRepo.findByUser(user).orElseGet(() ->
                statsRepo.save(UserStats.builder().user(user).reviewCount(0).travelCount(0).build())
        );
        stats.setReviewCount(Math.max(0, stats.getReviewCount() + delta));
        stats.setTravelCount(Math.max(0, stats.getTravelCount() + delta));
    }

    private void recalcDestinationStats(Long destId) {
        Destination d = destRepo.findById(destId)
                .orElseThrow(() -> new IllegalArgumentException("Destination not found"));
        var reviews = reviewRepository.findByDestinationIdOrderByIdDesc(destId);
        int count = reviews.size();
        double avg = reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
        d.setReviewCount(count);
        d.setAverageRating(Math.round(avg * 10.0) / 10.0); // one decimal place
    }
}
