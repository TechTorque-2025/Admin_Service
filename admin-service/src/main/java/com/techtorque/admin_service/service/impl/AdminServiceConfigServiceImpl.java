package com.techtorque.admin_service.service.impl;

import com.techtorque.admin_service.entity.ServiceType;
import com.techtorque.admin_service.repository.ServiceTypeRepository;
import com.techtorque.admin_service.service.AdminServiceConfigService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminServiceConfigServiceImpl implements AdminServiceConfigService {

  private final ServiceTypeRepository serviceTypeRepository;

  public AdminServiceConfigServiceImpl(ServiceTypeRepository serviceTypeRepository) {
    this.serviceTypeRepository = serviceTypeRepository;
  }

  @Override
  public List<ServiceType> getAllServiceTypes() {
    // TODO: Call serviceTypeRepository.findAll().
    return List.of();
  }

  @Override
  public ServiceType addServiceType(/* ServiceTypeDto dto */) {
    // TODO: Create a new ServiceType entity from the DTO and save it.
    return null;
  }

  @Override
  public ServiceType updateServiceType(String typeId /*, ServiceTypeDto dto */) {
    // TODO: Find the ServiceType by ID, update its fields, and save it.
    return null;
  }

  @Override
  public void removeServiceType(String typeId) {
    // TODO: Find the ServiceType by ID and delete it.
  }
}