package com.example.mybesttrip_be.domain.review.repository;

import com.example.mybesttrip_be.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByDestinationIdOrderByIdDesc(Long destinationId);
    List<Review> findByUserIdOrderByIdDesc(Long userId);
    List<Review> findByDestinationIdOrderByLikeCountDescIdDesc(Long destinationId);
}
