// ========================================
// AnalyticsDashboardResponse.java
// ========================================

package com.techtorque.admin_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Response DTO for analytics dashboard
 * Used by: GET /admin/analytics/dashboard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsDashboardResponse {

    // Key Performance Indicators
    private KPIs kpis;

    // Chart data
    private Charts charts;

    // Trends and comparisons
    private Trends trends;

    private String period; // 7d, 30d, 90d
    private LocalDateTime generatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KPIs {
        private Integer activeServices;
        private Integer completedServices;
        private Integer pendingAppointments;
        private Integer totalCustomers;
        private Integer activeEmployees;
        private BigDecimal totalRevenue;
        private BigDecimal averageServiceCost;
        private Double completionRate; // percentage
        private Double customerSatisfaction; // percentage
        private Double avgServiceTimeHours;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Charts {
        private List<ChartData> revenueOverTime;
        private List<ChartData> servicesCompleted;
        private List<CategoryData> servicesByCategory;
        private List<EmployeeData> topEmployees;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Trends {
        private Double revenueGrowth; // percentage change
        private Double serviceGrowth;
        private Double customerGrowth;
        private String trendDirection; // UP, DOWN, STABLE
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChartData {
        private String date;
        private BigDecimal value;
        private Integer count;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryData {
        private String category;
        private Integer count;
        private BigDecimal revenue;
        private Double percentage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeData {
        private String employeeId;
        private String employeeName;
        private Integer servicesCompleted;
        private Double hoursWorked;
        private Double rating;
    }
}