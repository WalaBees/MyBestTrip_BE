package com.example.mybesttrip_be.domain.review.repository;

import com.example.mybesttrip_be.domain.review.entity.Review;
import com.example.mybesttrip_be.domain.review.entity.ReviewLike;
import com.example.mybesttrip_be.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    Optional<ReviewLike> findByReviewAndUser(Review review, User user);
    void deleteByReview(Review review);
}
