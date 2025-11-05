package com.techtorque.admin_service.service.impl;

import com.techtorque.admin_service.dto.response.DashboardAnalyticsResponse;
import com.techtorque.admin_service.dto.response.SystemMetricsResponse;
import com.techtorque.admin_service.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of AnalyticsService
 * Aggregates data from multiple microservices for analytics and reporting
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService {

    @Qualifier("paymentServiceWebClient")
    private final WebClient paymentServiceWebClient;

    @Qualifier("appointmentServiceWebClient")
    private final WebClient appointmentServiceWebClient;

    @Qualifier("projectServiceWebClient")
    private final WebClient projectServiceWebClient;

    @Qualifier("timeLoggingServiceWebClient")
    private final WebClient timeLoggingServiceWebClient;

    @Override
    public DashboardAnalyticsResponse getDashboardAnalytics(String period) {
        log.info("Fetching dashboard analytics for period: {}", period);

        try {
            // In a real implementation, you would make parallel calls to various services
            // For now, returning mock data structure
            
            DashboardAnalyticsResponse.KpiData kpis = DashboardAnalyticsResponse.KpiData.builder()
                    .totalActiveServices(12)
                    .completedServicesToday(5)
                    .pendingAppointments(8)
                    .revenueToday(BigDecimal.valueOf(15000.00))
                    .revenueThisMonth(BigDecimal.valueOf(450000.00))
                    .revenueThisYear(BigDecimal.valueOf(5400000.00))
                    .completionRate(0.92)
                    .customerSatisfactionScore(4.5)
                    .activeEmployees(15)
                    .activeCustomers(145)
                    .build();

            DashboardAnalyticsResponse.RevenueData revenue = DashboardAnalyticsResponse.RevenueData.builder()
                    .total(BigDecimal.valueOf(5400000.00))
                    .pending(BigDecimal.valueOf(120000.00))
                    .received(BigDecimal.valueOf(5280000.00))
                    .revenueByMonth(getMonthlyRevenue())
                    .revenueByCategory(getRevenueByCategory())
                    .build();

            DashboardAnalyticsResponse.ServiceStats serviceStats = DashboardAnalyticsResponse.ServiceStats.builder()
                    .total(156)
                    .inProgress(12)
                    .completed(138)
                    .cancelled(6)
                    .servicesByType(getServicesByType())
                    .avgCompletionTimeHours(6.5)
                    .build();

            DashboardAnalyticsResponse.AppointmentStats appointmentStats = DashboardAnalyticsResponse.AppointmentStats.builder()
                    .totalBooked(178)
                    .todayAppointments(5)
                    .weekAppointments(34)
                    .confirmed(156)
                    .pending(8)
                    .cancelled(14)
                    .utilizationRate(0.85)
                    .build();

            DashboardAnalyticsResponse.EmployeeStats employeeStats = DashboardAnalyticsResponse.EmployeeStats.builder()
                    .totalEmployees(15)
                    .activeToday(12)
                    .topPerformers(getTopPerformers())
                    .avgHoursPerEmployee(7.5)
                    .build();

            return DashboardAnalyticsResponse.builder()
                    .kpis(kpis)
                    .revenue(revenue)
                    .serviceStats(serviceStats)
                    .appointmentStats(appointmentStats)
                    .employeeStats(employeeStats)
                    .build();

        } catch (Exception e) {
            log.error("Error fetching dashboard analytics", e);
            throw new RuntimeException("Failed to fetch dashboard analytics: " + e.getMessage());
        }
    }

    @Override
    public SystemMetricsResponse getSystemMetrics() {
        log.info("Fetching system metrics");

        try {
            // In a real implementation, aggregate from multiple services
            return SystemMetricsResponse.builder()
                    .activeServices(12)
                    .totalServices(156)
                    .completionRate(0.92)
                    .avgServiceTimeHours(6.5)
                    .totalAppointments(178)
                    .pendingAppointments(8)
                    .confirmedAppointments(156)
                    .totalUsers(160)
                    .activeCustomers(145)
                    .activeEmployees(15)
                    .totalVehicles(290)
                    .systemUptime(99.9)
                    .averageResponseTime(250.0)
                    .lastUpdated(java.time.LocalDateTime.now())
                    .build();

        } catch (Exception e) {
            log.error("Error fetching system metrics", e);
            throw new RuntimeException("Failed to fetch system metrics: " + e.getMessage());
        }
    }

    // Helper methods for mock data
    private Map<String, BigDecimal> getMonthlyRevenue() {
        Map<String, BigDecimal> revenue = new HashMap<>();
        revenue.put("January", BigDecimal.valueOf(420000.00));
        revenue.put("February", BigDecimal.valueOf(450000.00));
        revenue.put("March", BigDecimal.valueOf(480000.00));
        revenue.put("April", BigDecimal.valueOf(510000.00));
        revenue.put("May", BigDecimal.valueOf(490000.00));
        revenue.put("June", BigDecimal.valueOf(520000.00));
        return revenue;
    }

    private Map<String, BigDecimal> getRevenueByCategory() {
        Map<String, BigDecimal> revenue = new HashMap<>();
        revenue.put("MAINTENANCE", BigDecimal.valueOf(2200000.00));
        revenue.put("REPAIR", BigDecimal.valueOf(1800000.00));
        revenue.put("MODIFICATION", BigDecimal.valueOf(1000000.00));
        revenue.put("INSPECTION", BigDecimal.valueOf(400000.00));
        return revenue;
    }

    private Map<String, Integer> getServicesByType() {
        Map<String, Integer> services = new HashMap<>();
        services.put("Oil Change", 45);
        services.put("Brake Service", 32);
        services.put("Tire Rotation", 28);
        services.put("Engine Diagnostic", 22);
        services.put("AC Service", 18);
        services.put("Other", 11);
        return services;
    }

    private Map<String, Integer> getTopPerformers() {
        Map<String, Integer> performers = new HashMap<>();
        performers.put("John Smith", 45);
        performers.put("Sarah Johnson", 42);
        performers.put("Mike Williams", 38);
        performers.put("Emily Brown", 35);
        performers.put("David Jones", 32);
        return performers;
    }
}
