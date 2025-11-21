package com.techtorque.admin_service.controller;

import com.techtorque.admin_service.dto.response.ServiceTypeResponse;
import com.techtorque.admin_service.service.AdminServiceConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PublicServiceTypeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminServiceConfigService adminServiceConfigService;

    private ServiceTypeResponse activeServiceType;

    @BeforeEach
    void setUp() {
        activeServiceType = ServiceTypeResponse.builder()
                .id("1")
                .name("Plumbing")
                .description("Professional plumbing services")
                .category("HOME_REPAIR")
                .basePriceLKR(BigDecimal.valueOf(5000.00))
                .estimatedDurationMinutes(120)
                .active(true)
                .requiresApproval(false)
                .dailyCapacity(10)
                .skillLevel("INTERMEDIATE")
                .build();
    }

    @Test
    @WithMockUser(username = "customer@test.com", roles = "CUSTOMER")
    void testGetActiveServiceTypes_Success() throws Exception {
        List<ServiceTypeResponse> serviceTypes = Arrays.asList(activeServiceType);
        when(adminServiceConfigService.getAllServiceTypes(true)).thenReturn(serviceTypes);

        mockMvc.perform(get("/public/service-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Plumbing"))
                .andExpect(jsonPath("$[0].active").value(true));

        verify(adminServiceConfigService, times(1)).getAllServiceTypes(true);
    }

    @Test
    @WithMockUser(username = "customer@test.com", roles = "CUSTOMER")
    void testGetServiceTypeById_Success() throws Exception {
        when(adminServiceConfigService.getServiceTypeById("1")).thenReturn(activeServiceType);

        mockMvc.perform(get("/public/service-types/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Plumbing"));

        verify(adminServiceConfigService, times(1)).getServiceTypeById("1");
    }

    @Test
    @WithMockUser(username = "service@test.com", roles = "SERVICE_PROVIDER")
    void testGetActiveServiceTypes_AsServiceProvider() throws Exception {
        List<ServiceTypeResponse> serviceTypes = Arrays.asList(activeServiceType);
        when(adminServiceConfigService.getAllServiceTypes(anyBoolean())).thenReturn(serviceTypes);

        mockMvc.perform(get("/public/service-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(adminServiceConfigService, times(1)).getAllServiceTypes(true);
    }
}
