package com.techtorque.admin_service.controller;

import com.techtorque.admin_service.dto.request.CreateEmployeeRequest;
import com.techtorque.admin_service.dto.request.UpdateUserRequest;
import com.techtorque.admin_service.dto.response.ApiResponse;
import com.techtorque.admin_service.dto.response.UserResponse;
import com.techtorque.admin_service.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Tag(name = "Admin: User Management", description = "Endpoints for administrators to manage user accounts.")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {

  private final AdminUserService adminUserService;

  @Operation(summary = "List all users with filters and pagination")
  @GetMapping
  public ResponseEntity<ApiResponse<List<UserResponse>>> listAllUsers(
          @RequestParam(required = false) String role,
          @RequestParam(required = false) Boolean active,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "50") int limit) {
    List<UserResponse> users = adminUserService.getAllUsers(role, active, page, limit);
    return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
  }

  @Operation(summary = "Get detailed information for a specific user")
  @GetMapping("/{userId}")
  public ResponseEntity<ApiResponse<UserResponse>> getUserDetails(@PathVariable String userId) {
    UserResponse user = adminUserService.getUserById(userId);
    return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
  }

  @Operation(summary = "Update a user's role or status")
  @PutMapping("/{userId}")
  public ResponseEntity<ApiResponse<UserResponse>> updateUser(
          @PathVariable String userId,
          @Valid @RequestBody UpdateUserRequest request) {
    UserResponse user = adminUserService.updateUser(userId, request);
    return ResponseEntity.ok(ApiResponse.success("User updated successfully", user));
  }

  @Operation(summary = "Deactivate a user account")
  @DeleteMapping("/{userId}")
  public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable String userId) {
    adminUserService.deactivateUser(userId);
    return ResponseEntity.ok(ApiResponse.success("User deactivated successfully", null));
  }

  @Operation(summary = "Create employee account")
  @PostMapping("/employee")
  public ResponseEntity<ApiResponse<UserResponse>> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
    UserResponse user = adminUserService.createEmployee(request);
    return ResponseEntity.ok(ApiResponse.success("Employee created successfully", user));
  }

  @Operation(summary = "Create admin account")
  @PostMapping("/admin")
  @PreAuthorize("hasRole('SUPER_ADMIN')")
  public ResponseEntity<ApiResponse<UserResponse>> createAdmin(@Valid @RequestBody CreateEmployeeRequest request) {
    UserResponse user = adminUserService.createAdmin(request);
    return ResponseEntity.ok(ApiResponse.success("Admin created successfully", user));
  }
}