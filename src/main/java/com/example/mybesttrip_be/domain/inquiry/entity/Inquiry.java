package com.example.mybesttrip_be.domain.inquiry.entity;

import com.example.mybesttrip_be.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Inquiry {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    private User user;

    @Enumerated(EnumType.STRING)
    private InquiryCategory category;

    @Column(nullable=false, length=120)
    private String title;

    @Lob
    private String details;

    @Builder.Default
    private Boolean solved = false;

    @Builder.Default
    private Instant createdAt = Instant.now();
    @Builder.Default
    private Instant updatedAt = Instant.now();

    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InquiryFile> files = new ArrayList<>();
}
