package com.techtorque.admin_service.controller;

import com.techtorque.admin_service.dto.ApiResponse;
import com.techtorque.admin_service.dto.ReportRequestDto;
import com.techtorque.admin_service.dto.ReportResponseDto;
import com.techtorque.admin_service.service.AdminReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/reports")
@Tag(name = "Admin: Reports", description = "Endpoints for generating and retrieving reports.")
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
@RequiredArgsConstructor
public class AdminReportController {

  private final AdminReportService adminReportService;

  @Operation(summary = "Generate a new on-demand report")
  @PostMapping("/generate")
  public ResponseEntity<ApiResponse> generateReport(
          @Valid @RequestBody ReportRequestDto dto,
          @RequestHeader("X-User-Subject") String adminId) {

    ReportResponseDto report = adminReportService.generateReport(dto, adminId);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success("Report generated successfully", report));
  }

  @Operation(summary = "List all previously generated reports")
  @GetMapping
  public ResponseEntity<ApiResponse> listGeneratedReports() {

    List<ReportResponseDto> reports = adminReportService.listGeneratedReports();
    return ResponseEntity.ok(ApiResponse.success("Reports retrieved successfully", reports));
  }

  @Operation(summary = "Get the data for a specific generated report")
  @GetMapping("/{reportId}")
  public ResponseEntity<ApiResponse> getReportDetails(@PathVariable String reportId) {

    ReportResponseDto report = adminReportService.getReportDetails(reportId);
    return ResponseEntity.ok(ApiResponse.success("Report details retrieved successfully", report));
  }
}