// ========================================
// ReportResponse.java
// ========================================

package com.techtorque.admin_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for report information
 * Used by: POST /admin/reports/generate, GET /admin/reports, GET /admin/reports/{reportId}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponse {
    private String reportId;
    private String type;
    private String title;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String format;
    private String status; // PENDING, GENERATING, COMPLETED, FAILED
    private String generatedBy;
    private String downloadUrl;
    private Long fileSize;
    private Object data; // Report data for JSON format
    private String errorMessage;
    private Boolean isScheduled;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}