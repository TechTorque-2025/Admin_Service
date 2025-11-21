package com.techtorque.admin_service.repository;

import com.techtorque.admin_service.entity.ReportSchedule;
import com.techtorque.admin_service.entity.ReportType;
import com.techtorque.admin_service.entity.ScheduleFrequency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ReportScheduleRepositoryTest {

    @Autowired
    private ReportScheduleRepository reportScheduleRepository;

    private ReportSchedule dailySchedule;
    private ReportSchedule weeklySchedule;
    private ReportSchedule inactiveSchedule;

    @BeforeEach
    void setUp() {
        reportScheduleRepository.deleteAll();

        dailySchedule = ReportSchedule.builder()
                .reportType(ReportType.SERVICE_PERFORMANCE)
                .frequency(ScheduleFrequency.DAILY)
                .recipients(Arrays.asList("admin@test.com", "manager@test.com"))
                .hourOfDay(9)
                .createdBy("admin@test.com")
                .active(true)
                .nextRun(LocalDateTime.now().plusDays(1))
                .build();

        weeklySchedule = ReportSchedule.builder()
                .reportType(ReportType.REVENUE)
                .frequency(ScheduleFrequency.WEEKLY)
                .recipients(Arrays.asList("finance@test.com"))
                .dayOfSchedule(1) // Monday
                .hourOfDay(10)
                .createdBy("admin@test.com")
                .active(true)
                .nextRun(LocalDateTime.now().plusWeeks(1))
                .build();

        inactiveSchedule = ReportSchedule.builder()
                .reportType(ReportType.EMPLOYEE_PRODUCTIVITY)
                .frequency(ScheduleFrequency.MONTHLY)
                .recipients(Arrays.asList("hr@test.com"))
                .dayOfSchedule(1) // First day of month
                .hourOfDay(8)
                .createdBy("hr@test.com")
                .active(false)
                .nextRun(LocalDateTime.now().plusMonths(1))
                .build();

        reportScheduleRepository.save(dailySchedule);
        reportScheduleRepository.save(weeklySchedule);
        reportScheduleRepository.save(inactiveSchedule);
    }

    @Test
    void testFindByActiveTrue() {
        List<ReportSchedule> activeSchedules = reportScheduleRepository.findByActiveTrue();

        assertThat(activeSchedules).hasSize(2);
        assertThat(activeSchedules).extracting(ReportSchedule::getActive)
                .containsOnly(true);
    }

    @Test
    void testFindByReportType() {
        List<ReportSchedule> serviceSchedules = reportScheduleRepository.findByReportType(ReportType.SERVICE_PERFORMANCE);

        assertThat(serviceSchedules).hasSize(1);
        assertThat(serviceSchedules.get(0).getReportType()).isEqualTo(ReportType.SERVICE_PERFORMANCE);
    }

    @Test
    void testFindByFrequency() {
        List<ReportSchedule> dailySchedules = reportScheduleRepository.findByFrequency(ScheduleFrequency.DAILY);

        assertThat(dailySchedules).hasSize(1);
        assertThat(dailySchedules.get(0).getFrequency()).isEqualTo(ScheduleFrequency.DAILY);
    }

    @Test
    void testFindByCreatedBy() {
        List<ReportSchedule> adminSchedules = reportScheduleRepository.findByCreatedBy("admin@test.com");

        assertThat(adminSchedules).hasSize(2);
        assertThat(adminSchedules).extracting(ReportSchedule::getCreatedBy)
                .containsOnly("admin@test.com");
    }

    @Test
    void testFindDueSchedules() {
        // Create a schedule that is due now
        ReportSchedule dueSchedule = ReportSchedule.builder()
                .reportType(ReportType.APPOINTMENT_SUMMARY)
                .frequency(ScheduleFrequency.DAILY)
                .recipients(Arrays.asList("test@test.com"))
                .hourOfDay(12)
                .createdBy("test@test.com")
                .active(true)
                .nextRun(LocalDateTime.now().minusHours(1)) // Due 1 hour ago
                .build();
        reportScheduleRepository.save(dueSchedule);

        List<ReportSchedule> dueSchedules = reportScheduleRepository.findDueSchedules(LocalDateTime.now());

        assertThat(dueSchedules).hasSize(1);
        assertThat(dueSchedules.get(0).getReportType()).isEqualTo(ReportType.APPOINTMENT_SUMMARY);
    }

    @Test
    void testCountByActiveTrue() {
        Long activeCount = reportScheduleRepository.countByActiveTrue();

        assertThat(activeCount).isEqualTo(2L);
    }

    @Test
    void testSaveAndRetrieve() {
        ReportSchedule newSchedule = ReportSchedule.builder()
                .reportType(ReportType.CUSTOMER_SATISFACTION)
                .frequency(ScheduleFrequency.WEEKLY)
                .recipients(Arrays.asList("support@test.com"))
                .dayOfSchedule(5) // Friday
                .hourOfDay(15)
                .createdBy("support@test.com")
                .active(true)
                .nextRun(LocalDateTime.now().plusWeeks(1))
                .build();

        ReportSchedule saved = reportScheduleRepository.save(newSchedule);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getReportType()).isEqualTo(ReportType.CUSTOMER_SATISFACTION);
    }
}
