package com.techtorque.admin_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Response DTO for analytics dashboard
 * Used by: GET /admin/analytics/dashboard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardAnalyticsResponse {

    private KpiData kpis;
    private RevenueData revenue;
    private ServiceStats serviceStats;
    private AppointmentStats appointmentStats;
    private EmployeeStats employeeStats;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KpiData {
        private Integer totalActiveServices;
        private Integer completedServicesToday;
        private Integer pendingAppointments;
        private BigDecimal revenueToday;
        private BigDecimal revenueThisMonth;
        private BigDecimal revenueThisYear;
        private Double completionRate;
        private Double customerSatisfactionScore;
        private Integer activeEmployees;
        private Integer activeCustomers;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RevenueData {
        private BigDecimal total;
        private BigDecimal pending;
        private BigDecimal received;
        private Map<String, BigDecimal> revenueByMonth; // Month -> Amount
        private Map<String, BigDecimal> revenueByCategory; // Category -> Amount
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServiceStats {
        private Integer total;
        private Integer inProgress;
        private Integer completed;
        private Integer cancelled;
        private Map<String, Integer> servicesByType; // Type -> Count
        private Double avgCompletionTimeHours;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppointmentStats {
        private Integer totalBooked;
        private Integer todayAppointments;
        private Integer weekAppointments;
        private Integer confirmed;
        private Integer pending;
        private Integer cancelled;
        private Double utilizationRate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeStats {
        private Integer totalEmployees;
        private Integer activeToday;
        private Map<String, Integer> topPerformers; // Employee -> Services Completed
        private Double avgHoursPerEmployee;
    }
}
