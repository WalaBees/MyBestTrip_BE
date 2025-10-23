package com.example.mybesttrip_be.domain.bookmark.repository;

import com.example.mybesttrip_be.domain.bookmark.entity.Bookmark;
import com.example.mybesttrip_be.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByUserAndDestinationId(User user, Long destinationId);
    List<Bookmark> findAllByUser(User user);
    void deleteByUserAndDestinationId(User user, Long destinationId);
}
