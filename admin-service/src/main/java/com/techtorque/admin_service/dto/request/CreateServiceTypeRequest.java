package com.techtorque.admin_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * Request DTO for creating a new service type
 * Used by: POST /admin/service-types
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateServiceTypeRequest {

    @NotBlank(message = "Service name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @NotBlank(message = "Category is required")
    @Pattern(regexp = "MAINTENANCE|REPAIR|MODIFICATION|INSPECTION",
            message = "Category must be MAINTENANCE, REPAIR, MODIFICATION, or INSPECTION")
    private String category;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price must have max 8 integer digits and 2 decimal places")
    private BigDecimal price;

    @NotNull(message = "Duration is required")
    @Min(value = 15, message = "Duration must be at least 15 minutes")
    @Max(value = 480, message = "Duration must not exceed 8 hours (480 minutes)")
    private Integer durationMinutes;

    // Optional fields
    private Boolean requiresApproval = false;

    @Min(value = 1, message = "Daily capacity must be at least 1")
    @Max(value = 50, message = "Daily capacity cannot exceed 50")
    private Integer dailyCapacity;

    @Pattern(regexp = "BASIC|INTERMEDIATE|ADVANCED",
            message = "Skill level must be BASIC, INTERMEDIATE, or ADVANCED")
    private String skillLevel;

    @Size(max = 500, message = "Icon URL must be less than 500 characters")
    private String iconUrl;
}