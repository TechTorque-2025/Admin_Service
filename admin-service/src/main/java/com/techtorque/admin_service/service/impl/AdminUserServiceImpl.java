package com.techtorque.admin_service.service.impl;

import com.techtorque.admin_service.service.AdminUserService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AdminUserServiceImpl implements AdminUserService {

  private final WebClient.Builder webClientBuilder;

  public AdminUserServiceImpl(WebClient.Builder webClientBuilder) {
    this.webClientBuilder = webClientBuilder;
  }

  @Override
  public Object listAllUsers() {
    // TODO: Use WebClient to make a secure service-to-service GET request
    // to the Authentication Service's administrative user endpoint.
    return null;
  }

  @Override
  public Object getUserDetails(String userId) {
    // TODO: Use WebClient to make a secure GET request to the Auth Service for a single user.
    return null;
  }

  @Override
  public void updateUser(String userId /*, UserUpdateDto dto */) {
    // TODO: Use WebClient to make a secure PUT request to the Auth Service to update the user.
  }

  @Override
  public void deactivateUser(String userId) {
    // TODO: Use WebClient to make a secure DELETE request to the Auth Service to deactivate the user.
  }
}