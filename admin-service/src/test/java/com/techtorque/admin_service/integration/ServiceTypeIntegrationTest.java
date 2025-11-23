package com.techtorque.admin_service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techtorque.admin_service.dto.request.CreateServiceTypeRequest;
import com.techtorque.admin_service.dto.request.UpdateServiceTypeRequest;
import com.techtorque.admin_service.entity.ServiceType;
import com.techtorque.admin_service.repository.ServiceTypeRepository;
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

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ServiceTypeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    @BeforeEach
    void setUp() {
        serviceTypeRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCompleteServiceTypeLifecycle() throws Exception {
        // Create service type
        CreateServiceTypeRequest createRequest = new CreateServiceTypeRequest();
        createRequest.setName("Oil Change");
        createRequest.setDescription("Standard oil change service");
        createRequest.setCategory("MAINTENANCE");
        createRequest.setPrice(new BigDecimal("3500.00"));
        createRequest.setDurationMinutes(30);
        createRequest.setSkillLevel("BASIC");
        createRequest.setDailyCapacity(20);
        createRequest.setRequiresApproval(false);

        String createResponse = mockMvc.perform(post("/admin/service-types")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Oil Change"))
                .andReturn().getResponse().getContentAsString();

        String serviceId = objectMapper.readTree(createResponse).get("data").get("id").asText();

        // Verify in database
        ServiceType saved = serviceTypeRepository.findById(serviceId).orElse(null);
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo("Oil Change");

        // Get service type
        mockMvc.perform(get("/admin/service-types/{typeId}", serviceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Oil Change"));

        // Update service type
        UpdateServiceTypeRequest updateRequest = new UpdateServiceTypeRequest();
        updateRequest.setDescription("Updated oil change service");
        updateRequest.setPrice(new BigDecimal("4000.00"));

        mockMvc.perform(put("/admin/service-types/{typeId}", serviceId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.description").value("Updated oil change service"));

        // Verify update in database
        ServiceType updated = serviceTypeRepository.findById(serviceId).orElse(null);
        assertThat(updated).isNotNull();
        assertThat(updated.getDescription()).isEqualTo("Updated oil change service");
        assertThat(updated.getPrice()).isEqualByComparingTo(new BigDecimal("4000.00"));

        // List service types
        mockMvc.perform(get("/admin/service-types")
                        .param("activeOnly", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].name").value("Oil Change"));

        // Delete service type
        mockMvc.perform(delete("/admin/service-types/{typeId}", serviceId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // Verify deletion (should be marked inactive)
        ServiceType deleted = serviceTypeRepository.findById(serviceId).orElse(null);
        if (deleted != null) {
            assertThat(deleted.getActive()).isFalse();
        }
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateDuplicateServiceType_ShouldFail() throws Exception {
        // Create first service type
        CreateServiceTypeRequest createRequest = new CreateServiceTypeRequest();
        createRequest.setName("Brake Service");
        createRequest.setDescription("Brake system service");
        createRequest.setCategory("REPAIR");
        createRequest.setPrice(new BigDecimal("5000.00"));
        createRequest.setDurationMinutes(60);
        createRequest.setSkillLevel("INTERMEDIATE");
        createRequest.setDailyCapacity(10);

        mockMvc.perform(post("/admin/service-types")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk());

        // Try to create duplicate
        mockMvc.perform(post("/admin/service-types")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetNonExistentServiceType_ShouldFail() throws Exception {
        mockMvc.perform(get("/admin/service-types/{typeId}", "non-existent-id"))
                .andExpect(status().is4xxClientError());
    }
}
