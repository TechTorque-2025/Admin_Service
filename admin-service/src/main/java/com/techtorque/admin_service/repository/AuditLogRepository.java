// ========================================
// AuditLogRepository.java
// ========================================

package com.techtorque.admin_service.repository;

import com.techtorque.admin_service.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for AuditLog entity
 * File Location: src/main/java/com/techtorque/admin_service/repository/AuditLogRepository.java
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, String> {

    /**
     * Find logs by user ID with pagination
     */
    Page<AuditLog> findByUserId(String userId, Pageable pageable);

    /**
     * Find logs by action
     */
    List<AuditLog> findByAction(String action);

    /**
     * Find logs by action with pagination
     */
    Page<AuditLog> findByAction(String action, Pageable pageable);

    /**
     * Find logs by entity type
     */
    List<AuditLog> findByEntityType(String entityType);

    /**
     * Find logs by entity type with pagination
     */
    Page<AuditLog> findByEntityType(String entityType, Pageable pageable);

    /**
     * Find logs for specific entity
     */
    List<AuditLog> findByEntityId(String entityId);

    /**
     * Find logs by entity type and entity ID
     */
    List<AuditLog> findByEntityTypeAndEntityId(String entityType, String entityId);

    /**
     * Find failed actions
     */
    List<AuditLog> findBySuccessFalse();

    /**
     * Find logs within date range
     */
    @Query("SELECT a FROM AuditLog a WHERE a.createdAt BETWEEN :startDate AND :endDate ORDER BY a.createdAt DESC")
    Page<AuditLog> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate,
                                   Pageable pageable);

    /**
     * Find logs by multiple filters
     */
    @Query("SELECT a FROM AuditLog a WHERE " +
            "(:userId IS NULL OR a.userId = :userId) AND " +
            "(:action IS NULL OR a.action = :action) AND " +
            "(:entityType IS NULL OR a.entityType = :entityType) AND " +
            "a.createdAt BETWEEN :startDate AND :endDate " +
            "ORDER BY a.createdAt DESC")
    Page<AuditLog> findByFilters(@Param("userId") String userId,
                                 @Param("action") String action,
                                 @Param("entityType") String entityType,
                                 @Param("startDate") LocalDateTime startDate,
                                 @Param("endDate") LocalDateTime endDate,
                                 Pageable pageable);

    /**
     * Count logs by user
     */
    Long countByUserId(String userId);

    /**
     * Count logs by action
     */
    Long countByAction(String action);

    /**
     * Count failed actions
     */
    Long countBySuccessFalse();

    /**
     * Get recent activity (last N records)
     */
    List<AuditLog> findTop100ByOrderByCreatedAtDesc();
}