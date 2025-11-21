package com.techtorque.admin_service.controller;

import com.techtorque.admin_service.dto.response.DashboardAnalyticsResponse;
import com.techtorque.admin_service.dto.response.SystemMetricsResponse;
import com.techtorque.admin_service.service.AnalyticsService;
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
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminAnalyticsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnalyticsService analyticsService;

    private DashboardAnalyticsResponse dashboardResponse;
    private SystemMetricsResponse systemMetrics;

    @BeforeEach
    void setUp() {
        DashboardAnalyticsResponse.KpiData kpis = DashboardAnalyticsResponse.KpiData.builder()
                .totalActiveServices(12)
                .completedServicesToday(5)
                .revenueToday(BigDecimal.valueOf(15000.00))
                .build();

        dashboardResponse = DashboardAnalyticsResponse.builder()
                .kpis(kpis)
                .build();

        systemMetrics = SystemMetricsResponse.builder()
                .activeServices(50)
                .totalServices(100)
                .completionRate(0.85)
                .systemUptime(99.9)
                .lastUpdated(LocalDateTime.now())
                .build();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetDashboardAnalytics_Success() throws Exception {
        when(analyticsService.getDashboardAnalytics(anyString())).thenReturn(dashboardResponse);

        mockMvc.perform(get("/admin/analytics/dashboard")
                        .param("period", "MONTHLY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(analyticsService, times(1)).getDashboardAnalytics("MONTHLY");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetSystemMetrics_Success() throws Exception {
        when(analyticsService.getSystemMetrics()).thenReturn(systemMetrics);

        mockMvc.perform(get("/admin/analytics/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(analyticsService, times(1)).getSystemMetrics();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetDashboardAnalytics_DefaultPeriod() throws Exception {
        when(analyticsService.getDashboardAnalytics(anyString())).thenReturn(dashboardResponse);

        mockMvc.perform(get("/admin/analytics/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(analyticsService, times(1)).getDashboardAnalytics(anyString());
    }
}
