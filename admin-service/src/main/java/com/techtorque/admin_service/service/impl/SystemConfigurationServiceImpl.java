package com.techtorque.admin_service.service.impl;

import com.techtorque.admin_service.dto.request.CreateSystemConfigRequest;
import com.techtorque.admin_service.dto.request.UpdateSystemConfigRequest;
import com.techtorque.admin_service.dto.response.SystemConfigurationResponse;
import com.techtorque.admin_service.entity.SystemConfiguration;
import com.techtorque.admin_service.exception.ResourceNotFoundException;
import com.techtorque.admin_service.repository.SystemConfigurationRepository;
import com.techtorque.admin_service.service.SystemConfigurationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SystemConfigurationServiceImpl implements SystemConfigurationService {

    private final SystemConfigurationRepository configurationRepository;

    @Override
    public SystemConfigurationResponse createConfig(CreateSystemConfigRequest request, String createdBy) {
        log.info("Creating system configuration: {} by user: {}", request.getConfigKey(), createdBy);

        // Check if config already exists
        if (configurationRepository.findByConfigKey(request.getConfigKey()).isPresent()) {
            throw new IllegalArgumentException("Configuration with key '" + request.getConfigKey() + "' already exists");
        }

        SystemConfiguration config = SystemConfiguration.builder()
                .configKey(request.getConfigKey())
                .configValue(request.getConfigValue())
                .description(request.getDescription())
                .category(request.getCategory())
                .dataType(request.getDataType())
                .lastModifiedBy(createdBy)
                .updatedAt(LocalDateTime.now())
                .build();

        SystemConfiguration saved = configurationRepository.save(config);
        log.info("System configuration created: {}", saved.getId());
        
        return convertToResponse(saved);
    }

    @Override
    public SystemConfigurationResponse updateConfig(String key, UpdateSystemConfigRequest request, String updatedBy) {
        log.info("Updating system configuration: {} by user: {}", key, updatedBy);

        SystemConfiguration config = configurationRepository.findByConfigKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("System Configuration", key));

        config.setConfigValue(request.getConfigValue());
        if (request.getDescription() != null) {
            config.setDescription(request.getDescription());
        }
        config.setLastModifiedBy(updatedBy);
        config.setUpdatedAt(LocalDateTime.now());

        SystemConfiguration updated = configurationRepository.save(config);
        log.info("System configuration updated: {}", key);
        
        return convertToResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public SystemConfigurationResponse getConfig(String key) {
        log.info("Fetching system configuration: {}", key);

        SystemConfiguration config = configurationRepository.findByConfigKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("System Configuration", key));

        return convertToResponse(config);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemConfigurationResponse> getAllConfigs() {
        log.info("Fetching all system configurations");

        return configurationRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SystemConfigurationResponse> getConfigsByCategory(String category) {
        log.info("Fetching system configurations by category: {}", category);

        return configurationRepository.findByCategory(category).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteConfig(String key, String deletedBy) {
        log.info("Deleting system configuration: {} by user: {}", key, deletedBy);

        SystemConfiguration config = configurationRepository.findByConfigKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("System Configuration", key));

        configurationRepository.delete(config);
        log.info("System configuration deleted: {}", key);
    }

    @Override
    @Transactional(readOnly = true)
    public String getConfigValue(String key) {
        return configurationRepository.findByConfigKey(key)
                .map(SystemConfiguration::getConfigValue)
                .orElse(null);
    }

    @Override
    public void setConfigValue(String key, String value, String updatedBy) {
        log.info("Setting config value for key: {}", key);

        SystemConfiguration config = configurationRepository.findByConfigKey(key)
                .orElseThrow(() -> new ResourceNotFoundException("System Configuration", key));

        config.setConfigValue(value);
        config.setLastModifiedBy(updatedBy);
        config.setUpdatedAt(LocalDateTime.now());

        configurationRepository.save(config);
    }

    private SystemConfigurationResponse convertToResponse(SystemConfiguration config) {
        return SystemConfigurationResponse.builder()
                .id(config.getId())
                .configKey(config.getConfigKey())
                .configValue(config.getConfigValue())
                .description(config.getDescription())
                .category(config.getCategory())
                .dataType(config.getDataType())
                .lastModifiedBy(config.getLastModifiedBy())
                .updatedAt(config.getUpdatedAt())
                .build();
    }
}
