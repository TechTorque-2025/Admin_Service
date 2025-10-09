package com.techtorque.admin_service.service;

import com.techtorque.admin_service.entity.ServiceType;
import java.util.List;

public interface AdminServiceConfigService {
  List<ServiceType> getAllServiceTypes();
  ServiceType addServiceType(/* ServiceTypeDto dto */);
  ServiceType updateServiceType(String typeId /*, ServiceTypeDto dto */);
  void removeServiceType(String typeId);
}