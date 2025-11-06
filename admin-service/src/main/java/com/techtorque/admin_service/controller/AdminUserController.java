package com.techtorque.admin_service.controller;

import com.techtorque.admin_service.dto.ApiResponse;
import com.techtorque.admin_service.dto.UserUpdateDto;
import com.techtorque.admin_service.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@Tag(name = "Admin: User Management", description = "Endpoints for administrators to manage user accounts.")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {

  private final AdminUserService adminUserService;

  @Operation(summary = "List all users with filters and pagination")
  @GetMapping
  public ResponseEntity<ApiResponse> listAllUsers() {
    Object users = adminUserService.listAllUsers();
    return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
  }

  @Operation(summary = "Get detailed information for a specific user")
  @GetMapping("/{userId}")
  public ResponseEntity<ApiResponse> getUserDetails(@PathVariable String userId) {
    Object user = adminUserService.getUserDetails(userId);
    return ResponseEntity.ok(ApiResponse.success("User details retrieved successfully", user));
  }

  @Operation(summary = "Update a user's role or status")
  @PutMapping("/{userId}")
  public ResponseEntity<ApiResponse> updateUser(
          @PathVariable String userId,
          @Valid @RequestBody UserUpdateDto dto) {
    Object updatedUser = adminUserService.updateUser(userId, dto);
    return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
  }

  @Operation(summary = "Deactivate a user account")
  @DeleteMapping("/{userId}")
  public ResponseEntity<ApiResponse> deactivateUser(@PathVariable String userId) {
    adminUserService.deactivateUser(userId);
    return ResponseEntity.ok(ApiResponse.success("User deactivated successfully", null));
  }
}