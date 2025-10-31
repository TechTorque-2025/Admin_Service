// ========================================
// ReportSchedule.java
// ========================================

package com.techtorque.admin_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity for scheduled recurring reports
 * File Location: src/main/java/com/techtorque/admin_service/entity/ReportSchedule.java
 */
@Entity
@Table(name = "report_schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ReportSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ScheduleFrequency frequency;

    @ElementCollection
    @CollectionTable(name = "report_schedule_recipients",
        joinColumns = @JoinColumn(name = "schedule_id"))
    @Column(name = "email", nullable = false)
    private List<String> recipients;

    private Integer dayOfSchedule; // For WEEKLY (1-7) or MONTHLY (1-31)

    private Integer hourOfDay; // 0-23

    @Column(nullable = false)
    private String createdBy;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    private LocalDateTime nextRun;

    private LocalDateTime lastRun;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}

