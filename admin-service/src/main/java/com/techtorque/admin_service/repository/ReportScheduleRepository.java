// ========================================
// ReportScheduleRepository.java
// ========================================

package com.techtorque.admin_service.repository;

import com.techtorque.admin_service.entity.ReportSchedule;
import com.techtorque.admin_service.entity.ReportType;
import com.techtorque.admin_service.entity.ScheduleFrequency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for ReportSchedule entity
 * File Location: src/main/java/com/techtorque/admin_service/repository/ReportScheduleRepository.java
 */
@Repository
public interface ReportScheduleRepository extends JpaRepository<ReportSchedule, String> {

    /**
     * Find active schedules
     */
    List<ReportSchedule> findByActiveTrue();

    /**
     * Find schedules by report type
     */
    List<ReportSchedule> findByReportType(ReportType reportType);

    /**
     * Find schedules by frequency
     */
    List<ReportSchedule> findByFrequency(ScheduleFrequency frequency);

    /**
     * Find schedules created by user
     */
    List<ReportSchedule> findByCreatedBy(String userId);

    /**
     * Find schedules due to run (nextRun <= now and active = true)
     */
    @Query("SELECT rs FROM ReportSchedule rs WHERE rs.active = true AND rs.nextRun <= :now")
    List<ReportSchedule> findDueSchedules(@Param("now") LocalDateTime now);

    /**
     * Count active schedules
     */
    Long countByActiveTrue();
}