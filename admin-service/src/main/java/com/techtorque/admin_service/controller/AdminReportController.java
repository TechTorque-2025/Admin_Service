package com.techtorque.admin_service.controller;

import com.techtorque.admin_service.dto.request.GenerateReportRequest;
import com.techtorque.admin_service.dto.response.ApiResponse;
import com.techtorque.admin_service.dto.response.ReportResponse;
import com.techtorque.admin_service.service.AdminReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
  public ResponseEntity<ApiResponse<ReportResponse>> generateReport(
      @Valid @RequestBody GenerateReportRequest request,
      Authentication authentication) {
    String generatedBy = authentication.getName();
    ReportResponse report = adminReportService.generateReport(request, generatedBy);
    return ResponseEntity.ok(ApiResponse.success("Report generation initiated", report));
  }

  @Operation(summary = "List all previously generated reports")
  @GetMapping
  public ResponseEntity<ApiResponse<List<ReportResponse>>> listGeneratedReports(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int limit) {
    List<ReportResponse> reports = adminReportService.getAllReports(page, limit);
    return ResponseEntity.ok(ApiResponse.success("Reports retrieved successfully", reports));
  }

  @Operation(summary = "Get the data for a specific generated report")
  @GetMapping("/{reportId}")
  public ResponseEntity<ApiResponse<ReportResponse>> getReportDetails(@PathVariable String reportId) {
    ReportResponse report = adminReportService.getReportById(reportId);
    return ResponseEntity.ok(ApiResponse.success("Report retrieved successfully", report));
  }

  @Operation(summary = "Download a generated report")
  @GetMapping("/{reportId}/download")
  public ResponseEntity<byte[]> downloadReport(@PathVariable String reportId) {
    ReportResponse report = adminReportService.getReportById(reportId);
    byte[] data = adminReportService.downloadReport(reportId);

    String filename = (report.getTitle() != null ? report.getTitle().replace(" ", "_") : "report")
        + "." + report.getFormat().toLowerCase();

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
        .body(data);
  }
}