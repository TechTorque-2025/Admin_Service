package com.techtorque.admin_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDto {
    private Integer totalCustomers;
    private Integer totalEmployees;
    private Integer activeProjects;
    private Integer completedProjects;
    private Integer pendingAppointments;
    private Integer completedAppointments;
    private BigDecimal totalRevenue;
    private BigDecimal pendingPayments;
    private Double averageProjectCompletionTime;
    private Integer totalVehicles;
    private Map<String, Integer> appointmentsByStatus;
    private Map<String, Integer> projectsByStatus;
    private String period;
}
