package com.techtorque.admin_service.service;

import com.techtorque.admin_service.dto.request.GenerateReportRequest;
import com.techtorque.admin_service.dto.response.ReportResponse;
import com.techtorque.admin_service.entity.Report;
import com.techtorque.admin_service.entity.ReportFormat;
import com.techtorque.admin_service.entity.ReportStatus;
import com.techtorque.admin_service.entity.ReportType;
import com.techtorque.admin_service.repository.ReportRepository;
import com.techtorque.admin_service.service.impl.AdminReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminReportServiceTest {

    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private AdminReportServiceImpl adminReportService;

    private Report testReport;
    private GenerateReportRequest generateRequest;

    @BeforeEach
    void setUp() {
        testReport = Report.builder()
                .id("report-123")
                .type(ReportType.SERVICE_PERFORMANCE)
                .title("Service Performance Report - 2024-01-01 to 2024-01-31")
                .fromDate(LocalDate.of(2024, 1, 1))
                .toDate(LocalDate.of(2024, 1, 31))
                .format(ReportFormat.PDF)
                .status(ReportStatus.COMPLETED)
                .generatedBy("admin@test.com")
                .isScheduled(false)
                .downloadUrl("/api/v1/admin/reports/report-123/download")
                .dataJson("{\"message\":\"Report data\"}")
                .createdAt(LocalDateTime.now())
                .completedAt(LocalDateTime.now())
                .build();

        generateRequest = new GenerateReportRequest();
        generateRequest.setType("SERVICE_PERFORMANCE");
        generateRequest.setFromDate(LocalDate.of(2024, 1, 1));
        generateRequest.setToDate(LocalDate.of(2024, 1, 31));
        generateRequest.setFormat("PDF");
    }

    @Test
    void testGenerateReport_Success() {
        when(reportRepository.save(any(Report.class))).thenReturn(testReport);

        ReportResponse response = adminReportService.generateReport(generateRequest, "admin@test.com");

        assertThat(response).isNotNull();
        assertThat(response.getType()).isEqualTo("SERVICE_PERFORMANCE");
        assertThat(response.getStatus()).isEqualTo("COMPLETED");
        verify(reportRepository, times(2)).save(any(Report.class));
    }

    @Test
    void testGetAllReports_Success() {
        Page<Report> page = new PageImpl<>(Arrays.asList(testReport));
        when(reportRepository.findAll(any(PageRequest.class))).thenReturn(page);

        List<ReportResponse> responses = adminReportService.getAllReports(0, 10);

        assertThat(responses).isNotEmpty();
        assertThat(responses.get(0).getReportId()).isEqualTo("report-123");
        verify(reportRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void testGetReportById_Success() {
        when(reportRepository.findById(anyString())).thenReturn(Optional.of(testReport));

        ReportResponse response = adminReportService.getReportById("report-123");

        assertThat(response).isNotNull();
        assertThat(response.getReportId()).isEqualTo("report-123");
        assertThat(response.getType()).isEqualTo("SERVICE_PERFORMANCE");
        verify(reportRepository, times(1)).findById("report-123");
    }

    @Test
    void testGetReportById_NotFound() {
        when(reportRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminReportService.getReportById("non-existent"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Report not found");
    }
}
