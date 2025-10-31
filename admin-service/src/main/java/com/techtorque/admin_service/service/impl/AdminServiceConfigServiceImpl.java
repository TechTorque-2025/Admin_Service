package com.techtorque.admin_service.service.impl;

import com.techtorque.admin_service.dto.request.CreateServiceTypeRequest;
import com.techtorque.admin_service.dto.request.UpdateServiceTypeRequest;
import com.techtorque.admin_service.dto.response.ServiceTypeResponse;
import com.techtorque.admin_service.entity.ServiceType;
import com.techtorque.admin_service.repository.ServiceTypeRepository;
import com.techtorque.admin_service.service.AdminServiceConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminServiceConfigServiceImpl implements AdminServiceConfigService {

  private final ServiceTypeRepository serviceTypeRepository;

  @Override
  public ServiceTypeResponse createServiceType(CreateServiceTypeRequest request, String createdBy) {
    log.info("Creating service type: {} by user: {}", request.getName(), createdBy);

    // Validate service type doesn't already exist
    if (serviceTypeRepository.existsByName(request.getName())) {
      throw new IllegalArgumentException("Service type already exists: " + request.getName());
    }

    // Create entity
    ServiceType serviceType = ServiceType.builder()
            .name(request.getName())
            .description(request.getDescription())
            .category(request.getCategory())
            .price(request.getPrice())
            .durationMinutes(request.getDurationMinutes())
            .requiresApproval(request.getRequiresApproval())
            .dailyCapacity(request.getDailyCapacity())
            .skillLevel(request.getSkillLevel())
            .iconUrl(request.getIconUrl())
            .active(true)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    ServiceType saved = serviceTypeRepository.save(serviceType);

    log.info("Service type created successfully: {}", saved.getId());
    return convertToResponse(saved);
  }

  @Override
  public List<ServiceTypeResponse> getAllServiceTypes(boolean activeOnly) {
    log.info("Fetching all service types, activeOnly: {}", activeOnly);

    List<ServiceType> serviceTypes = activeOnly
            ? serviceTypeRepository.findByActiveTrue()
            : serviceTypeRepository.findAll();

    return serviceTypes.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
  }

  @Override
  public ServiceTypeResponse getServiceTypeById(String id) {
    log.info("Fetching service type: {}", id);

    ServiceType serviceType = serviceTypeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Service type not found: " + id));

    return convertToResponse(serviceType);
  }

  @Override
  public ServiceTypeResponse updateServiceType(String id, UpdateServiceTypeRequest request, String updatedBy) {
    log.info("Updating service type: {} by user: {}", id, updatedBy);

    ServiceType serviceType = serviceTypeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Service type not found: " + id));

    // Update fields if provided
    if (request.getDescription() != null) {
      serviceType.setDescription(request.getDescription());
    }
    if (request.getPrice() != null) {
      serviceType.setPrice(request.getPrice());
    }
    if (request.getDurationMinutes() != null) {
      serviceType.setDurationMinutes(request.getDurationMinutes());
    }
    if (request.getActive() != null) {
      serviceType.setActive(request.getActive());
    }
    if (request.getDailyCapacity() != null) {
      serviceType.setDailyCapacity(request.getDailyCapacity());
    }
    if (request.getSkillLevel() != null) {
      serviceType.setSkillLevel(request.getSkillLevel());
    }
    if (request.getIconUrl() != null) {
      serviceType.setIconUrl(request.getIconUrl());
    }

    serviceType.setUpdatedAt(LocalDateTime.now());
    ServiceType updated = serviceTypeRepository.save(serviceType);

    log.info("Service type updated successfully: {}", updated.getId());
    return convertToResponse(updated);
  }

  @Override
  public void deleteServiceType(String id, String deletedBy) {
    log.info("Deleting service type: {} by user: {}", id, deletedBy);

    ServiceType serviceType = serviceTypeRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Service type not found: " + id));

    // Soft delete
    serviceType.setActive(false);
    serviceTypeRepository.save(serviceType);

    log.info("Service type deleted successfully: {}", id);
  }

  private ServiceTypeResponse convertToResponse(ServiceType serviceType) {
    return ServiceTypeResponse.builder()
            .id(serviceType.getId())
            .name(serviceType.getName())
            .description(serviceType.getDescription())
            .category(serviceType.getCategory())
            .price(serviceType.getPrice())
            .durationMinutes(serviceType.getDurationMinutes())
            .active(serviceType.getActive())
            .requiresApproval(serviceType.getRequiresApproval())
            .dailyCapacity(serviceType.getDailyCapacity())
            .skillLevel(serviceType.getSkillLevel())
            .iconUrl(serviceType.getIconUrl())
            .createdAt(serviceType.getCreatedAt())
            .updatedAt(serviceType.getUpdatedAt())
            .build();
  }
}