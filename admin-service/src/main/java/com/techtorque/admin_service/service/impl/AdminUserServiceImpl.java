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
    log.info("Fetching user: {} from auth service", userId);
    try {
      UserResponse user = authServiceWebClient.get()
          .uri("/users/" + userId)
          .retrieve()
          .bodyToMono(UserResponse.class)
          .block();

      if (user == null) {
        throw new RuntimeException("User not found: " + userId);
      }
      
      // Convert id to userId if needed
      if (user.getUserId() == null && user.getId() != null) {
        user.setUserId(String.valueOf(user.getId()));
      }
      
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
      UserResponse response = authServiceWebClient.put()
          .uri("/users/" + userId)
          .bodyValue(request)
          .retrieve()
          .bodyToMono(UserResponse.class)
          .block();

      return response;
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