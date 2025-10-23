package com.example.mybesttrip_be.domain.balance.repository;

import com.example.mybesttrip_be.domain.balance.entity.BalanceAnswer;
import com.example.mybesttrip_be.domain.balance.entity.BalanceQuestion;
import com.example.mybesttrip_be.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BalanceAnswerRepository extends JpaRepository<BalanceAnswer, Long> {
    long countByQuestionAndChoice(BalanceQuestion q, Integer choice);
    boolean existsByQuestionAndUser(BalanceQuestion q, User u);
}
