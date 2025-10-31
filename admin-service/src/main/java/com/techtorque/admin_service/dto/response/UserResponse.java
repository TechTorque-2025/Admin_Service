// ========================================
// UserResponse.java
// ========================================

package com.techtorque.admin_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Response DTO for user information
 * Used by: GET /admin/users, GET /admin/users/{userId}, POST /admin/users/employee
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String userId;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String role;
    private Boolean active;
    private String department;
    private String profilePhoto;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    // Activity statistics (for detailed view)
    private UserActivity activity;

    // User statistics (for detailed view)
    private UserStatistics statistics;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserActivity {
        private Integer totalLogins;
        private LocalDateTime lastActivity;
        private Integer actionsToday;
        private Integer actionsThisWeek;
        private Integer actionsThisMonth;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserStatistics {
        private Integer totalServices; // for employees
        private Integer completedServices;
        private Double hoursWorked;
        private Integer totalVehicles; // for customers
        private Integer totalAppointments;
        private Double averageRating;
    }
}