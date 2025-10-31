package com.techtorque.admin_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemMetricsDto {
    private Integer totalUsers;
    private Integer activeServices;
    private Double serviceCompletionRate;
    private Double customerSatisfactionRate;
    private Integer totalTimeLogged;
    private String systemStatus;
    private Long uptime;
}
