// ========================================
// ScheduleReportRequest.java
// ========================================

package com.techtorque.admin_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Request DTO for scheduling recurring reports
 * Used by: POST /admin/reports/schedule
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleReportRequest {

    @NotBlank(message = "Report type is required")
    @Pattern(regexp = "SERVICE_PERFORMANCE|REVENUE|EMPLOYEE_PRODUCTIVITY|CUSTOMER_SATISFACTION|INVENTORY",
            message = "Invalid report type")
    private String type;

    @NotBlank(message = "Frequency is required")
    @Pattern(regexp = "DAILY|WEEKLY|MONTHLY", message = "Frequency must be DAILY, WEEKLY, or MONTHLY")
    private String frequency;

    @NotEmpty(message = "At least one recipient email is required")
    @Size(max = 10, message = "Maximum 10 recipients allowed")
    private List<@Email(message = "Invalid email address") String> recipients;

    // Optional: specific day for WEEKLY (1-7, Monday-Sunday) or MONTHLY (1-31)
    @Min(value = 1, message = "Day must be between 1 and 31")
    @Max(value = 31, message = "Day must be between 1 and 31")
    private Integer dayOfSchedule;

    // Optional: specific hour for daily reports (0-23)
    @Min(value = 0, message = "Hour must be between 0 and 23")
    @Max(value = 23, message = "Hour must be between 0 and 23")
    private Integer hourOfDay;
}