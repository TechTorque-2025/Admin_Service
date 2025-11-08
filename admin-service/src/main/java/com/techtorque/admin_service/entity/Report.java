package com.techtorque.admin_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity for storing generated reports
 * File Location: src/main/java/com/techtorque/admin_service/entity/Report.java
 */
@Entity
@Table(name = "reports", indexes = {
        @Index(name = "idx_report_type", columnList = "type"),
        @Index(name = "idx_report_status", columnList = "status"),
        @Index(name = "idx_generated_by", columnList = "generatedBy")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ReportType type;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false)
    private LocalDate fromDate;

    @Column(nullable = false)
    private LocalDate toDate;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ReportFormat format;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ReportStatus status = ReportStatus.PENDING;

    @Column(nullable = false)
    private String generatedBy; // User ID who generated the report

    @Column(length = 500)
    private String filePath;

    @Column(length = 500)
    private String downloadUrl;

    private Long fileSize;

    @Column(columnDefinition = "TEXT")
    private String dataJson; // Store report data as JSON

    @Column(length = 1000)
    private String errorMessage;

    @Builder.Default
    private Boolean isScheduled = false;

    private String scheduleId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime completedAt;
}
