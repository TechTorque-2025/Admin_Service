package com.techtorque.admin_service.service.impl;

import com.techtorque.admin_service.dto.request.AuditLogSearchRequest;
import com.techtorque.admin_service.dto.request.CreateAuditLogRequest;
import com.techtorque.admin_service.dto.response.AuditLogResponse;
import com.techtorque.admin_service.dto.response.PaginatedResponse;
import com.techtorque.admin_service.entity.AuditLog;
import com.techtorque.admin_service.exception.ResourceNotFoundException;
import com.techtorque.admin_service.repository.AuditLogRepository;
import com.techtorque.admin_service.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public AuditLogResponse createAuditLog(CreateAuditLogRequest request) {
        AuditLog auditLog = AuditLog.builder()
                .userId(request.getUserId())
                .username(request.getUsername())
                .userRole(request.getUserRole())
                .action(request.getAction())
                .entityType(request.getEntityType())
                .entityId(request.getEntityId())
                .description(request.getDescription())
                .oldValues(request.getOldValues())
                .newValues(request.getNewValues())
                .ipAddress(request.getIpAddress())
                .userAgent(request.getUserAgent())
                .success(request.getSuccess())
                .errorMessage(request.getErrorMessage())
                .requestId(request.getRequestId())
                .executionTimeMs(request.getExecutionTimeMs())
                .createdAt(LocalDateTime.now())
                .build();

        AuditLog saved = auditLogRepository.save(auditLog);
        return convertToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponse<AuditLogResponse> searchAuditLogs(AuditLogSearchRequest request) {
        log.info("Searching audit logs with filters: {}", request);

        // Build dynamic query based on filters
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(
                        request.getSortDirection().equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC,
                        request.getSortBy()
                )
        );

        // For simplicity, using findAll with filters
        // In a production system, you'd use Specifications or QueryDSL
        List<AuditLog> filtered = auditLogRepository.findAll().stream()
                .filter(log -> request.getUserId() == null || log.getUserId().equals(request.getUserId()))
                .filter(log -> request.getUsername() == null || log.getUsername().contains(request.getUsername()))
                .filter(log -> request.getUserRole() == null || log.getUserRole().equals(request.getUserRole()))
                .filter(log -> request.getAction() == null || log.getAction().equals(request.getAction()))
                .filter(log -> request.getEntityType() == null || log.getEntityType().equals(request.getEntityType()))
                .filter(log -> request.getEntityId() == null || log.getEntityId().equals(request.getEntityId()))
                .filter(log -> request.getFromDate() == null || !log.getCreatedAt().isBefore(request.getFromDate()))
                .filter(log -> request.getToDate() == null || !log.getCreatedAt().isAfter(request.getToDate()))
                .filter(log -> request.getSuccess() == null || log.getSuccess().equals(request.getSuccess()))
                .filter(log -> request.getIpAddress() == null || (log.getIpAddress() != null && log.getIpAddress().contains(request.getIpAddress())))
                .collect(Collectors.toList());

        // Manual pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filtered.size());
        List<AuditLog> pageContent = filtered.subList(start, end);

        List<AuditLogResponse> content = pageContent.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        int totalPages = (int) Math.ceil((double) filtered.size() / request.getSize());
        
        return PaginatedResponse.<AuditLogResponse>builder()
                .data(content)
                .page(request.getPage())
                .limit(request.getSize())
                .total((long) filtered.size())
                .totalPages(totalPages)
                .hasNext(request.getPage() < totalPages - 1)
                .hasPrevious(request.getPage() > 0)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuditLogResponse getAuditLogById(String id) {
        log.info("Fetching audit log: {}", id);

        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Audit Log", id));

        return convertToResponse(auditLog);
    }

    @Override
    public void logAction(String userId, String username, String userRole, String action,
                         String entityType, String entityId, String description) {
        logAction(userId, username, userRole, action, entityType, entityId, description, null, null);
    }

    @Override
    public void logAction(String userId, String username, String userRole, String action,
                         String entityType, String entityId, String description,
                         String oldValues, String newValues) {
        try {
            AuditLog auditLog = AuditLog.builder()
                    .userId(userId)
                    .username(username)
                    .userRole(userRole)
                    .action(action)
                    .entityType(entityType)
                    .entityId(entityId)
                    .description(description)
                    .oldValues(oldValues)
                    .newValues(newValues)
                    .success(true)
                    .createdAt(LocalDateTime.now())
                    .build();

            auditLogRepository.save(auditLog);
        } catch (Exception e) {
            log.error("Failed to create audit log", e);
            // Don't throw exception - audit logging shouldn't break business logic
        }
    }

    private AuditLogResponse convertToResponse(AuditLog auditLog) {
        return AuditLogResponse.builder()
                .logId(auditLog.getId())
                .userId(auditLog.getUserId())
                .username(auditLog.getUsername())
                .userRole(auditLog.getUserRole())
                .action(auditLog.getAction())
                .entityType(auditLog.getEntityType())
                .entityId(auditLog.getEntityId())
                .description(auditLog.getDescription())
                .ipAddress(auditLog.getIpAddress())
                .success(auditLog.getSuccess())
                .errorMessage(auditLog.getErrorMessage())
                .timestamp(auditLog.getCreatedAt())
                .build();
    }
}
