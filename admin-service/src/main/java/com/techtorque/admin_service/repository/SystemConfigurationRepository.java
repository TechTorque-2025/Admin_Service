// ========================================
// SystemConfigurationRepository.java
// ========================================

package com.techtorque.admin_service.repository;

import com.techtorque.admin_service.entity.SystemConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for SystemConfiguration entity
 * File Location: src/main/java/com/techtorque/admin_service/repository/SystemConfigurationRepository.java
 */
@Repository
public interface SystemConfigurationRepository extends JpaRepository<SystemConfiguration, String> {

    /**
     * Find configuration by key
     */
    Optional<SystemConfiguration> findByConfigKey(String configKey);

    /**
     * Find configurations by category
     */
    List<SystemConfiguration> findByCategory(String category);

    /**
     * Find configurations by data type
     */
    List<SystemConfiguration> findByDataType(String dataType);

    /**
     * Check if configuration key exists
     */
    boolean existsByConfigKey(String configKey);

    /**
     * Delete configuration by key
     */
    void deleteByConfigKey(String configKey);
}