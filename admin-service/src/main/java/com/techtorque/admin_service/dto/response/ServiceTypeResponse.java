package com.techtorque.admin_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for service type information
 * Used by: GET /admin/service-types, POST /admin/service-types, PUT /admin/service-types/{typeId}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceTypeResponse {
    private String id;
    private String name;
    private String description;
    private String category;
    private BigDecimal basePriceLKR;  // Changed from 'price' to match frontend
    private Integer estimatedDurationMinutes;  // Changed from 'durationMinutes' to match frontend
    private Boolean active;
    private Boolean requiresApproval;
    private Integer dailyCapacity;
    private String skillLevel;
    private String iconUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}