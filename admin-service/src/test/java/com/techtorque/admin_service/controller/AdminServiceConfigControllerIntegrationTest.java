package com.techtorque.admin_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techtorque.admin_service.dto.request.CreateServiceTypeRequest;
import com.techtorque.admin_service.dto.response.ServiceTypeResponse;
import com.techtorque.admin_service.service.AdminServiceConfigService;
import com.techtorque.admin_service.service.AuditLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminServiceConfigControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminServiceConfigService serviceConfigService;

    @MockBean
    private AuditLogService auditLogService;

    private ServiceTypeResponse serviceTypeResponse;

    @BeforeEach
    void setUp() {
        serviceTypeResponse = new ServiceTypeResponse();
        serviceTypeResponse.setId(UUID.randomUUID().toString());
        serviceTypeResponse.setName("Oil Change");
        serviceTypeResponse.setDescription("Standard oil change service");
        serviceTypeResponse.setCategory("MAINTENANCE");
        serviceTypeResponse.setBasePriceLKR(new BigDecimal("3500.00"));
        serviceTypeResponse.setEstimatedDurationMinutes(30);
        serviceTypeResponse.setSkillLevel("BASIC");
        serviceTypeResponse.setDailyCapacity(20);
        serviceTypeResponse.setActive(true);
        serviceTypeResponse.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "admin@test.com")
    void testListServiceTypes_Success() throws Exception {
        when(serviceConfigService.getAllServiceTypes(anyBoolean())).thenReturn(Arrays.asList(serviceTypeResponse));

        mockMvc.perform(get("/admin/service-types")
                        .param("activeOnly", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());

        verify(serviceConfigService, times(1)).getAllServiceTypes(true);
    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "admin@test.com")
    void testGetServiceType_Success() throws Exception {
        when(serviceConfigService.getServiceTypeById(anyString())).thenReturn(serviceTypeResponse);

        mockMvc.perform(get("/admin/service-types/{typeId}", serviceTypeResponse.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(serviceConfigService, times(1)).getServiceTypeById(anyString());
    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "admin@test.com")
    void testAddServiceType_Success() throws Exception {
        CreateServiceTypeRequest createRequest = new CreateServiceTypeRequest();
        createRequest.setName("Oil Change");
        createRequest.setDescription("Standard oil change service");
        createRequest.setCategory("MAINTENANCE");
        createRequest.setPrice(new BigDecimal("3500.00"));
        createRequest.setDurationMinutes(30);

        when(serviceConfigService.createServiceType(any(CreateServiceTypeRequest.class), anyString()))
                .thenReturn(serviceTypeResponse);
        doNothing().when(auditLogService).logAction(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString());

        mockMvc.perform(post("/admin/service-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(serviceConfigService, times(1)).createServiceType(any(CreateServiceTypeRequest.class), anyString());
    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "admin@test.com")
    void testDeleteServiceType_Success() throws Exception {
        doNothing().when(serviceConfigService).deleteServiceType(anyString(), anyString());
        doNothing().when(auditLogService).logAction(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString());

        mockMvc.perform(delete("/admin/service-types/{typeId}", serviceTypeResponse.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(serviceConfigService, times(1)).deleteServiceType(anyString(), anyString());
    }
}
