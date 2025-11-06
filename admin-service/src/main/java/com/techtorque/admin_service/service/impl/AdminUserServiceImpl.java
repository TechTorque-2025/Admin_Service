package com.techtorque.admin_service.service.impl;

import com.techtorque.admin_service.dto.UserUpdateDto;
import com.techtorque.admin_service.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

  private final WebClient.Builder webClientBuilder;

  @Value("${auth.service.url:http://localhost:8081}")
  private String authServiceUrl;

  @Override
  public Object listAllUsers() {
    log.info("Fetching all users from Authentication Service");

    try {
      return webClientBuilder.build()
              .get()
              .uri(authServiceUrl + "/api/v1/users")
              .retrieve()
              .bodyToMono(Object.class)
              .block();
    } catch (Exception e) {
      log.error("Error fetching users from Auth service", e);
      throw new RuntimeException("Failed to fetch users from Authentication Service: " + e.getMessage());
    }
  }

  @Override
  public Object getUserDetails(String userId) {
    log.info("Fetching user details for ID: {} from Authentication Service", userId);

    try {
      return webClientBuilder.build()
              .get()
              .uri(authServiceUrl + "/api/v1/users/" + userId)
              .retrieve()
              .bodyToMono(Object.class)
              .block();
    } catch (Exception e) {
      log.error("Error fetching user details from Auth service", e);
      throw new RuntimeException("Failed to fetch user details from Authentication Service: " + e.getMessage());
    }
  }

  @Override
  public Object updateUser(String userId, UserUpdateDto dto) {
    log.info("Updating user ID: {} in Authentication Service", userId);

    try {
      return webClientBuilder.build()
              .put()
              .uri(authServiceUrl + "/api/v1/users/" + userId)
              .bodyValue(dto)
              .retrieve()
              .bodyToMono(Object.class)
              .block();
    } catch (Exception e) {
      log.error("Error updating user in Auth service", e);
      throw new RuntimeException("Failed to update user in Authentication Service: " + e.getMessage());
    }
  }

  @Override
  public void deactivateUser(String userId) {
    log.info("Deactivating user ID: {} in Authentication Service", userId);

    try {
      webClientBuilder.build()
              .delete()
              .uri(authServiceUrl + "/api/v1/users/" + userId)
              .retrieve()
              .bodyToMono(Void.class)
              .block();

      log.info("User deactivated successfully: {}", userId);
    } catch (Exception e) {
      log.error("Error deactivating user in Auth service", e);
      throw new RuntimeException("Failed to deactivate user in Authentication Service: " + e.getMessage());
    }
  }
}