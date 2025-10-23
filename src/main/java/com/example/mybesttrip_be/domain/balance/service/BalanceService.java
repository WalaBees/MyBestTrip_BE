package com.example.mybesttrip_be.domain.balance.service;

import com.example.mybesttrip_be.domain.balance.dto.BalanceAnswerRequest;
import com.example.mybesttrip_be.domain.balance.dto.BalanceQuestionRequest;
import com.example.mybesttrip_be.domain.balance.dto.BalanceResultResponse;
import com.example.mybesttrip_be.domain.balance.entity.BalanceAnswer;
import com.example.mybesttrip_be.domain.balance.entity.BalanceQuestion;
import com.example.mybesttrip_be.domain.balance.repository.BalanceAnswerRepository;
import com.example.mybesttrip_be.domain.balance.repository.BalanceQuestionRepository;
import com.example.mybesttrip_be.domain.user.entity.User;
import com.example.mybesttrip_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class BalanceService {
    private final BalanceQuestionRepository qRepo;
    private final BalanceAnswerRepository aRepo;
    private final UserRepository userRepo;

    @Transactional
    public BalanceQuestion createQuestion(BalanceQuestionRequest req) {
        return qRepo.save(BalanceQuestion.builder()
                .question(req.getQuestion())
                .option1(req.getOption1())
                .option2(req.getOption2())
                .isActive(true)
                .build());
    }

    @Transactional
    public void answer(BalanceAnswerRequest req) {
        BalanceQuestion q = qRepo.findById(req.getQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));
        User u = userRepo.findById(req.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (aRepo.existsByQuestionAndUser(q, u)) {
            throw new IllegalStateException("Already answered");
        }
        aRepo.save(BalanceAnswer.builder().question(q).user(u).choice(req.getChoice()).build());
    }

    @Transactional(readOnly = true)
    public BalanceResultResponse result(Long questionId) {
        BalanceQuestion q = qRepo.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found"));
        long c1 = aRepo.countByQuestionAndChoice(q, 1);
        long c2 = aRepo.countByQuestionAndChoice(q, 2);
        return new BalanceResultResponse(q.getId(), c1, c2);
    }
}