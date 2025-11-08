package com.techtorque.admin_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Request DTO for searching/filtering audit logs
 * Used by: GET /admin/audit-logs
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogSearchRequest {

    private String userId;
    private String username;
    private String userRole;
    private String action; // CREATE, UPDATE, DELETE, LOGIN, etc.
    private String entityType; // USER, SERVICE, APPOINTMENT, etc.
    private String entityId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime toDate;

    private Boolean success;
    private String ipAddress;

    // Pagination
    @Builder.Default
    private Integer page = 0;

    @Builder.Default
    private Integer size = 50;

    @Builder.Default
    private String sortBy = "createdAt";

    @Builder.Default
    private String sortDirection = "DESC";
}
