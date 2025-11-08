package com.techtorque.admin_service.controller;

import com.techtorque.admin_service.dto.response.ApiResponse;
import com.techtorque.admin_service.dto.response.DashboardAnalyticsResponse;
import com.techtorque.admin_service.dto.response.SystemMetricsResponse;
import com.techtorque.admin_service.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/analytics")
@Tag(name = "Admin: Analytics", description = "Endpoints for dashboard data and system metrics.")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminAnalyticsController {

  private final AnalyticsService analyticsService;

  @Operation(summary = "Get aggregated data for the admin dashboard")
  @GetMapping("/dashboard")
  public ResponseEntity<ApiResponse<DashboardAnalyticsResponse>> getDashboardData(
          @RequestParam(defaultValue = "30d") String period) {
    DashboardAnalyticsResponse analytics = analyticsService.getDashboardAnalytics(period);
    return ResponseEntity.ok(ApiResponse.success("Dashboard analytics retrieved successfully", analytics));
  }

  @Operation(summary = "Get high-level system metrics")
  @GetMapping("/metrics")
  public ResponseEntity<ApiResponse<SystemMetricsResponse>> getSystemMetrics() {
    SystemMetricsResponse metrics = analyticsService.getSystemMetrics();
    return ResponseEntity.ok(ApiResponse.success("System metrics retrieved successfully", metrics));
  }
}