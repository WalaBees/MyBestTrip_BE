package com.example.mybesttrip_be.domain.inquiry.service;

import com.example.mybesttrip_be.domain.common.FileStorage;
import com.example.mybesttrip_be.domain.inquiry.dto.*;
import com.example.mybesttrip_be.domain.inquiry.entity.Inquiry;
import com.example.mybesttrip_be.domain.inquiry.entity.InquiryFile;
import com.example.mybesttrip_be.domain.inquiry.repository.InquiryFileRepository;
import com.example.mybesttrip_be.domain.inquiry.repository.InquiryRepository;
import com.example.mybesttrip_be.domain.user.entity.User;
import com.example.mybesttrip_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Service @RequiredArgsConstructor
public class InquiryService {
    private final InquiryRepository repo;
    private final InquiryFileRepository fileRepo;
    private final UserRepository userRepo;
    private final FileStorage storage;

    @Transactional
    public InquiryResponse create(InquiryCreateRequest req, List<MultipartFile> files) throws Exception {
        User u = userRepo.findById(req.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Inquiry inq = Inquiry.builder()
                .user(u)
                .category(req.getCategory())
                .title(req.getTitle())
                .details(req.getDetails())
                .solved(false)
                .build();
        repo.save(inq);

        if (files != null) {
            if (files.size() > 5) throw new IllegalArgumentException("Max 5 files");
            for (MultipartFile f : files) {
                if (f.isEmpty()) continue;
                // size limit ~10MB
                if (f.getSize() > 10 * 1024 * 1024L) throw new IllegalArgumentException("File too large (10MB)");
                String ext = f.getOriginalFilename() == null ? "" : f.getOriginalFilename().toLowerCase();
                if (!(ext.endsWith(".png") || ext.endsWith(".jpg") || ext.endsWith(".jpeg") || ext.endsWith(".pdf")))
                    throw new IllegalArgumentException("Only png/jpg/jpeg/pdf allowed");
                String path = storage.save("inquiry/" + inq.getId(), f);
                fileRepo.save(InquiryFile.builder()
                        .inquiry(inq)
                        .path(path)
                        .originalName(f.getOriginalFilename())
                        .build());
            }
        }
        return toRes(inq);
    }

    @Transactional
    public InquiryResponse update(Long id, InquiryUpdateRequest req) {
        Inquiry inq = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Inquiry not found"));
        if (req.getCategory() != null) inq.setCategory(req.getCategory());
        if (req.getTitle() != null) inq.setTitle(req.getTitle());
        if (req.getDetails() != null) inq.setDetails(req.getDetails());
        if (req.getSolved() != null) inq.setSolved(req.getSolved());
        inq.setUpdatedAt(Instant.now());
        return toRes(inq);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<InquiryResponse> listForUser(Long userId, boolean admin) {
        if (admin) {
            return repo.findAllByOrderByIdDesc().stream().map(this::toRes).toList();
        }
        User u = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return repo.findAllByUserOrderByIdDesc(u).stream().map(this::toRes).toList();
    }

    private InquiryResponse toRes(Inquiry i) {
        return InquiryResponse.builder()
                .id(i.getId())
                .userId(i.getUser().getId())
                .category(i.getCategory())
                .title(i.getTitle())
                .details(i.getDetails())
                .solved(i.getSolved())
                .createdAt(i.getCreatedAt())
                .updatedAt(i.getUpdatedAt())
                .files(i.getFiles().stream().map(f -> InquiryResponse.FileItem.builder()
                        .id(f.getId()).path(f.getPath()).originalName(f.getOriginalName()).build()).toList())
                .build();
    }
}
