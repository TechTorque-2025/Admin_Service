package com.techtorque.admin_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techtorque.admin_service.dto.request.GenerateReportRequest;
import com.techtorque.admin_service.dto.response.ReportResponse;
import com.techtorque.admin_service.service.AdminReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminReportControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminReportService adminReportService;

    private ReportResponse testReport;

    @BeforeEach
    void setUp() {
        testReport = ReportResponse.builder()
                .reportId("report-123")
                .type("SERVICE_PERFORMANCE")
                .title("Service Performance Report")
                .status("COMPLETED")
                .generatedBy("admin@test.com")
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN", username = "admin@test.com")
    void testGenerateReport_Success() throws Exception {
        GenerateReportRequest request = new GenerateReportRequest();
        request.setType("SERVICE_PERFORMANCE");
        request.setFromDate(LocalDate.of(2024, 1, 1));
        request.setToDate(LocalDate.of(2024, 1, 31));
        request.setFormat("PDF");

        when(adminReportService.generateReport(any(GenerateReportRequest.class), anyString()))
                .thenReturn(testReport);

        mockMvc.perform(post("/admin/reports/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(adminReportService, times(1)).generateReport(any(GenerateReportRequest.class), anyString());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllReports_Success() throws Exception {
        when(adminReportService.getAllReports(anyInt(), anyInt())).thenReturn(Arrays.asList(testReport));

        mockMvc.perform(get("/admin/reports")
                        .param("page", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());

        verify(adminReportService, times(1)).getAllReports(0, 10);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetReportById_Success() throws Exception {
        when(adminReportService.getReportById(anyString())).thenReturn(testReport);

        mockMvc.perform(get("/admin/reports/{reportId}", "report-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(adminReportService, times(1)).getReportById("report-123");
    }
}
