package com.techtorque.admin_service.service;

import com.techtorque.admin_service.dto.request.CreateSystemConfigRequest;
import com.techtorque.admin_service.dto.request.UpdateSystemConfigRequest;
import com.techtorque.admin_service.dto.response.SystemConfigurationResponse;
import com.techtorque.admin_service.entity.SystemConfiguration;
import com.techtorque.admin_service.exception.ResourceNotFoundException;
import com.techtorque.admin_service.repository.SystemConfigurationRepository;
import com.techtorque.admin_service.service.impl.SystemConfigurationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SystemConfigurationServiceTest {

    @Mock
    private SystemConfigurationRepository configurationRepository;

    @InjectMocks
    private SystemConfigurationServiceImpl configurationService;

    private SystemConfiguration testConfig;
    private CreateSystemConfigRequest createRequest;

    @BeforeEach
    void setUp() {
        testConfig = new SystemConfiguration();
        testConfig.setId(UUID.randomUUID().toString());
        testConfig.setConfigKey("business.hours.open");
        testConfig.setConfigValue("08:00");
        testConfig.setDescription("Business opening time");
        testConfig.setCategory("BUSINESS_HOURS");
        testConfig.setDataType("TIME");
        testConfig.setLastModifiedBy("admin@test.com");

        createRequest = new CreateSystemConfigRequest();
        createRequest.setConfigKey("business.hours.open");
        createRequest.setConfigValue("08:00");
        createRequest.setDescription("Business opening time");
        createRequest.setCategory("BUSINESS_HOURS");
        createRequest.setDataType("TIME");
    }

    @Test
    void testCreateConfiguration_Success() {
        when(configurationRepository.save(any(SystemConfiguration.class))).thenReturn(testConfig);

        SystemConfigurationResponse response = configurationService.createConfig(createRequest, "admin@test.com");

        assertThat(response).isNotNull();
        assertThat(response.getConfigKey()).isEqualTo("business.hours.open");
        assertThat(response.getConfigValue()).isEqualTo("08:00");
        verify(configurationRepository, times(1)).save(any(SystemConfiguration.class));
    }

    @Test
    void testGetAllConfigurations() {
        SystemConfiguration config2 = new SystemConfiguration();
        config2.setId(UUID.randomUUID().toString());
        config2.setConfigKey("business.hours.close");
        config2.setConfigValue("18:00");

        when(configurationRepository.findAll()).thenReturn(Arrays.asList(testConfig, config2));

        List<SystemConfigurationResponse> responses = configurationService.getAllConfigs();

        assertThat(responses).hasSize(2);
        verify(configurationRepository, times(1)).findAll();
    }

    @Test
    void testGetConfigurationByKey_Success() {
        when(configurationRepository.findByConfigKey(anyString())).thenReturn(Optional.of(testConfig));

        SystemConfigurationResponse response = configurationService.getConfig("business.hours.open");

        assertThat(response).isNotNull();
        assertThat(response.getConfigKey()).isEqualTo("business.hours.open");
    }

    @Test
    void testGetConfigurationByKey_NotFound() {
        when(configurationRepository.findByConfigKey(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> configurationService.getConfig("non.existent.key"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testGetConfigurationsByCategory() {
        SystemConfiguration config2 = new SystemConfiguration();
        config2.setId(UUID.randomUUID().toString());
        config2.setConfigKey("business.hours.close");
        config2.setConfigValue("18:00");
        config2.setCategory("BUSINESS_HOURS");

        when(configurationRepository.findByCategory(anyString())).thenReturn(Arrays.asList(testConfig, config2));

        List<SystemConfigurationResponse> responses = configurationService.getConfigsByCategory("BUSINESS_HOURS");

        assertThat(responses).hasSize(2);
        assertThat(responses).allMatch(r -> r.getCategory().equals("BUSINESS_HOURS"));
    }

    @Test
    void testUpdateConfiguration_Success() {
        UpdateSystemConfigRequest updateRequest = new UpdateSystemConfigRequest();
        updateRequest.setConfigValue("09:00");
        updateRequest.setDescription("Updated opening time");

        when(configurationRepository.findByConfigKey(anyString())).thenReturn(Optional.of(testConfig));
        when(configurationRepository.save(any(SystemConfiguration.class))).thenReturn(testConfig);

        SystemConfigurationResponse response = configurationService.updateConfig("business.hours.open", updateRequest, "admin@test.com");

        assertThat(response).isNotNull();
        verify(configurationRepository, times(1)).save(any(SystemConfiguration.class));
    }

    @Test
    void testUpdateConfiguration_NotFound() {
        UpdateSystemConfigRequest updateRequest = new UpdateSystemConfigRequest();
        updateRequest.setConfigValue("09:00");

        when(configurationRepository.findByConfigKey(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> configurationService.updateConfig("non.existent.key", updateRequest, "admin@test.com"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void testDeleteConfiguration_Success() {
        when(configurationRepository.findByConfigKey(anyString())).thenReturn(Optional.of(testConfig));
        doNothing().when(configurationRepository).delete(any(SystemConfiguration.class));

        configurationService.deleteConfig("business.hours.open", "admin@test.com");

        verify(configurationRepository, times(1)).findByConfigKey(anyString());
        verify(configurationRepository, times(1)).delete(any(SystemConfiguration.class));
    }

    @Test
    void testDeleteConfiguration_NotFound() {
        when(configurationRepository.findByConfigKey(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> configurationService.deleteConfig("non.existent.key", "admin@test.com"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
