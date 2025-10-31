package com.techtorque.admin_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequestDto {

    @NotBlank(message = "Report type is required")
    private String reportType; // "APPOINTMENTS", "PROJECTS", "REVENUE", "TIME_LOGS"

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    private String employeeId;
    private String customerId;
}
