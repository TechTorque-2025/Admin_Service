package com.techtorque.admin_service.controller;

import com.techtorque.admin_service.dto.ApiResponse;
import com.techtorque.admin_service.dto.DashboardDto;
import com.techtorque.admin_service.dto.SystemMetricsDto;
import com.techtorque.admin_service.service.AdminAnalyticsService;
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

  private final AdminAnalyticsService adminAnalyticsService;

  @Operation(summary = "Get aggregated data for the admin dashboard")
  @GetMapping("/dashboard")
  public ResponseEntity<ApiResponse> getDashboardData(
          @RequestParam(defaultValue = "monthly") String period) {

    DashboardDto dashboard = adminAnalyticsService.getDashboardData(period);
    return ResponseEntity.ok(ApiResponse.success("Dashboard data retrieved successfully", dashboard));
  }

  @Operation(summary = "Get high-level system metrics")
  @GetMapping("/metrics")
  public ResponseEntity<ApiResponse> getSystemMetrics() {

    SystemMetricsDto metrics = adminAnalyticsService.getSystemMetrics();
    return ResponseEntity.ok(ApiResponse.success("System metrics retrieved successfully", metrics));
  }
}