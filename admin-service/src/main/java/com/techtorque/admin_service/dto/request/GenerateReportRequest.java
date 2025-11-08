// ========================================
// GenerateReportRequest.java
// ========================================

package com.techtorque.admin_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

/**
 * Request DTO for generating reports
 * Used by: POST /admin/reports/generate
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GenerateReportRequest {

    @NotBlank(message = "Report type is required")
    @Pattern(regexp = "SERVICE_PERFORMANCE|REVENUE|EMPLOYEE_PRODUCTIVITY|CUSTOMER_SATISFACTION|INVENTORY|APPOINTMENT_SUMMARY",
            message = "Invalid report type")
    private String type;

    @NotNull(message = "Start date is required")
    @PastOrPresent(message = "Start date cannot be in the future")
    private LocalDate fromDate;

    @NotNull(message = "End date is required")
    private LocalDate toDate;

    @NotBlank(message = "Format is required")
    @Pattern(regexp = "JSON|PDF|EXCEL|CSV", message = "Format must be JSON, PDF, EXCEL, or CSV")
    private String format;

    // Optional filters for more specific reports
    private String departmentId;
    private String employeeId;
    private String serviceCategory;
    private String customerId;

    /**
     * Custom validation to ensure fromDate is before toDate
     */
    @AssertTrue(message = "End date must be after start date")
    public boolean isValidDateRange() {
        if (fromDate == null || toDate == null) {
            return true; // Let @NotNull handle null validation
        }
        return !toDate.isBefore(fromDate);
    }
}