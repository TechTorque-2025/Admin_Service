package com.techtorque.admin_service.service.impl;

import com.techtorque.admin_service.dto.request.CreateEmployeeRequest;
import com.techtorque.admin_service.dto.request.UpdateUserRequest;
import com.techtorque.admin_service.dto.response.UserResponse;
import com.techtorque.admin_service.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of AdminUserService using RestTemplate to call the auth-service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUserServiceImpl implements AdminUserService {

  @Value("${auth.service.url:http://localhost:8080}")
  private String authServiceUrl;

  private final RestTemplate restTemplate = new RestTemplate();

  @Override
  public List<UserResponse> getAllUsers(String role, Boolean active, int page, int limit) {
    log.info("Fetching users from auth service - role: {}, active: {}, page: {}, limit: {}",
        role, active, page, limit);

    try {
      String url = String.format("%s/auth/admin/users?page=%d&limit=%d", authServiceUrl, page, limit);
      if (role != null) url += "&role=" + role;
      if (active != null) url += "&active=" + active;

      ResponseEntity<UserResponse[]> response = restTemplate.exchange(
          url,
          HttpMethod.GET,
          createAuthHeaders(),
          UserResponse[].class
      );

      if (response.getBody() != null) return Arrays.asList(response.getBody());
      return Collections.emptyList();
    } catch (Exception e) {
      log.error("Error fetching users from auth service", e);
      return Collections.emptyList();
    }
  }

  @Override
  public UserResponse getUserById(String userId) {
    log.info("Fetching user: {} from auth service", userId);
    try {
      String url = String.format("%s/auth/admin/users/%s", authServiceUrl, userId);
      ResponseEntity<UserResponse> response = restTemplate.exchange(
          url,
          HttpMethod.GET,
          createAuthHeaders(),
          UserResponse.class
      );
      return response.getBody();
    } catch (Exception e) {
      log.error("Error fetching user: {}", userId, e);
      throw new RuntimeException("User not found: " + userId);
    }
  }

  @Override
  public UserResponse createEmployee(CreateEmployeeRequest request) {
    log.info("Creating employee: {} via auth service", request.getEmail());
    try {
      String url = String.format("%s/auth/admin/create-employee", authServiceUrl);
      HttpEntity<CreateEmployeeRequest> entity = new HttpEntity<>(request, createHeaders());
      ResponseEntity<UserResponse> response = restTemplate.exchange(
          url,
          HttpMethod.POST,
          entity,
          UserResponse.class
      );
      return response.getBody();
    } catch (Exception e) {
      log.error("Error creating employee", e);
      throw new RuntimeException("Failed to create employee: " + e.getMessage());
    }
  }

  @Override
  public UserResponse createAdmin(CreateEmployeeRequest request) {
    log.info("Creating admin: {} via auth service", request.getEmail());
    try {
      String url = String.format("%s/auth/admin/create-admin", authServiceUrl);
      HttpEntity<CreateEmployeeRequest> entity = new HttpEntity<>(request, createHeaders());
      ResponseEntity<UserResponse> response = restTemplate.exchange(
          url,
          HttpMethod.POST,
          entity,
          UserResponse.class
      );
      return response.getBody();
    } catch (Exception e) {
      log.error("Error creating admin", e);
      throw new RuntimeException("Failed to create admin: " + e.getMessage());
    }
  }

  @Override
  public UserResponse updateUser(String userId, UpdateUserRequest request) {
    log.info("Updating user: {} via auth service", userId);
    try {
      String url = String.format("%s/auth/admin/users/%s", authServiceUrl, userId);
      HttpEntity<UpdateUserRequest> entity = new HttpEntity<>(request, createHeaders());
      ResponseEntity<UserResponse> response = restTemplate.exchange(
          url,
          HttpMethod.PUT,
          entity,
          UserResponse.class
      );
      return response.getBody();
    } catch (Exception e) {
      log.error("Error updating user: {}", userId, e);
      throw new RuntimeException("Failed to update user: " + e.getMessage());
    }
  }

  @Override
  public void deactivateUser(String userId) {
    log.info("Deactivating user: {} via auth service", userId);
    try {
      String url = String.format("%s/auth/admin/users/%s/disable", authServiceUrl, userId);
      restTemplate.exchange(url, HttpMethod.PUT, createAuthHeaders(), Void.class);
    } catch (Exception e) {
      log.error("Error deactivating user: {}", userId, e);
      throw new RuntimeException("Failed to deactivate user: " + e.getMessage());
    }
  }

  @Override
  public void activateUser(String userId) {
    log.info("Activating user: {} via auth service", userId);
    try {
      String url = String.format("%s/auth/admin/users/%s/enable", authServiceUrl, userId);
      restTemplate.exchange(url, HttpMethod.PUT, createAuthHeaders(), Void.class);
    } catch (Exception e) {
      log.error("Error activating user: {}", userId, e);
      throw new RuntimeException("Failed to activate user: " + e.getMessage());
    }
  }

  private org.springframework.http.HttpHeaders createHeaders() {
    org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }

  private HttpEntity<Void> createAuthHeaders() {
    return new HttpEntity<>(createHeaders());
  }
}