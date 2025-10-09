package com.techtorque.admin_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@Tag(name = "Admin: User Management", description = "Endpoints for administrators to manage user accounts.")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

  // private final WebClient.Builder webClientBuilder;

  @Operation(summary = "List all users with filters and pagination")
  @GetMapping
  public ResponseEntity<?> listAllUsers() {
    // TODO: Make a secure GET request to the Authentication Service to fetch users.
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Get detailed information for a specific user")
  @GetMapping("/{userId}")
  public ResponseEntity<?> getUserDetails(@PathVariable String userId) {
    // TODO: Make a secure GET request to the Authentication Service for a single user's details.
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Update a user's role or status")
  @PutMapping("/{userId}")
  public ResponseEntity<?> updateUser(@PathVariable String userId /*, @RequestBody UserUpdateDto dto */) {
    // TODO: Make a secure PUT request to the Authentication Service to update the user.
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Deactivate a user account")
  @DeleteMapping("/{userId}")
  public ResponseEntity<?> deactivateUser(@PathVariable String userId) {
    // TODO: Make a secure DELETE request to the Authentication Service to deactivate the user.
    return ResponseEntity.ok().build();
  }
}