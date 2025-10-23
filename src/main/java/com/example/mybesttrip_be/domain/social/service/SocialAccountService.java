package com.example.mybesttrip_be.domain.social.service;

import com.example.mybesttrip_be.domain.social.entity.SocialAccount;
import com.example.mybesttrip_be.domain.social.repository.SocialAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class SocialAccountService {
    private final SocialAccountRepository repo;
    public SocialAccount save(SocialAccount s) { return repo.save(s); }
}
