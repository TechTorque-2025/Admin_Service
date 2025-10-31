package com.techtorque.admin_service.service.impl;

import com.techtorque.admin_service.dto.DashboardDto;
import com.techtorque.admin_service.dto.SystemMetricsDto;
import com.techtorque.admin_service.service.AdminAnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminAnalyticsServiceImpl implements AdminAnalyticsService {

    private final WebClient.Builder webClientBuilder;

    @Override
    public DashboardDto getDashboardData(String period) {
        log.info("Generating dashboard data for period: {}", period);

        // In a real implementation, this would make parallel calls to other microservices
        // For now, returning mock data with realistic structure

        Map<String, Integer> appointmentsByStatus = new HashMap<>();
        appointmentsByStatus.put("SCHEDULED", 15);
        appointmentsByStatus.put("CONFIRMED", 10);
        appointmentsByStatus.put("COMPLETED", 45);
        appointmentsByStatus.put("CANCELLED", 5);

        Map<String, Integer> projectsByStatus = new HashMap<>();
        projectsByStatus.put("REQUESTED", 8);
        projectsByStatus.put("QUOTED", 5);
        projectsByStatus.put("IN_PROGRESS", 12);
        projectsByStatus.put("COMPLETED", 30);

        return DashboardDto.builder()
                .totalCustomers(150)
                .totalEmployees(25)
                .activeProjects(25)
                .completedProjects(30)
                .pendingAppointments(25)
                .completedAppointments(45)
                .totalRevenue(BigDecimal.valueOf(125000.00))
                .pendingPayments(BigDecimal.valueOf(15000.00))
                .averageProjectCompletionTime(7.5)
                .totalVehicles(180)
                .appointmentsByStatus(appointmentsByStatus)
                .projectsByStatus(projectsByStatus)
                .period(period)
                .build();
    }

    @Override
    public SystemMetricsDto getSystemMetrics() {
        log.info("Generating system metrics");

        // In production, these would be real metrics from monitoring systems
        return SystemMetricsDto.builder()
                .totalUsers(175)
                .activeServices(70)
                .serviceCompletionRate(92.5)
                .customerSatisfactionRate(87.3)
                .totalTimeLogged(2500)
                .systemStatus("OPERATIONAL")
                .uptime(System.currentTimeMillis())
                .build();
    }
}
