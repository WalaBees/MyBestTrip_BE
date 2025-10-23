package com.example.mybesttrip_be.domain.inquiry.repository;

import com.example.mybesttrip_be.domain.inquiry.entity.Inquiry;
import com.example.mybesttrip_be.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findAllByUserOrderByIdDesc(User user);
    List<Inquiry> findAllByOrderByIdDesc();
}
