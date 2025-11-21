package com.techtorque.admin_service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techtorque.admin_service.dto.request.CreateSystemConfigRequest;
import com.techtorque.admin_service.dto.request.UpdateSystemConfigRequest;
import com.techtorque.admin_service.entity.SystemConfiguration;
import com.techtorque.admin_service.repository.SystemConfigurationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SystemConfigurationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SystemConfigurationRepository configurationRepository;

    @BeforeEach
    void setUp() {
        configurationRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCompleteConfigurationLifecycle() throws Exception {
        // Create configuration
        CreateSystemConfigRequest createRequest = new CreateSystemConfigRequest();
        createRequest.setConfigKey("BUSINESS_HOURS_OPEN");
        createRequest.setConfigValue("08:00");
        createRequest.setDescription("Business opening time");
        createRequest.setCategory("BUSINESS_HOURS");
        createRequest.setDataType("TIME");

        mockMvc.perform(post("/admin/config")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.configKey").value("BUSINESS_HOURS_OPEN"))
                .andExpect(jsonPath("$.data.configValue").value("08:00"));

        // Verify in database
        SystemConfiguration saved = configurationRepository.findByConfigKey("BUSINESS_HOURS_OPEN").orElse(null);
        assertThat(saved).isNotNull();
        assertThat(saved.getConfigValue()).isEqualTo("08:00");

        // Get configuration by key
        mockMvc.perform(get("/admin/config/{key}", "BUSINESS_HOURS_OPEN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.configKey").value("BUSINESS_HOURS_OPEN"));

        // Update configuration
        UpdateSystemConfigRequest updateRequest = new UpdateSystemConfigRequest();
        updateRequest.setConfigValue("09:00");
        updateRequest.setDescription("Updated opening time");

        mockMvc.perform(put("/admin/config/{key}", "BUSINESS_HOURS_OPEN")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.configValue").value("09:00"));

        // Verify update in database
        SystemConfiguration updated = configurationRepository.findByConfigKey("BUSINESS_HOURS_OPEN").orElse(null);
        assertThat(updated).isNotNull();
        assertThat(updated.getConfigValue()).isEqualTo("09:00");

        // Get configurations by category
        mockMvc.perform(get("/admin/config/category/{category}", "BUSINESS_HOURS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].category").value("BUSINESS_HOURS"));

        // List all configurations
        mockMvc.perform(get("/admin/config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());

        // Delete configuration
        mockMvc.perform(delete("/admin/config/{key}", "BUSINESS_HOURS_OPEN")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Verify deletion
        SystemConfiguration deleted = configurationRepository.findByConfigKey("BUSINESS_HOURS_OPEN").orElse(null);
        assertThat(deleted).isNull();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateMultipleConfigurationsInCategory() throws Exception {
        // Create opening time
        CreateSystemConfigRequest openRequest = new CreateSystemConfigRequest();
        openRequest.setConfigKey("BUSINESS_HOURS_OPEN");
        openRequest.setConfigValue("08:00");
        openRequest.setDescription("Business opening time");
        openRequest.setCategory("BUSINESS_HOURS");
        openRequest.setDataType("TIME");

        mockMvc.perform(post("/admin/config")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(openRequest)))
                .andExpect(status().isOk());

        // Create closing time
        CreateSystemConfigRequest closeRequest = new CreateSystemConfigRequest();
        closeRequest.setConfigKey("BUSINESS_HOURS_CLOSE");
        closeRequest.setConfigValue("18:00");
        closeRequest.setDescription("Business closing time");
        closeRequest.setCategory("BUSINESS_HOURS");
        closeRequest.setDataType("TIME");

        mockMvc.perform(post("/admin/config")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(closeRequest)))
                .andExpect(status().isOk());

        // Get all configs in category
        mockMvc.perform(get("/admin/config/category/{category}", "BUSINESS_HOURS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateDuplicateConfiguration_ShouldFail() throws Exception {
        CreateSystemConfigRequest createRequest = new CreateSystemConfigRequest();
        createRequest.setConfigKey("DUPLICATE_KEY");
        createRequest.setConfigValue("value1");
        createRequest.setDescription("First config");
        createRequest.setCategory("GENERAL");
        createRequest.setDataType("STRING");

        // Create first config
        mockMvc.perform(post("/admin/config")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk());

        // Try to create duplicate
        mockMvc.perform(post("/admin/config")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().is4xxClientError());
    }
}
