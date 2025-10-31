// ========================================
// UpdateServiceTypeRequest.java
// ========================================

package com.techtorque.admin_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * Request DTO for updating service type
 * Used by: PUT /admin/service-types/{typeId}
 * All fields are optional for partial updates
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateServiceTypeRequest {

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2)
    private BigDecimal price;

    @Min(value = 15, message = "Duration must be at least 15 minutes")
    @Max(value = 480, message = "Duration must not exceed 480 minutes")
    private Integer durationMinutes;

    private Boolean active;

    @Min(value = 1, message = "Daily capacity must be at least 1")
    private Integer dailyCapacity;

    @Pattern(regexp = "BASIC|INTERMEDIATE|ADVANCED")
    private String skillLevel;

    @Size(max = 500)
    private String iconUrl;
}