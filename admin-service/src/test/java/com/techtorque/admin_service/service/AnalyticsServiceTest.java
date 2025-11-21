package com.techtorque.admin_service.service;

import com.techtorque.admin_service.dto.response.DashboardAnalyticsResponse;
import com.techtorque.admin_service.dto.response.SystemMetricsResponse;
import com.techtorque.admin_service.service.impl.AnalyticsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private WebClient paymentServiceWebClient;

    @Mock
    private WebClient appointmentServiceWebClient;

    @Mock
    private WebClient projectServiceWebClient;

    @Mock
    private WebClient timeLoggingServiceWebClient;

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    @Test
    void testGetDashboardAnalytics_Success() {
        DashboardAnalyticsResponse response = analyticsService.getDashboardAnalytics("MONTHLY");

        assertThat(response).isNotNull();
        assertThat(response.getKpis()).isNotNull();
        assertThat(response.getKpis().getTotalActiveServices()).isGreaterThan(0);
        assertThat(response.getRevenue()).isNotNull();
        assertThat(response.getServiceStats()).isNotNull();
        assertThat(response.getAppointmentStats()).isNotNull();
    }

    @Test
    void testGetDashboardAnalytics_WeeklyPeriod() {
        DashboardAnalyticsResponse response = analyticsService.getDashboardAnalytics("WEEKLY");

        assertThat(response).isNotNull();
        assertThat(response.getKpis()).isNotNull();
    }

    @Test
    void testGetSystemMetrics_Success() {
        SystemMetricsResponse response = analyticsService.getSystemMetrics();

        assertThat(response).isNotNull();
    }
}
