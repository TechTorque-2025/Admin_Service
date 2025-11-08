// ========================================
// AuditLogResponse.java
// ========================================

package com.techtorque.admin_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Response DTO for audit log entries
 * Used by: GET /admin/audit-logs
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogResponse {
    private String logId;
    private String userId;
    private String username;
    private String userRole;
    private String action; // CREATE, UPDATE, DELETE, LOGIN, etc.
    private String entityType; // USER, SERVICE, APPOINTMENT, etc.
    private String entityId;
    private String description;
    private String ipAddress;
    private Boolean success;
    private String errorMessage;
    private LocalDateTime timestamp;
}