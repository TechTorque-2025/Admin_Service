package com.techtorque.admin_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseDto {
    private String id;
    private String reportType;
    private String startDate;
    private String endDate;
    private Map<String, Object> data;
    private LocalDateTime generatedAt;
    private String generatedBy;
}
