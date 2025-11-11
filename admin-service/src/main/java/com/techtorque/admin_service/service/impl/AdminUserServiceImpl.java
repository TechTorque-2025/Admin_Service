package com.techtorque.admin_service.service.impl;

import com.techtorque.admin_service.dto.request.CreateEmployeeRequest;
import com.techtorque.admin_service.dto.request.UpdateUserRequest;
import com.techtorque.admin_service.dto.response.UserResponse;
import com.techtorque.admin_service.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of AdminUserService using WebClient to call the auth-service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {

  @Qualifier("authServiceWebClient")
  private final WebClient authServiceWebClient;

  @Override
  public List<UserResponse> getAllUsers(String role, Boolean active, int page, int limit) {
    log.info("Fetching users from auth service - role: {}, active: {}, page: {}, limit: {}",
        role, active, page, limit);

    try {
      // Extract current user info from security context
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String username = authentication != null ? authentication.getName() : "system";
      
      // Extract roles and strip "ROLE_" prefix if present
      String roles = authentication != null && authentication.getAuthorities() != null
          ? authentication.getAuthorities().stream()
              .map(auth -> auth.toString().replaceFirst("^ROLE_", ""))
              .collect(Collectors.joining(","))
          : "ADMIN";
      
      String path = "/users?page=" + page + "&limit=" + limit;
      if (role != null) path += "&role=" + role;
      if (active != null) path += "&active=" + active;

      List<UserResponse> users = authServiceWebClient.get()
          .uri(path)
          .header("X-User-Subject", username)
          .header("X-User-Roles", roles)
          .retrieve()
          .bodyToFlux(UserResponse.class)
          .collectList()
          .block();

      // Convert id to userId and ensure userId is set
      if (users != null) {
        users.forEach(user -> {
          if (user.getUserId() == null && user.getId() != null) {
            user.setUserId(String.valueOf(user.getId()));
          }
        });
      }

      return users != null ? users : Collections.emptyList();
    } catch (Exception e) {
      log.error("Error fetching users from auth service", e);
      throw new RuntimeException("Failed to fetch users: " + e.getMessage());
    }
  }

  @Override
  public UserResponse getUserById(String userId) {
    log.info("Fetching user by ID: {} from auth service", userId);
    try {
      // Auth service endpoints use username, not userId
      // We need to first get all users and find the one with matching ID
      List<UserResponse> allUsers = getAllUsers(null, null, 0, 1000);
      
      UserResponse user = allUsers.stream()
          .filter(u -> {
            String userIdStr = u.getUserId() != null ? u.getUserId() : String.valueOf(u.getId());
            return userIdStr.equals(userId);
          })
          .findFirst()
          .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
      
      log.info("Found user: {} with username: {}", userId, user.getUsername());
      return user;
    } catch (Exception e) {
      log.error("Error fetching user: {}", userId, e);
      throw new RuntimeException("User not found: " + userId);
    }
  }

  @Override
  public UserResponse createEmployee(CreateEmployeeRequest request) {
    log.info("Creating employee: {} via auth service", request.getEmail());
    try {
      UserResponse response = authServiceWebClient.post()
          .uri("/users/employee")
          .bodyValue(request)
          .retrieve()
          .bodyToMono(UserResponse.class)
          .block();

      return response;
    } catch (Exception e) {
      log.error("Error creating employee", e);
      throw new RuntimeException("Failed to create employee: " + e.getMessage());
    }
  }

  @Override
  public UserResponse createAdmin(CreateEmployeeRequest request) {
    log.info("Creating admin: {} via auth service", request.getEmail());
    try {
      UserResponse response = authServiceWebClient.post()
          .uri("/users/admin")
          .bodyValue(request)
          .retrieve()
          .bodyToMono(UserResponse.class)
          .block();

      return response;
    } catch (Exception e) {
      log.error("Error creating admin", e);
      throw new RuntimeException("Failed to create admin: " + e.getMessage());
    }
  }

  @Override
  public UserResponse updateUser(String userId, UpdateUserRequest request) {
    log.info("Updating user: {} via auth service", userId);
    try {
      // First, get the current user details to obtain the username
      UserResponse currentUser = getUserById(userId);
      String username = currentUser.getUsername();
      
      // Handle role updates separately via the roles endpoint
      if (request.getRoles() != null || request.getRole() != null) {
        // Get current roles
        List<String> currentRoles = currentUser.getRoles() != null ? currentUser.getRoles() : new java.util.ArrayList<>();
        List<String> newRoles = new java.util.ArrayList<>();
        
        // Build the new role list - prioritize roles array over single role
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
          newRoles.addAll(request.getRoles());
        } else if (request.getRole() != null) {
          newRoles.add(request.getRole());
        }
        
        // Always preserve CUSTOMER role if it exists
        if (currentRoles.contains("CUSTOMER")) {
          if (!newRoles.contains("CUSTOMER")) {
            newRoles.add("CUSTOMER");
          }
        }
        
        // Always preserve SUPER_ADMIN role if it exists (cannot be removed via this endpoint)
        if (currentRoles.contains("SUPER_ADMIN")) {
          if (!newRoles.contains("SUPER_ADMIN")) {
            newRoles.add("SUPER_ADMIN");
          }
        }
        
        // Determine which roles to add and which to remove
        List<String> rolesToAdd = new java.util.ArrayList<>();
        List<String> rolesToRemove = new java.util.ArrayList<>();
        
        // Find roles to add
        for (String role : newRoles) {
          if (!currentRoles.contains(role)) {
            rolesToAdd.add(role);
          }
        }
        
        // Find roles to remove (only remove EMPLOYEE and ADMIN, never CUSTOMER or SUPER_ADMIN)
        for (String role : currentRoles) {
          if (!newRoles.contains(role) && (role.equals("EMPLOYEE") || role.equals("ADMIN"))) {
            rolesToRemove.add(role);
          }
        }
        
        // Apply role changes
        for (String roleToAdd : rolesToAdd) {
          log.info("Assigning role {} to user {}", roleToAdd, username);
          java.util.Map<String, Object> roleRequest = new java.util.HashMap<>();
          roleRequest.put("roleName", roleToAdd);
          roleRequest.put("action", "ASSIGN");
          
          authServiceWebClient.post()
              .uri("/users/" + username + "/roles")
              .bodyValue(roleRequest)
              .retrieve()
              .bodyToMono(Void.class)
              .block();
        }
        
        for (String roleToRemove : rolesToRemove) {
          log.info("Revoking role {} from user {}", roleToRemove, username);
          java.util.Map<String, Object> roleRequest = new java.util.HashMap<>();
          roleRequest.put("roleName", roleToRemove);
          roleRequest.put("action", "REVOKE");
          
          authServiceWebClient.post()
              .uri("/users/" + username + "/roles")
              .bodyValue(roleRequest)
              .retrieve()
              .bodyToMono(Void.class)
              .block();
        }
      }
      
      // Handle other updates (active status, department, etc.)
      Boolean activationStatus = request.getActivationStatus();
      if (activationStatus != null || request.getDepartment() != null) {
        java.util.Map<String, Object> updateRequest = new java.util.HashMap<>();
        if (activationStatus != null) {
          updateRequest.put("enabled", activationStatus);
        }
        
        if (updateRequest.size() > 0) {
          authServiceWebClient.put()
              .uri("/users/" + username)
              .bodyValue(updateRequest)
              .retrieve()
              .bodyToMono(Void.class)
              .block();
        }
      }
      
      // Return the updated user
      return getUserById(userId);
    } catch (Exception e) {
      log.error("Error updating user: {}", userId, e);
      throw new RuntimeException("Failed to update user: " + e.getMessage());
    }
  }

  @Override
  public void deactivateUser(String userId) {
    log.info("Deactivating user: {} via auth service", userId);
    try {
      authServiceWebClient.post()
          .uri("/users/" + userId + "/disable")
          .retrieve()
          .bodyToMono(Void.class)
          .block();
    } catch (Exception e) {
      log.error("Error deactivating user: {}", userId, e);
      throw new RuntimeException("Failed to deactivate user: " + e.getMessage());
    }
  }

  @Override
  public void activateUser(String userId) {
    log.info("Activating user: {} via auth service", userId);
    try {
      authServiceWebClient.post()
          .uri("/users/" + userId + "/enable")
          .retrieve()
          .bodyToMono(Void.class)
          .block();
    } catch (Exception e) {
      log.error("Error activating user: {}", userId, e);
      throw new RuntimeException("Failed to activate user: " + e.getMessage());
    }
  }
}