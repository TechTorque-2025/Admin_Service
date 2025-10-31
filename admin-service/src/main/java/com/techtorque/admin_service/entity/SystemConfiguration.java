// ========================================
// SystemConfiguration.java
// ========================================

package com.techtorque.admin_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Entity for storing system-wide configuration
 * File Location: src/main/java/com/techtorque/admin_service/entity/SystemConfiguration.java
 */
@Entity
@Table(name = "system_configuration")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class SystemConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true, length = 100)
    private String configKey;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String configValue; // JSON string for complex values

    @Column(length = 500)
    private String description;

    @Column(nullable = false, length = 50)
    private String category; // BUSINESS_HOURS, SCHEDULING, NOTIFICATIONS, etc.

    @Column(length = 50)
    private String dataType; // STRING, NUMBER, BOOLEAN, JSON, TIME

    private String lastModifiedBy;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}

/**
 * Example configurations:
 * - configKey: "BUSINESS_HOURS_START", configValue: "08:00", dataType: "TIME"
 * - configKey: "BUSINESS_HOURS_END", configValue: "18:00", dataType: "TIME"
 * - configKey: "SLOTS_PER_HOUR", configValue: "4", dataType: "NUMBER"
 * - configKey: "MAX_APPOINTMENTS_PER_DAY", configValue: "50", dataType: "NUMBER"
 * - configKey: "EMAIL_NOTIFICATIONS_ENABLED", configValue: "true", dataType: "BOOLEAN"
 */