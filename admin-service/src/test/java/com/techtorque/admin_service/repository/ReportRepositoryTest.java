package com.techtorque.admin_service.repository;

import com.techtorque.admin_service.entity.Report;
import com.techtorque.admin_service.entity.ReportFormat;
import com.techtorque.admin_service.entity.ReportStatus;
import com.techtorque.admin_service.entity.ReportType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ReportRepositoryTest {

    @Autowired
    private ReportRepository reportRepository;

    private Report testReport;

    @BeforeEach
    void setUp() {
        reportRepository.deleteAll();

        testReport = new Report();
        testReport.setType(ReportType.REVENUE);
        testReport.setTitle("Monthly Revenue Report");
        testReport.setFromDate(LocalDate.of(2025, 1, 1));
        testReport.setToDate(LocalDate.of(2025, 1, 31));
        testReport.setFormat(ReportFormat.PDF);
        testReport.setStatus(ReportStatus.COMPLETED);
        testReport.setGeneratedBy("admin@test.com");
        testReport.setCreatedAt(LocalDateTime.now());
        testReport.setCompletedAt(LocalDateTime.now());
    }

    @Test
    void testSaveReport() {
        Report saved = reportRepository.save(testReport);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(testReport.getId());
        assertThat(saved.getTitle()).isEqualTo("Monthly Revenue Report");
        assertThat(saved.getType()).isEqualTo(ReportType.REVENUE);
    }

    @Test
    void testFindById() {
        reportRepository.save(testReport);

        Optional<Report> found = reportRepository.findById(testReport.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Monthly Revenue Report");
    }

    @Test
    void testFindByGeneratedBy() {
        Report report2 = new Report();
        report2.setType(ReportType.SERVICE_PERFORMANCE);
        report2.setTitle("Service Performance Report");
        report2.setFromDate(LocalDate.of(2025, 2, 1));
        report2.setToDate(LocalDate.of(2025, 2, 28));
        report2.setFormat(ReportFormat.EXCEL);
        report2.setStatus(ReportStatus.PENDING);
        report2.setGeneratedBy("admin2@test.com");
        report2.setCreatedAt(LocalDateTime.now());

        reportRepository.save(testReport);
        reportRepository.save(report2);

        List<Report> reports = reportRepository.findByGeneratedBy("admin@test.com");

        assertThat(reports).hasSize(1);
        assertThat(reports.get(0).getGeneratedBy()).isEqualTo("admin@test.com");
    }

    @Test
    void testFindByType() {
        Report performanceReport = new Report();
        performanceReport.setType(ReportType.SERVICE_PERFORMANCE);
        performanceReport.setTitle("Performance Report");
        performanceReport.setFromDate(LocalDate.of(2025, 1, 1));
        performanceReport.setToDate(LocalDate.of(2025, 1, 31));
        performanceReport.setFormat(ReportFormat.JSON);
        performanceReport.setStatus(ReportStatus.COMPLETED);
        performanceReport.setGeneratedBy("admin@test.com");
        performanceReport.setCreatedAt(LocalDateTime.now());

        reportRepository.save(testReport);
        reportRepository.save(performanceReport);

        List<Report> revenueReports = reportRepository.findByType(ReportType.REVENUE);

        assertThat(revenueReports).hasSize(1);
        assertThat(revenueReports.get(0).getType()).isEqualTo(ReportType.REVENUE);
    }

    @Test
    void testFindByStatus() {
        Report pendingReport = new Report();
        pendingReport.setType(ReportType.EMPLOYEE_PRODUCTIVITY);
        pendingReport.setTitle("Productivity Report");
        pendingReport.setFromDate(LocalDate.of(2025, 1, 1));
        pendingReport.setToDate(LocalDate.of(2025, 1, 31));
        pendingReport.setFormat(ReportFormat.CSV);
        pendingReport.setStatus(ReportStatus.PENDING);
        pendingReport.setGeneratedBy("admin@test.com");
        pendingReport.setCreatedAt(LocalDateTime.now());

        reportRepository.save(testReport);
        reportRepository.save(pendingReport);

        List<Report> completedReports = reportRepository.findByStatus(ReportStatus.COMPLETED);

        assertThat(completedReports).hasSize(1);
        assertThat(completedReports.get(0).getStatus()).isEqualTo(ReportStatus.COMPLETED);
    }

    // Test for method that may not exist in repository - commented out
    // @Test
    // void testFindByCreatedAtBetween() ...

    @Test
    void testUpdateReportStatus() {
        reportRepository.save(testReport);

        testReport.setStatus(ReportStatus.FAILED);
        testReport.setErrorMessage("Generation failed");
        Report updated = reportRepository.save(testReport);

        assertThat(updated.getStatus()).isEqualTo(ReportStatus.FAILED);
        assertThat(updated.getErrorMessage()).isEqualTo("Generation failed");
    }
}
