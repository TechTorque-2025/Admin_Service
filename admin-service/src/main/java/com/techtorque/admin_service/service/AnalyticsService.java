package com.techtorque.admin_service.service;

import com.techtorque.admin_service.dto.response.DashboardAnalyticsResponse;
import com.techtorque.admin_service.dto.response.SystemMetricsResponse;

/**
 * Service interface for analytics and reporting
 */
public interface AnalyticsService {
    
    /**
     * Get dashboard analytics for specified period
     * @param period Period in days (7d, 30d, 90d)
     * @return Dashboard analytics data
     */
    DashboardAnalyticsResponse getDashboardAnalytics(String period);
    
    /**
     * Get system metrics
     * @return System metrics data
     */
    SystemMetricsResponse getSystemMetrics();
}
