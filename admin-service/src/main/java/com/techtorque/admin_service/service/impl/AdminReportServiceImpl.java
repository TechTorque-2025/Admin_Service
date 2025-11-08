package com.techtorque.admin_service.service.impl;

import com.techtorque.admin_service.dto.request.GenerateReportRequest;
import com.techtorque.admin_service.dto.response.ReportResponse;
import com.techtorque.admin_service.entity.Report;
import com.techtorque.admin_service.entity.ReportFormat;
import com.techtorque.admin_service.entity.ReportStatus;
import com.techtorque.admin_service.entity.ReportType;
import com.techtorque.admin_service.repository.ReportRepository;
import com.techtorque.admin_service.service.AdminReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminReportServiceImpl implements AdminReportService {

  private final ReportRepository reportRepository;

  @Override
  public ReportResponse generateReport(GenerateReportRequest request, String generatedBy) {
    log.info("Generating report: {} from {} to {}", request.getType(), request.getFromDate(), request.getToDate());

    // Create report entity
    Report report = Report.builder()
            .type(ReportType.valueOf(request.getType()))
            .title(generateTitle(request))
            .fromDate(request.getFromDate())
            .toDate(request.getToDate())
            .format(ReportFormat.valueOf(request.getFormat()))
            .status(ReportStatus.PENDING)
            .generatedBy(generatedBy)
            .isScheduled(false)
            .build();

    Report saved = reportRepository.save(report);

    // TODO: Trigger async report generation here
    // For now, immediately mark as completed with dummy data
    saved.setStatus(ReportStatus.COMPLETED);
    saved.setCompletedAt(LocalDateTime.now());
    saved.setDataJson("{\"message\":\"Report data will be generated here\"}");
    saved.setDownloadUrl("/api/v1/admin/reports/" + saved.getId() + "/download");

    reportRepository.save(saved);

    log.info("Report generated successfully: {}", saved.getId());
    return convertToResponse(saved);
  }

  @Override
  public List<ReportResponse> getAllReports(int page, int limit) {
    log.info("Fetching all reports - page: {}, limit: {}", page, limit);

    Page<Report> reports = reportRepository.findAll(PageRequest.of(page, limit));

    return reports.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
  }

  @Override
  public ReportResponse getReportById(String reportId) {
    log.info("Fetching report: {}", reportId);

    Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new IllegalArgumentException("Report not found: " + reportId));

    return convertToResponse(report);
  }

  private String generateTitle(GenerateReportRequest request) {
    return String.format("%s Report - %s to %s",
            request.getType().replace("_", " "),
            request.getFromDate(),
            request.getToDate());
  }

  private ReportResponse convertToResponse(Report report) {
    return ReportResponse.builder()
            .reportId(report.getId())
            .type(report.getType().name())
            .title(report.getTitle())
            .fromDate(report.getFromDate())
            .toDate(report.getToDate())
            .format(report.getFormat().name())
            .status(report.getStatus().name())
            .generatedBy(report.getGeneratedBy())
            .downloadUrl(report.getDownloadUrl())
            .fileSize(report.getFileSize())
            .data(report.getDataJson())
            .errorMessage(report.getErrorMessage())
            .isScheduled(report.getIsScheduled())
            .createdAt(report.getCreatedAt())
            .completedAt(report.getCompletedAt())
            .build();
  }
}