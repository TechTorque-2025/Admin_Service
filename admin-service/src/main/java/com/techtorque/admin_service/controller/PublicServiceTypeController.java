package com.techtorque.admin_service.controller;

import com.techtorque.admin_service.dto.response.ServiceTypeResponse;
import com.techtorque.admin_service.service.AdminServiceConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Public controller for service types - accessible by all authenticated users and other microservices
 * This allows customers and other services to read active service types without admin privileges
 */
@RestController
@RequestMapping("/public/service-types")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Public Service Types", description = "Public API for accessing service types")
public class PublicServiceTypeController {

  private final AdminServiceConfigService adminServiceConfigService;

  @GetMapping
  @Operation(summary = "Get all active service types", description = "Retrieve all active service types available to customers")
  public ResponseEntity<List<ServiceTypeResponse>> getActiveServiceTypes() {
    log.info("Public request: Fetching all active service types");
    List<ServiceTypeResponse> serviceTypes = adminServiceConfigService.getAllServiceTypes(true); // activeOnly = true
    log.info("Retrieved {} active service types for public access", serviceTypes.size());
    return ResponseEntity.ok(serviceTypes);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get service type by ID", description = "Retrieve a specific service type by its ID")
  public ResponseEntity<ServiceTypeResponse> getServiceTypeById(@PathVariable String id) {
    log.info("Public request: Fetching service type with ID: {}", id);
    ServiceTypeResponse serviceType = adminServiceConfigService.getServiceTypeById(id);
    return ResponseEntity.ok(serviceType);
  }
}
