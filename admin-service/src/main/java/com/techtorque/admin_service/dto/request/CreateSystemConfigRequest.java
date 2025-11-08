package com.techtorque.admin_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating system configuration
 * Used by: POST /admin/config
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSystemConfigRequest {

    @NotBlank(message = "Config key is required")
    @Size(max = 100, message = "Config key must be less than 100 characters")
    @Pattern(regexp = "^[A-Z][A-Z0-9_]*$", message = "Config key must be uppercase with underscores only")
    private String configKey;

    @NotBlank(message = "Config value is required")
    private String configValue;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @NotBlank(message = "Category is required")
    @Pattern(regexp = "BUSINESS_HOURS|SCHEDULING|NOTIFICATIONS|PAYMENT|GENERAL|SECURITY",
            message = "Invalid category")
    private String category;

    @NotBlank(message = "Data type is required")
    @Pattern(regexp = "STRING|NUMBER|BOOLEAN|JSON|TIME|DATE",
            message = "Data type must be STRING, NUMBER, BOOLEAN, JSON, TIME, or DATE")
    private String dataType;
}
