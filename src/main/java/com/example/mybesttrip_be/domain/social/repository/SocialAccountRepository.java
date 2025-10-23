package com.example.mybesttrip_be.domain.social.repository;

import com.example.mybesttrip_be.domain.social.entity.SocialAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {}
