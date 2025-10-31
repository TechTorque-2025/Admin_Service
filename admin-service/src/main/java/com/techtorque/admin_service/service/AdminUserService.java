package com.techtorque.admin_service.service;

import com.techtorque.admin_service.dto.request.CreateEmployeeRequest;
import com.techtorque.admin_service.dto.request.UpdateUserRequest;
import com.techtorque.admin_service.dto.response.UserResponse;

import java.util.List;

/**
 * Service interface for admin user management operations
 */
public interface AdminUserService {

    /**
     * Get all users with optional filters
     */
    List<UserResponse> getAllUsers(String role, Boolean active, int page, int limit);

    /**
     * Get user by ID
     */
    UserResponse getUserById(String userId);

    /**
     * Create employee account
     */
    UserResponse createEmployee(CreateEmployeeRequest request);

    /**
     * Create admin account
     */
    UserResponse createAdmin(CreateEmployeeRequest request);

    /**
     * Update user
     */
    UserResponse updateUser(String userId, UpdateUserRequest request);

    /**
     * Deactivate user
     */
    void deactivateUser(String userId);

    /**
     * Activate user
     */
    void activateUser(String userId);
}