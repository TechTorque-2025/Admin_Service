// ========================================
// AuditLog.java
// ========================================

package com.techtorque.admin_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

/**
 * Entity for audit logging all system changes
 * File Location: src/main/java/com/techtorque/admin_service/entity/AuditLog.java
 */
@Entity
@Table(name = "audit_logs", indexes = {
        @Index(name = "idx_user_id", columnList = "userId"),
        @Index(name = "idx_action", columnList = "action"),
        @Index(name = "idx_entity_type", columnList = "entityType"),
        @Index(name = "idx_created_at", columnList = "createdAt")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(nullable = false, length = 50)
    private String userRole;

    @Column(nullable = false, length = 50)
    private String action; // CREATE, UPDATE, DELETE, LOGIN, LOGOUT, etc.

    @Column(nullable = false, length = 50)
    private String entityType; // USER, SERVICE, APPOINTMENT, SERVICE_TYPE, etc.

    @Column(length = 100)
    private String entityId;

    @Column(length = 500)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String oldValues; // JSON of old values before change

    @Column(columnDefinition = "TEXT")
    private String newValues; // JSON of new values after change

    @Column(length = 45) // IPv6 max length
    private String ipAddress;

    @Column(length = 500)
    private String userAgent;

    @Builder.Default
    private Boolean success = true;

    @Column(length = 1000)
    private String errorMessage;

    @Column(length = 100)
    private String requestId; // For tracing requests

    private Long executionTimeMs; // How long the operation took

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
}