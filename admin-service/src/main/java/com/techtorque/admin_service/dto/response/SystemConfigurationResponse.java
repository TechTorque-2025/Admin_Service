package com.techtorque.admin_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Response DTO for system configuration
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemConfigurationResponse {
    private String id;
    private String configKey;
    private String configValue;
    private String description;
    private String category;
    private String dataType;
    private String lastModifiedBy;
    private LocalDateTime updatedAt;
}
