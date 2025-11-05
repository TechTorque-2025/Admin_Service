package com.techtorque.admin_service.service;

import com.techtorque.admin_service.dto.request.CreateSystemConfigRequest;
import com.techtorque.admin_service.dto.request.UpdateSystemConfigRequest;
import com.techtorque.admin_service.dto.response.SystemConfigurationResponse;

import java.util.List;

/**
 * Service interface for system configuration management
 */
public interface SystemConfigurationService {
    
    SystemConfigurationResponse createConfig(CreateSystemConfigRequest request, String createdBy);
    
    SystemConfigurationResponse updateConfig(String key, UpdateSystemConfigRequest request, String updatedBy);
    
    SystemConfigurationResponse getConfig(String key);
    
    List<SystemConfigurationResponse> getAllConfigs();
    
    List<SystemConfigurationResponse> getConfigsByCategory(String category);
    
    void deleteConfig(String key, String deletedBy);
    
    String getConfigValue(String key);
    
    void setConfigValue(String key, String value, String updatedBy);
}
