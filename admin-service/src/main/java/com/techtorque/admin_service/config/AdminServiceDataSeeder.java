package com.techtorque.admin_service.config;

import com.techtorque.admin_service.entity.*;
import com.techtorque.admin_service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data seeder for Admin Service
 * Seeds service types, system configurations, and sample data for development
 */
@Component
@Profile({"dev", "local"})
@RequiredArgsConstructor
@Slf4j
public class AdminServiceDataSeeder implements CommandLineRunner {

    private final ServiceTypeRepository serviceTypeRepository;
    private final SystemConfigurationRepository systemConfigurationRepository;
    private final AuditLogRepository auditLogRepository;
    private final ReportRepository reportRepository;

    @Override
    public void run(String... args) {
        log.info("===== Starting Admin Service Data Seeding =====");

        seedServiceTypes();
        seedSystemConfigurations();
        seedSampleAuditLogs();
        seedSampleReports();

        log.info("===== Admin Service Data Seeding Completed =====");
    }

    private void seedServiceTypes() {
        log.info("Seeding service types...");

        if (serviceTypeRepository.count() > 0) {
            log.info("Service types already exist. Skipping seeding.");
            return;
        }

        List<ServiceType> serviceTypes = List.of(
                ServiceType.builder()
                        .name("Oil Change")
                        .description("Complete engine oil and filter replacement")
                        .category("MAINTENANCE")
                        .price(BigDecimal.valueOf(5000.00))
                        .defaultDurationMinutes(30)
                        .requiresApproval(false)
                        .dailyCapacity(20)
                        .skillLevel("BASIC")
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build(),

                ServiceType.builder()
                        .name("Brake Service")
                        .description("Brake pad replacement and system inspection")
                        .category("REPAIR")
                        .price(BigDecimal.valueOf(12000.00))
                        .defaultDurationMinutes(90)
                        .requiresApproval(false)
                        .dailyCapacity(8)
                        .skillLevel("INTERMEDIATE")
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build(),

                ServiceType.builder()
                        .name("Tire Rotation")
                        .description("Tire rotation and balance")
                        .category("MAINTENANCE")
                        .price(BigDecimal.valueOf(3000.00))
                        .defaultDurationMinutes(45)
                        .requiresApproval(false)
                        .dailyCapacity(15)
                        .skillLevel("BASIC")
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build(),

                ServiceType.builder()
                        .name("Engine Diagnostic")
                        .description("Complete engine diagnostic with computer scan")
                        .category("INSPECTION")
                        .price(BigDecimal.valueOf(8000.00))
                        .defaultDurationMinutes(60)
                        .requiresApproval(false)
                        .dailyCapacity(10)
                        .skillLevel("ADVANCED")
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build(),

                ServiceType.builder()
                        .name("AC Service")
                        .description("Air conditioning system service and recharge")
                        .category("MAINTENANCE")
                        .price(BigDecimal.valueOf(9500.00))
                        .defaultDurationMinutes(75)
                        .requiresApproval(false)
                        .dailyCapacity(6)
                        .skillLevel("INTERMEDIATE")
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build(),

                ServiceType.builder()
                        .name("Transmission Service")
                        .description("Transmission fluid replacement and inspection")
                        .category("MAINTENANCE")
                        .price(BigDecimal.valueOf(15000.00))
                        .defaultDurationMinutes(120)
                        .requiresApproval(false)
                        .dailyCapacity(5)
                        .skillLevel("ADVANCED")
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build(),

                ServiceType.builder()
                        .name("Full Vehicle Inspection")
                        .description("Comprehensive 60-point vehicle inspection")
                        .category("INSPECTION")
                        .price(BigDecimal.valueOf(4500.00))
                        .defaultDurationMinutes(45)
                        .requiresApproval(false)
                        .dailyCapacity(12)
                        .skillLevel("INTERMEDIATE")
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build(),

                ServiceType.builder()
                        .name("Custom Body Modification")
                        .description("Custom body work and modifications")
                        .category("MODIFICATION")
                        .price(BigDecimal.valueOf(50000.00))
                        .defaultDurationMinutes(480)
                        .requiresApproval(true)
                        .dailyCapacity(2)
                        .skillLevel("ADVANCED")
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build(),

                ServiceType.builder()
                        .name("Paint Job")
                        .description("Complete vehicle paint and finishing")
                        .category("MODIFICATION")
                        .price(BigDecimal.valueOf(75000.00))
                        .defaultDurationMinutes(960)
                        .requiresApproval(true)
                        .dailyCapacity(1)
                        .skillLevel("ADVANCED")
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build(),

                ServiceType.builder()
                        .name("Wheel Alignment")
                        .description("4-wheel computerized alignment")
                        .category("MAINTENANCE")
                        .price(BigDecimal.valueOf(6500.00))
                        .defaultDurationMinutes(60)
                        .requiresApproval(false)
                        .dailyCapacity(10)
                        .skillLevel("INTERMEDIATE")
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

        serviceTypeRepository.saveAll(serviceTypes);
        log.info("Seeded {} service types", serviceTypes.size());
    }

    private void seedSystemConfigurations() {
        log.info("Seeding system configurations...");

        if (systemConfigurationRepository.count() > 0) {
            log.info("System configurations already exist. Skipping seeding.");
            return;
        }

        List<SystemConfiguration> configurations = List.of(
                SystemConfiguration.builder()
                        .configKey("BUSINESS_HOURS_START")
                        .configValue("08:00")
                        .description("Business opening time")
                        .category("BUSINESS_HOURS")
                        .dataType("TIME")
                        .lastModifiedBy("SYSTEM")
                        .updatedAt(LocalDateTime.now())
                        .build(),

                SystemConfiguration.builder()
                        .configKey("BUSINESS_HOURS_END")
                        .configValue("18:00")
                        .description("Business closing time")
                        .category("BUSINESS_HOURS")
                        .dataType("TIME")
                        .lastModifiedBy("SYSTEM")
                        .updatedAt(LocalDateTime.now())
                        .build(),

                SystemConfiguration.builder()
                        .configKey("SLOTS_PER_HOUR")
                        .configValue("4")
                        .description("Number of appointment slots per hour")
                        .category("SCHEDULING")
                        .dataType("NUMBER")
                        .lastModifiedBy("SYSTEM")
                        .updatedAt(LocalDateTime.now())
                        .build(),

                SystemConfiguration.builder()
                        .configKey("MAX_APPOINTMENTS_PER_DAY")
                        .configValue("50")
                        .description("Maximum appointments allowed per day")
                        .category("SCHEDULING")
                        .dataType("NUMBER")
                        .lastModifiedBy("SYSTEM")
                        .updatedAt(LocalDateTime.now())
                        .build(),

                SystemConfiguration.builder()
                        .configKey("EMAIL_NOTIFICATIONS_ENABLED")
                        .configValue("true")
                        .description("Enable email notifications")
                        .category("NOTIFICATIONS")
                        .dataType("BOOLEAN")
                        .lastModifiedBy("SYSTEM")
                        .updatedAt(LocalDateTime.now())
                        .build(),

                SystemConfiguration.builder()
                        .configKey("SMS_NOTIFICATIONS_ENABLED")
                        .configValue("false")
                        .description("Enable SMS notifications")
                        .category("NOTIFICATIONS")
                        .dataType("BOOLEAN")
                        .lastModifiedBy("SYSTEM")
                        .updatedAt(LocalDateTime.now())
                        .build(),

                SystemConfiguration.builder()
                        .configKey("APPOINTMENT_REMINDER_HOURS")
                        .configValue("24")
                        .description("Hours before appointment to send reminder")
                        .category("NOTIFICATIONS")
                        .dataType("NUMBER")
                        .lastModifiedBy("SYSTEM")
                        .updatedAt(LocalDateTime.now())
                        .build(),

                SystemConfiguration.builder()
                        .configKey("COMPANY_NAME")
                        .configValue("TechTorque Auto Services")
                        .description("Company name")
                        .category("GENERAL")
                        .dataType("STRING")
                        .lastModifiedBy("SYSTEM")
                        .updatedAt(LocalDateTime.now())
                        .build(),

                SystemConfiguration.builder()
                        .configKey("COMPANY_EMAIL")
                        .configValue("info@techtorque.com")
                        .description("Company contact email")
                        .category("GENERAL")
                        .dataType("STRING")
                        .lastModifiedBy("SYSTEM")
                        .updatedAt(LocalDateTime.now())
                        .build(),

                SystemConfiguration.builder()
                        .configKey("COMPANY_PHONE")
                        .configValue("+94 11 234 5678")
                        .description("Company contact phone")
                        .category("GENERAL")
                        .dataType("STRING")
                        .lastModifiedBy("SYSTEM")
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

        systemConfigurationRepository.saveAll(configurations);
        log.info("Seeded {} system configurations", configurations.size());
    }

    private void seedSampleAuditLogs() {
        log.info("Seeding sample audit logs...");

        if (auditLogRepository.count() > 0) {
            log.info("Audit logs already exist. Skipping seeding.");
            return;
        }

        List<AuditLog> auditLogs = List.of(
                AuditLog.builder()
                        .userId("SYSTEM")
                        .username("system")
                        .userRole("SYSTEM")
                        .action("SEED_DATA")
                        .entityType("SERVICE_TYPE")
                        .entityId("BULK")
                        .description("Seeded initial service types")
                        .success(true)
                        .createdAt(LocalDateTime.now())
                        .build(),

                AuditLog.builder()
                        .userId("SYSTEM")
                        .username("system")
                        .userRole("SYSTEM")
                        .action("SEED_DATA")
                        .entityType("SYSTEM_CONFIG")
                        .entityId("BULK")
                        .description("Seeded initial system configurations")
                        .success(true)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        auditLogRepository.saveAll(auditLogs);
        log.info("Seeded {} audit logs", auditLogs.size());
    }

    private void seedSampleReports() {
        log.info("Seeding sample reports...");

        if (reportRepository.count() > 0) {
            log.info("Reports already exist. Skipping seeding.");
            return;
        }

        List<Report> reports = List.of(
                Report.builder()
                        .type(ReportType.REVENUE)
                        .title("Monthly Revenue Report - January 2025")
                        .fromDate(LocalDate.of(2025, 1, 1))
                        .toDate(LocalDate.of(2025, 1, 31))
                        .format(ReportFormat.PDF)
                        .status(ReportStatus.COMPLETED)
                        .generatedBy("admin")
                        .dataJson("{\"totalRevenue\": 450000, \"services\": 45}")
                        .downloadUrl("/api/v1/admin/reports/sample-1/download")
                        .isScheduled(false)
                        .createdAt(LocalDateTime.now().minusDays(5))
                        .completedAt(LocalDateTime.now().minusDays(5).plusMinutes(2))
                        .build(),

                Report.builder()
                        .type(ReportType.SERVICE_PERFORMANCE)
                        .title("Service Performance Report - Q1 2025")
                        .fromDate(LocalDate.of(2025, 1, 1))
                        .toDate(LocalDate.of(2025, 3, 31))
                        .format(ReportFormat.EXCEL)
                        .status(ReportStatus.COMPLETED)
                        .generatedBy("admin")
                        .dataJson("{\"completedServices\": 138, \"avgCompletionTime\": 6.5}")
                        .downloadUrl("/api/v1/admin/reports/sample-2/download")
                        .isScheduled(false)
                        .createdAt(LocalDateTime.now().minusDays(2))
                        .completedAt(LocalDateTime.now().minusDays(2).plusMinutes(5))
                        .build()
        );

        reportRepository.saveAll(reports);
        log.info("Seeded {} sample reports", reports.size());
    }
}
