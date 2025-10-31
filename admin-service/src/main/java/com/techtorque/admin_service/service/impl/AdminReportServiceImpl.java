package com.techtorque.admin_service.service.impl;

import com.techtorque.admin_service.dto.ReportRequestDto;
import com.techtorque.admin_service.dto.ReportResponseDto;
import com.techtorque.admin_service.service.AdminReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminReportServiceImpl implements AdminReportService {

  private final WebClient.Builder webClientBuilder;

  // In-memory storage for demo purposes (use database in production)
  private final List<ReportResponseDto> reportStorage = new ArrayList<>();

  @Override
  public ReportResponseDto generateReport(ReportRequestDto dto, String adminId) {
    log.info("Generating {} report from {} to {} by admin: {}",
            dto.getReportType(), dto.getStartDate(), dto.getEndDate(), adminId);

    Map<String, Object> reportData = new HashMap<>();

    // Generate report based on type
    switch (dto.getReportType().toUpperCase()) {
      case "APPOINTMENTS":
        reportData.put("totalAppointments", 75);
        reportData.put("completedAppointments", 45);
        reportData.put("cancelledAppointments", 5);
        reportData.put("pendingAppointments", 25);
        reportData.put("completionRate", "90%");
        break;

      case "PROJECTS":
        reportData.put("totalProjects", 55);
        reportData.put("completedProjects", 30);
        reportData.put("inProgressProjects", 12);
        reportData.put("requestedProjects", 8);
        reportData.put("quotedProjects", 5);
        reportData.put("averageCompletionTime", "7.5 days");
        break;

      case "REVENUE":
        reportData.put("totalRevenue", "125000.00");
        reportData.put("paidInvoices", 65);
        reportData.put("pendingInvoices", 15);
        reportData.put("averageInvoiceAmount", "1923.08");
        reportData.put("largestPayment", "15000.00");
        break;

      case "TIME_LOGS":
        reportData.put("totalHoursLogged", 2500);
        reportData.put("totalEmployees", 25);
        reportData.put("averageHoursPerEmployee", 100);
        reportData.put("mostProductiveEmployee", "EMP-001");
        reportData.put("topWorkType", "Maintenance");
        break;

      default:
        reportData.put("error", "Unknown report type");
    }

    // Create report response
    ReportResponseDto report = ReportResponseDto.builder()
            .id(UUID.randomUUID().toString())
            .reportType(dto.getReportType())
            .startDate(dto.getStartDate().toString())
            .endDate(dto.getEndDate().toString())
            .data(reportData)
            .generatedAt(LocalDateTime.now())
            .generatedBy(adminId)
            .build();

    // Store report
    reportStorage.add(report);
    log.info("Report generated successfully with ID: {}", report.getId());

    return report;
  }

  @Override
  public List<ReportResponseDto> listGeneratedReports() {
    log.info("Listing all generated reports. Count: {}", reportStorage.size());
    return new ArrayList<>(reportStorage);
  }

  @Override
  public ReportResponseDto getReportDetails(String reportId) {
    log.info("Fetching report: {}", reportId);
    return reportStorage.stream()
            .filter(report -> report.getId().equals(reportId))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Report not found with ID: " + reportId));
  }
}