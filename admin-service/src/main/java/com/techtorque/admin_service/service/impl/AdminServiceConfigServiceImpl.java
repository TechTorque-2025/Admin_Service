package com.techtorque.admin_service.service.impl;

import com.techtorque.admin_service.dto.ServiceTypeDto;
import com.techtorque.admin_service.entity.ServiceType;
import com.techtorque.admin_service.repository.ServiceTypeRepository;
import com.techtorque.admin_service.service.AdminServiceConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceConfigServiceImpl implements AdminServiceConfigService {

  private final ServiceTypeRepository serviceTypeRepository;

  @Override
  public List<ServiceType> getAllServiceTypes() {
    log.info("Fetching all service types");
    return serviceTypeRepository.findAll();
  }

  @Override
  public ServiceType addServiceType(ServiceTypeDto dto) {
    log.info("Adding new service type: {}", dto.getName());

    ServiceType serviceType = ServiceType.builder()
            .name(dto.getName())
            .description(dto.getDescription())
            .price(dto.getPrice())
            .defaultDurationMinutes(dto.getDefaultDurationMinutes())
            .category(dto.getCategory())
            .build();

    ServiceType saved = serviceTypeRepository.save(serviceType);
    log.info("Service type created with ID: {}", saved.getId());
    return saved;
  }

  @Override
  public ServiceType updateServiceType(String typeId, ServiceTypeDto dto) {
    log.info("Updating service type with ID: {}", typeId);

    ServiceType serviceType = serviceTypeRepository.findById(typeId)
            .orElseThrow(() -> new RuntimeException("Service type not found with ID: " + typeId));

    serviceType.setName(dto.getName());
    serviceType.setDescription(dto.getDescription());
    serviceType.setPrice(dto.getPrice());
    serviceType.setDefaultDurationMinutes(dto.getDefaultDurationMinutes());
    serviceType.setCategory(dto.getCategory());

    ServiceType updated = serviceTypeRepository.save(serviceType);
    log.info("Service type updated successfully: {}", typeId);
    return updated;
  }

  @Override
  public void removeServiceType(String typeId) {
    log.info("Removing service type with ID: {}", typeId);

    ServiceType serviceType = serviceTypeRepository.findById(typeId)
            .orElseThrow(() -> new RuntimeException("Service type not found with ID: " + typeId));

    serviceTypeRepository.delete(serviceType);
    log.info("Service type removed successfully: {}", typeId);
  }
}