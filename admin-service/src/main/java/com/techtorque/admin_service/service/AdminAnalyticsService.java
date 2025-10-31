package com.techtorque.admin_service.service;

import com.techtorque.admin_service.dto.DashboardDto;
import com.techtorque.admin_service.dto.SystemMetricsDto;

public interface AdminAnalyticsService {
    DashboardDto getDashboardData(String period);
    SystemMetricsDto getSystemMetrics();
}
