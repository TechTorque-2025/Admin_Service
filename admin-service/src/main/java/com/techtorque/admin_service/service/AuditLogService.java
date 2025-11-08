package com.techtorque.admin_service.service;

import com.techtorque.admin_service.dto.request.AuditLogSearchRequest;
import com.techtorque.admin_service.dto.request.CreateAuditLogRequest;
import com.techtorque.admin_service.dto.response.AuditLogResponse;
import com.techtorque.admin_service.dto.response.PaginatedResponse;

/**
 * Service interface for audit logging
 */
public interface AuditLogService {
    
    AuditLogResponse createAuditLog(CreateAuditLogRequest request);
    
    PaginatedResponse<AuditLogResponse> searchAuditLogs(AuditLogSearchRequest request);
    
    AuditLogResponse getAuditLogById(String id);
    
    void logAction(String userId, String username, String userRole, String action, 
                   String entityType, String entityId, String description);
    
    void logAction(String userId, String username, String userRole, String action,
                   String entityType, String entityId, String description,
                   String oldValues, String newValues);
}
