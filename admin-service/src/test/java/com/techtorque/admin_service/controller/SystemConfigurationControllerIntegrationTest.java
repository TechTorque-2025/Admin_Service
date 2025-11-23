package com.techtorque.admin_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techtorque.admin_service.dto.request.CreateSystemConfigRequest;
import com.techtorque.admin_service.dto.response.SystemConfigurationResponse;
import com.techtorque.admin_service.service.SystemConfigurationService;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SystemConfigurationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SystemConfigurationService configurationService;

    private SystemConfigurationResponse configResponse;

    @BeforeEach
    void setUp() {
        configResponse = new SystemConfigurationResponse();
        configResponse.setId(UUID.randomUUID().toString());
        configResponse.setConfigKey("business.hours.open");
        configResponse.setConfigValue("08:00");
        configResponse.setDescription("Business opening time");
        configResponse.setCategory("BUSINESS_HOURS");
        configResponse.setDataType("TIME");
        configResponse.setLastModifiedBy("admin@test.com");
        configResponse.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "admin@test.com")
    void testGetAllConfigurations_Success() throws Exception {
        when(configurationService.getAllConfigs()).thenReturn(Arrays.asList(configResponse));

        mockMvc.perform(get("/admin/config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());

        verify(configurationService, times(1)).getAllConfigs();
    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "admin@test.com")
    void testGetConfigurationByKey_Success() throws Exception {
        when(configurationService.getConfig(anyString())).thenReturn(configResponse);

        mockMvc.perform(get("/admin/config/{key}", "business.hours.open"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(configurationService, times(1)).getConfig("business.hours.open");
    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "admin@test.com")
    void testCreateConfiguration_Success() throws Exception {
        CreateSystemConfigRequest createRequest = new CreateSystemConfigRequest();
        createRequest.setConfigKey("BUSINESS_HOURS_OPEN");
        createRequest.setConfigValue("08:00");
        createRequest.setDescription("Business opening time");
        createRequest.setCategory("BUSINESS_HOURS");
        createRequest.setDataType("TIME");

        when(configurationService.createConfig(any(CreateSystemConfigRequest.class), anyString()))
                .thenReturn(configResponse);

        mockMvc.perform(post("/admin/config")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(configurationService, times(1)).createConfig(any(CreateSystemConfigRequest.class), anyString());
    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "admin@test.com")
    void testDeleteConfiguration_Success() throws Exception {
        doNothing().when(configurationService).deleteConfig(anyString(), anyString());

        mockMvc.perform(delete("/admin/config/{key}", "business.hours.open"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(configurationService, times(1)).deleteConfig(anyString(), anyString());
    }
}
