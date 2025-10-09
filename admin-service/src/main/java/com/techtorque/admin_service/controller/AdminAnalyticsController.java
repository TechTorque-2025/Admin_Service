package com.techtorque.admin_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/analytics")
@Tag(name = "Admin: Analytics", description = "Endpoints for dashboard data and system metrics.")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAnalyticsController {

  @Operation(summary = "Get aggregated data for the admin dashboard")
  @GetMapping("/dashboard")
  public ResponseEntity<?> getDashboardData(@RequestParam String period) {
    // TODO: This is a major aggregator endpoint. It will make parallel calls to multiple
    // services (Appointment, Project, Payment) to gather KPIs, charts, and trend data.
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Get high-level system metrics")
  @GetMapping("/metrics")
  public ResponseEntity<?> getSystemMetrics() {
    // TODO: Make calls to relevant services to get metrics like active services, completion rate, etc.
    return ResponseEntity.ok().build();
  }
}