package com.techtorque.admin_service.controller;

import com.techtorque.admin_service.dto.request.AuditLogSearchRequest;
import com.techtorque.admin_service.dto.response.ApiResponse;
import com.techtorque.admin_service.dto.response.AuditLogResponse;
import com.techtorque.admin_service.dto.response.PaginatedResponse;
import com.techtorque.admin_service.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin/audit-logs")
@Tag(name = "Admin: Audit Logs", description = "Endpoints for viewing system audit logs")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @Operation(summary = "Search and filter audit logs")
    @GetMapping
    public ResponseEntity<ApiResponse<PaginatedResponse<AuditLogResponse>>> searchAuditLogs(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String userRole,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String entityType,
            @RequestParam(required = false) String entityId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            @RequestParam(required = false) Boolean success,
            @RequestParam(required = false) String ipAddress,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "50") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        AuditLogSearchRequest searchRequest = AuditLogSearchRequest.builder()
                .userId(userId)
                .username(username)
                .userRole(userRole)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .fromDate(fromDate)
                .toDate(toDate)
                .success(success)
                .ipAddress(ipAddress)
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .sortDirection(sortDirection)
                .build();

        PaginatedResponse<AuditLogResponse> logs = auditLogService.searchAuditLogs(searchRequest);
        return ResponseEntity.ok(ApiResponse.success("Audit logs retrieved successfully", logs));
    }

    @Operation(summary = "Get audit log by ID")
    @GetMapping("/{logId}")
    public ResponseEntity<ApiResponse<AuditLogResponse>> getAuditLog(@PathVariable String logId) {
        AuditLogResponse log = auditLogService.getAuditLogById(logId);
        return ResponseEntity.ok(ApiResponse.success("Audit log retrieved successfully", log));
    }
}
