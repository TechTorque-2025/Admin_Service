package com.techtorque.admin_service.controller;

import com.techtorque.admin_service.dto.request.AuditLogSearchRequest;
import com.techtorque.admin_service.dto.response.AuditLogResponse;
import com.techtorque.admin_service.dto.response.PaginatedResponse;
import com.techtorque.admin_service.service.AuditLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuditLogControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditLogService auditLogService;

    private AuditLogResponse auditLogResponse;
    private PaginatedResponse<AuditLogResponse> paginatedResponse;

    @BeforeEach
    void setUp() {
        auditLogResponse = new AuditLogResponse();
        auditLogResponse.setLogId(UUID.randomUUID().toString());
        auditLogResponse.setUserId("user-123");
        auditLogResponse.setUsername("admin@test.com");
        auditLogResponse.setUserRole("ADMIN");
        auditLogResponse.setAction("CREATE");
        auditLogResponse.setEntityType("SERVICE_TYPE");
        auditLogResponse.setEntityId("service-123");
        auditLogResponse.setDescription("Created service type");
        auditLogResponse.setIpAddress("192.168.1.1");
        auditLogResponse.setSuccess(true);
        auditLogResponse.setTimestamp(LocalDateTime.now());

        paginatedResponse = new PaginatedResponse<>();
        paginatedResponse.setData(Arrays.asList(auditLogResponse));
        paginatedResponse.setPage(0);
        paginatedResponse.setLimit(10);
        paginatedResponse.setTotal(1L);
        paginatedResponse.setTotalPages(1);
    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "admin@test.com")
    void testSearchAuditLogs_Success() throws Exception {
        when(auditLogService.searchAuditLogs(any(AuditLogSearchRequest.class))).thenReturn(paginatedResponse);

        mockMvc.perform(get("/admin/audit-logs")
                        .param("action", "CREATE")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.data").isArray());

        verify(auditLogService, times(1)).searchAuditLogs(any(AuditLogSearchRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "admin@test.com")
    void testGetAuditLogById_Success() throws Exception {
        when(auditLogService.getAuditLogById(anyString())).thenReturn(auditLogResponse);

        mockMvc.perform(get("/admin/audit-logs/{logId}", auditLogResponse.getLogId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(auditLogService, times(1)).getAuditLogById(anyString());
    }
}
