package com.techtorque.admin_service.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@Tag(name = "Admin: User Management", description = "Endpoints for administrators to manage user accounts.")
@PreAuthorize("hasRole('ADMIN')") // Secure all endpoints in this controller for ADMIN role
public class AdminUserController {

  // private final WebClient.Builder webClientBuilder;
  // Inject WebClient to make calls to the auth-service

  @GetMapping
  public ResponseEntity<?> listAllUsers(/* Paging and filtering parameters */) {
    // TODO: Make a secure, service-to-service GET request to the Authentication Service
    // to fetch a list of all users.
    return ResponseEntity.ok().build();
  }

  @PutMapping("/{userId}")
  public ResponseEntity<?> updateUser(@PathVariable String userId /*, @RequestBody UserUpdateDto dto */) {
    // TODO: Make a secure, service-to-service PUT request to the Authentication Service
    // to update the specified user's details (e.g., role, active status).
    return ResponseEntity.ok().build();
  }

  @PostMapping("/employee")
  public ResponseEntity<?> createEmployeeAccount(/* @RequestBody CreateEmployeeDto dto */) {
    // TODO: Make a secure, service-to-service POST request to the Authentication Service's
    // /users/employee endpoint to create a new employee.
    return ResponseEntity.ok().build();
  }
}