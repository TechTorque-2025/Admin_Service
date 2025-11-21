package com.techtorque.admin_service.service;

import com.techtorque.admin_service.dto.request.AuditLogSearchRequest;
import com.techtorque.admin_service.dto.response.AuditLogResponse;
import com.techtorque.admin_service.dto.response.PaginatedResponse;
import com.techtorque.admin_service.entity.AuditLog;
import com.techtorque.admin_service.repository.AuditLogRepository;
import com.techtorque.admin_service.service.impl.AuditLogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.techtorque.admin_service.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class AuditLogServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private AuditLogServiceImpl auditLogService;

    private AuditLog testAuditLog;

    @BeforeEach
    void setUp() {
        testAuditLog = new AuditLog();
        testAuditLog.setId(UUID.randomUUID().toString());
        testAuditLog.setUserId("user-123");
        testAuditLog.setUsername("admin@test.com");
        testAuditLog.setUserRole("ADMIN");
        testAuditLog.setAction("CREATE");
        testAuditLog.setEntityType("SERVICE_TYPE");
        testAuditLog.setEntityId("service-123");
        testAuditLog.setDescription("Created service type");
        testAuditLog.setIpAddress("192.168.1.1");
        testAuditLog.setSuccess(true);
        testAuditLog.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testLogAction_Success() {
        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(testAuditLog);

        auditLogService.logAction("user-123", "admin@test.com", "ADMIN", "CREATE",
                                  "SERVICE_TYPE", "service-123", "Created service type");

        verify(auditLogRepository, times(1)).save(any(AuditLog.class));
    }

    @Test
    void testSearchAuditLogs_WithAllFilters() {
        AuditLogSearchRequest searchRequest = new AuditLogSearchRequest();
        searchRequest.setUserId("user-123");
        searchRequest.setAction("CREATE");
        searchRequest.setEntityType("SERVICE_TYPE");
        searchRequest.setPage(0);
        searchRequest.setSize(10);

        // Mock findAll() since the service uses findAll() and filters in memory
        when(auditLogRepository.findAll()).thenReturn(Arrays.asList(testAuditLog));

        PaginatedResponse<AuditLogResponse> result = auditLogService.searchAuditLogs(searchRequest);

        assertThat(result).isNotNull();
        assertThat(result.getData()).isNotEmpty();
        verify(auditLogRepository, times(1)).findAll();
    }

    @Test
    void testSearchAuditLogs_WithUserIdOnly() {
        AuditLogSearchRequest searchRequest = new AuditLogSearchRequest();
        searchRequest.setUserId("user-123");
        searchRequest.setPage(0);
        searchRequest.setSize(10);

        // Mock findAll() since the service uses findAll() and filters in memory
        when(auditLogRepository.findAll()).thenReturn(Arrays.asList(testAuditLog));

        PaginatedResponse<AuditLogResponse> result = auditLogService.searchAuditLogs(searchRequest);

        assertThat(result).isNotNull();
        assertThat(result.getData()).isNotEmpty();
        verify(auditLogRepository, times(1)).findAll();
    }

    @Test
    void testSearchAuditLogs_WithEntityTypeOnly() {
        AuditLogSearchRequest searchRequest = new AuditLogSearchRequest();
        searchRequest.setEntityType("SERVICE_TYPE");
        searchRequest.setPage(0);
        searchRequest.setSize(10);

        // Mock findAll() since the service uses findAll() and filters in memory
        when(auditLogRepository.findAll()).thenReturn(Arrays.asList(testAuditLog));

        PaginatedResponse<AuditLogResponse> result = auditLogService.searchAuditLogs(searchRequest);

        assertThat(result).isNotNull();
        assertThat(result.getData()).isNotEmpty();
        verify(auditLogRepository, times(1)).findAll();
    }

    @Test
    void testGetAuditLogById_Success() {
        when(auditLogRepository.findById(anyString())).thenReturn(Optional.of(testAuditLog));

        AuditLogResponse response = auditLogService.getAuditLogById(testAuditLog.getId());

        assertThat(response).isNotNull();
        assertThat(response.getAction()).isEqualTo("CREATE");
        assertThat(response.getEntityType()).isEqualTo("SERVICE_TYPE");
    }

    @Test
    void testGetAuditLogById_NotFound() {
        when(auditLogRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> auditLogService.getAuditLogById("non-existent-id"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testSearchAuditLogs_EmptyResult() {
        AuditLogSearchRequest searchRequest = new AuditLogSearchRequest();
        searchRequest.setPage(0);
        searchRequest.setSize(10);

        // Mock findAll() to return empty list
        when(auditLogRepository.findAll()).thenReturn(List.of());

        PaginatedResponse<AuditLogResponse> result = auditLogService.searchAuditLogs(searchRequest);

        assertThat(result).isNotNull();
        assertThat(result.getData()).isEmpty();
        assertThat(result.getTotal()).isEqualTo(0L);
        verify(auditLogRepository, times(1)).findAll();
    }
}
