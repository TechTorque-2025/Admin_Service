package com.techtorque.admin_service.service;

import com.techtorque.admin_service.dto.UserUpdateDto;

public interface AdminUserService {
  Object listAllUsers();
  Object getUserDetails(String userId);
  Object updateUser(String userId, UserUpdateDto dto);
  void deactivateUser(String userId);
}