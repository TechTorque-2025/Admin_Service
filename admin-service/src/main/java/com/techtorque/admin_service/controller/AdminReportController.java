package com.techtorque.admin_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/reports")
@Tag(name = "Admin: Reports", description = "Endpoints for generating and retrieving reports.")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
public class AdminReportController {

  @Operation(summary = "Generate a new on-demand report")
  @PostMapping("/generate")
  public ResponseEntity<?> generateReport(/* @RequestBody ReportRequestDto dto */) {
    // TODO: This service will call other microservices (Payment, Time Logging) to aggregate data.
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "List all previously generated reports")
  @GetMapping
  public ResponseEntity<?> listGeneratedReports() {
    // TODO: Retrieve a list of report metadata from a local database table.
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Get the data for a specific generated report")
  @GetMapping("/{reportId}")
  public ResponseEntity<?> getReportDetails(@PathVariable String reportId) {
    // TODO: Retrieve the data for a single report by its ID.
    return ResponseEntity.ok().build();
  }
}