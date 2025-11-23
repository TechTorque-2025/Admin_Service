package com.techtorque.admin_service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techtorque.admin_service.dto.external.AppointmentDto;
import com.techtorque.admin_service.dto.external.InvoiceDto;
import com.techtorque.admin_service.dto.request.GenerateReportRequest;
import com.techtorque.admin_service.dto.response.ReportResponse;
import com.techtorque.admin_service.entity.Report;
import com.techtorque.admin_service.entity.ReportFormat;
import com.techtorque.admin_service.entity.ReportStatus;
import com.techtorque.admin_service.entity.ReportType;
import com.techtorque.admin_service.exception.ResourceNotFoundException;
import com.techtorque.admin_service.repository.ReportRepository;
import com.techtorque.admin_service.service.AdminReportService;
import com.techtorque.admin_service.util.PdfReportGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminReportServiceImpl implements AdminReportService {

  private final ReportRepository reportRepository;
  private final ObjectMapper objectMapper;
  private final PdfReportGenerator pdfReportGenerator;

  private final WebClient appointmentServiceWebClient;

  private final WebClient paymentServiceWebClient;

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
        .status(ReportStatus.GENERATING)
        .generatedBy(generatedBy)
        .isScheduled(false)
        .createdAt(LocalDateTime.now())
        .build();

    Report saved = reportRepository.save(report);

    try {
      Map<String, Object> reportData = new HashMap<>();
      ReportType type = ReportType.valueOf(request.getType());

      switch (type) {
        case APPOINTMENT_SUMMARY:
          reportData = generateAppointmentSummary(request.getFromDate(), request.getToDate());
          break;
        case REVENUE:
          reportData = generateRevenueReport(request.getFromDate(), request.getToDate());
          break;
        default:
          throw new UnsupportedOperationException("Report type not implemented: " + type);
      }

      saved.setDataJson(objectMapper.writeValueAsString(reportData));
      saved.setStatus(ReportStatus.COMPLETED);
      saved.setCompletedAt(LocalDateTime.now());
      // For now, download URL is just a placeholder or points to the get endpoint
      saved.setDownloadUrl("/api/v1/admin/reports/" + saved.getId() + "/download");

    } catch (Exception e) {
      log.error("Error generating report", e);
      saved.setStatus(ReportStatus.FAILED);
      saved.setErrorMessage(e.getMessage());
    }

    reportRepository.save(saved);
    return convertToResponse(saved);
  }

  private Map<String, Object> generateAppointmentSummary(LocalDate fromDate, LocalDate toDate) {
    log.info("Fetching appointments from {} to {}", fromDate, toDate);

    List<AppointmentDto> appointments = appointmentServiceWebClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/appointments")
            .queryParam("fromDate", fromDate.toString())
            .queryParam("toDate", toDate.toString())
            .build())
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<AppointmentDto>>() {
        })
        .block();

    if (appointments == null) {
      appointments = List.of();
    }

    Map<String, Object> data = new HashMap<>();
    data.put("totalAppointments", appointments.size());

    Map<String, Long> statusBreakdown = appointments.stream()
        .collect(Collectors.groupingBy(AppointmentDto::getStatus, Collectors.counting()));
    data.put("statusBreakdown", statusBreakdown);

    Map<String, Long> serviceTypeBreakdown = appointments.stream()
        .collect(Collectors.groupingBy(AppointmentDto::getServiceType, Collectors.counting()));
    data.put("serviceTypeBreakdown", serviceTypeBreakdown);

    data.put("appointments", appointments); // Include raw data for now

    return data;
  }

  private Map<String, Object> generateRevenueReport(LocalDate fromDate, LocalDate toDate) {
    log.info("Fetching invoices for revenue report");

    // Fetch all invoices and filter in memory as per current limitation
    List<InvoiceDto> allInvoices = paymentServiceWebClient.get()
        .uri("/invoices")
        .retrieve()
        .bodyToMono(new ParameterizedTypeReference<List<InvoiceDto>>() {
        })
        .block();

    if (allInvoices == null) {
      allInvoices = List.of();
    }

    List<InvoiceDto> filteredInvoices = allInvoices.stream()
        .filter(inv -> {
          LocalDate date = inv.getIssuedAt().toLocalDate();
          return (date.isEqual(fromDate) || date.isAfter(fromDate)) &&
              (date.isEqual(toDate) || date.isBefore(toDate));
        })
        .collect(Collectors.toList());

    Map<String, Object> data = new HashMap<>();
    data.put("totalInvoices", filteredInvoices.size());

    BigDecimal totalRevenue = filteredInvoices.stream()
        .map(InvoiceDto::getTotalAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    data.put("totalRevenue", totalRevenue);

    BigDecimal totalPaid = filteredInvoices.stream()
        .map(InvoiceDto::getPaidAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    data.put("totalPaid", totalPaid);

    BigDecimal outstandingAmount = filteredInvoices.stream()
        .map(InvoiceDto::getBalanceAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
    data.put("outstandingAmount", outstandingAmount);

    data.put("invoices", filteredInvoices);

    return data;
  }

  @Override
  public List<ReportResponse> getAllReports(int page, int limit) {
    log.info("Fetching all reports - page: {}, limit: {}", page, limit);

    Page<Report> reports = reportRepository.findAll(
        PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt")));

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

  @Override
  public byte[] downloadReport(String reportId) {
    Report report = reportRepository.findById(reportId)
        .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + reportId));

    if (report.getStatus() != ReportStatus.COMPLETED) {
      throw new IllegalStateException("Report is not completed yet");
    }

    try {
      if (report.getFormat() == ReportFormat.PDF) {
        return pdfReportGenerator.generatePdf(report);
      } else if (report.getFormat() == ReportFormat.JSON) {
        return report.getDataJson().getBytes();
      } else {
        // For CSV/Excel, we might need other generators. For now, fallback to JSON
        // bytes or throw
        throw new UnsupportedOperationException("Download not implemented for format: " + report.getFormat());
      }
    } catch (Exception e) {
      log.error("Error generating download for report: {}", reportId, e);
      throw new RuntimeException("Failed to generate report download", e);
    }
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