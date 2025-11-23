package com.techtorque.admin_service.repository;

import com.techtorque.admin_service.entity.SystemConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SystemConfigurationRepositoryTest {

    @Autowired
    private SystemConfigurationRepository configurationRepository;

    private SystemConfiguration testConfig;

    @BeforeEach
    void setUp() {
        configurationRepository.deleteAll();

        testConfig = new SystemConfiguration();
        testConfig.setConfigKey("business.hours.open");
        testConfig.setConfigValue("08:00");
        testConfig.setDescription("Business opening time");
        testConfig.setCategory("BUSINESS_HOURS");
        testConfig.setDataType("TIME");
        testConfig.setLastModifiedBy("admin@test.com");
    }

    @Test
    void testSaveConfiguration() {
        SystemConfiguration saved = configurationRepository.save(testConfig);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(testConfig.getId());
        assertThat(saved.getConfigKey()).isEqualTo("business.hours.open");
        assertThat(saved.getConfigValue()).isEqualTo("08:00");
    }

    @Test
    void testFindByConfigKey() {
        configurationRepository.save(testConfig);

        Optional<SystemConfiguration> found = configurationRepository.findByConfigKey("business.hours.open");

        assertThat(found).isPresent();
        assertThat(found.get().getConfigValue()).isEqualTo("08:00");
    }

    @Test
    void testFindByCategory() {
        SystemConfiguration closeTime = new SystemConfiguration();
        closeTime.setConfigKey("business.hours.close");
        closeTime.setConfigValue("18:00");
        closeTime.setDescription("Business closing time");
        closeTime.setCategory("BUSINESS_HOURS");
        closeTime.setDataType("TIME");

        SystemConfiguration notificationConfig = new SystemConfiguration();
        notificationConfig.setConfigKey("notification.email.enabled");
        notificationConfig.setConfigValue("true");
        notificationConfig.setDescription("Email notifications enabled");
        notificationConfig.setCategory("NOTIFICATIONS");
        notificationConfig.setDataType("BOOLEAN");

        configurationRepository.save(testConfig);
        configurationRepository.save(closeTime);
        configurationRepository.save(notificationConfig);

        List<SystemConfiguration> businessHoursConfigs = configurationRepository.findByCategory("BUSINESS_HOURS");

        assertThat(businessHoursConfigs).hasSize(2);
        assertThat(businessHoursConfigs).extracting("category").containsOnly("BUSINESS_HOURS");
    }

    @Test
    void testFindByDataType() {
        SystemConfiguration booleanConfig = new SystemConfiguration();
        booleanConfig.setConfigKey("feature.enabled");
        booleanConfig.setConfigValue("true");
        booleanConfig.setDescription("Feature flag");
        booleanConfig.setCategory("GENERAL");
        booleanConfig.setDataType("BOOLEAN");

        configurationRepository.save(testConfig);
        configurationRepository.save(booleanConfig);

        List<SystemConfiguration> timeConfigs = configurationRepository.findByDataType("TIME");

        assertThat(timeConfigs).hasSize(1);
        assertThat(timeConfigs.get(0).getConfigKey()).isEqualTo("business.hours.open");
    }

    @Test
    void testUpdateConfiguration() {
        configurationRepository.save(testConfig);

        testConfig.setConfigValue("09:00");
        testConfig.setLastModifiedBy("admin2@test.com");
        SystemConfiguration updated = configurationRepository.save(testConfig);

        assertThat(updated.getConfigValue()).isEqualTo("09:00");
        assertThat(updated.getLastModifiedBy()).isEqualTo("admin2@test.com");
    }

    @Test
    void testDeleteConfiguration() {
        configurationRepository.save(testConfig);
        String configId = testConfig.getId();

        configurationRepository.deleteById(configId);

        Optional<SystemConfiguration> deleted = configurationRepository.findById(configId);
        assertThat(deleted).isEmpty();
    }

    @Test
    void testFindAll() {
        SystemConfiguration config2 = new SystemConfiguration();
        config2.setConfigKey("max.appointments.per.day");
        config2.setConfigValue("50");
        config2.setDescription("Maximum appointments");
        config2.setCategory("SCHEDULING");
        config2.setDataType("NUMBER");

        configurationRepository.save(testConfig);
        configurationRepository.save(config2);

        List<SystemConfiguration> allConfigs = configurationRepository.findAll();

        assertThat(allConfigs).hasSize(2);
    }
}
