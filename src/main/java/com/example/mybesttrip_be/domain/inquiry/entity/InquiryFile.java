package com.example.mybesttrip_be.domain.inquiry.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InquiryFile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    private Inquiry inquiry;

    @Column(nullable=false, length=500)
    private String path;   // local path now, later S3 URL

    @Column(length=120)
    private String originalName;
}
