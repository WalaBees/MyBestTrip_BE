package com.example.mybesttrip_be.domain.user.service;

import com.example.mybesttrip_be.domain.user.dto.UserCreateRequest;
import com.example.mybesttrip_be.domain.user.dto.UserProfileResponse;
import com.example.mybesttrip_be.domain.user.dto.UserUpdateRequest;
import com.example.mybesttrip_be.domain.user.entity.User;
import com.example.mybesttrip_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public UserProfileResponse create(UserCreateRequest req) {
        User user = User.builder()
                .email(req.getEmail())
                .passwordHash(req.getPassword()) // NOTE: hash later
                .nickname(req.getNickname())
                .mbti(req.getMbti())
                .tosAgreed(req.getTosAgreed())
                .profileImageUrl(req.getProfileImageUrl())
                .oneLiner(req.getOneLiner())
                .locationCode(req.getLocationCode())
                .build();
        userRepository.save(user);
        return toProfile(user);
    }

    @Transactional(readOnly = true)
    public UserProfileResponse get(Long id) {
        return userRepository.findById(id).map(this::toProfile)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional
    public UserProfileResponse update(Long id, UserUpdateRequest req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (req.getNickname() != null) user.setNickname(req.getNickname());
        if (req.getMbti() != null) user.setMbti(req.getMbti());
        if (req.getProfileImageUrl() != null) user.setProfileImageUrl(req.getProfileImageUrl());
        if (req.getOneLiner() != null) user.setOneLiner(req.getOneLiner());
        if (req.getLocationCode() != null) user.setLocationCode(req.getLocationCode());
        return toProfile(user);
    }

    private UserProfileResponse toProfile(User u) {
        return UserProfileResponse.builder()
                .id(u.getId())
                .nickname(u.getNickname())
                .mbti(u.getMbti())
                .profileImageUrl(u.getProfileImageUrl())
                .oneLiner(u.getOneLiner())
                .locationCode(u.getLocationCode())
                .build();
    }
}
