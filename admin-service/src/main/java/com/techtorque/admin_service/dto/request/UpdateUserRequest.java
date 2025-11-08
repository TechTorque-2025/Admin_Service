// ========================================
// UpdateUserRequest.java
// ========================================

package com.techtorque.admin_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Request DTO for updating user information
 * Used by: PUT /admin/users/{userId}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

    @Pattern(regexp = "ADMIN|EMPLOYEE|CUSTOMER", message = "Role must be ADMIN, EMPLOYEE, or CUSTOMER")
    private String role;

    private Boolean active;

    @Size(max = 20, message = "Maximum 20 permissions allowed")
    private List<String> permissions;

    @Size(max = 100)
    private String department;
}