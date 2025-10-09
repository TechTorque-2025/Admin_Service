package com.techtorque.admin_service.service;

public interface AdminUserService {
  Object listAllUsers(/* Paging params */);
  Object getUserDetails(String userId);
  void updateUser(String userId /*, UserUpdateDto dto */);
  void deactivateUser(String userId);
}