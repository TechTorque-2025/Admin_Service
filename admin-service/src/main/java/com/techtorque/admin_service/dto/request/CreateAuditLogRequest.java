package com.techtorque.admin_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating an audit log entry
 * Used internally by services
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateAuditLogRequest {

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "User role is required")
    private String userRole;

    @NotBlank(message = "Action is required")
    private String action; // CREATE, UPDATE, DELETE, LOGIN, LOGOUT, etc.

    @NotBlank(message = "Entity type is required")
    private String entityType; // USER, SERVICE, APPOINTMENT, SERVICE_TYPE, etc.

    private String entityId;
    private String description;
    private String oldValues; // JSON string
    private String newValues; // JSON string
    private String ipAddress;
    private String userAgent;

    @Builder.Default
    private Boolean success = true;

    private String errorMessage;
    private String requestId;
    private Long executionTimeMs;
}
