// ========================================
// SystemMetricsResponse.java
// ========================================

package com.techtorque.admin_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Response DTO for system metrics
 * Used by: GET /admin/analytics/metrics
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemMetricsResponse {
    private Integer activeServices;
    private Integer totalServices;
    private Double completionRate; // percentage
    private Double avgServiceTimeHours;
    private Integer totalAppointments;
    private Integer pendingAppointments;
    private Integer confirmedAppointments;
    private Integer totalUsers;
    private Integer activeCustomers;
    private Integer activeEmployees;
    private Integer totalVehicles;
    private Double systemUptime; // percentage
    private Double averageResponseTime; // milliseconds
    private LocalDateTime lastUpdated;
}