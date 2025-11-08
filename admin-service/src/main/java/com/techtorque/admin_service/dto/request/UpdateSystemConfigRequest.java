package com.techtorque.admin_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating system configuration
 * Used by: PUT /admin/config
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSystemConfigRequest {

    @NotBlank(message = "Config value is required")
    private String configValue;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;
}
