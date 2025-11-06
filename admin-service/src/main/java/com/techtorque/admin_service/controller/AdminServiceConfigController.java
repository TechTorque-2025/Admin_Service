package com.techtorque.admin_service.controller;

import com.techtorque.admin_service.dto.ApiResponse;
import com.techtorque.admin_service.dto.ServiceTypeDto;
import com.techtorque.admin_service.entity.ServiceType;
import com.techtorque.admin_service.service.AdminServiceConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/service-types")
@Tag(name = "Admin: Service Configuration", description = "Endpoints for managing available service types.")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminServiceConfigController {

  private final AdminServiceConfigService serviceTypeService;

  @Operation(summary = "List all configurable service types")
  @GetMapping
  public ResponseEntity<ApiResponse> listServiceTypes() {
    List<ServiceType> serviceTypes = serviceTypeService.getAllServiceTypes();
    return ResponseEntity.ok(ApiResponse.success("Service types retrieved successfully", serviceTypes));
  }

  @Operation(summary = "Add a new service type")
  @PostMapping
  public ResponseEntity<ApiResponse> addServiceType(@Valid @RequestBody ServiceTypeDto dto) {
    ServiceType created = serviceTypeService.addServiceType(dto);
    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success("Service type created successfully", created));
  }

  @Operation(summary = "Update an existing service type")
  @PutMapping("/{typeId}")
  public ResponseEntity<ApiResponse> updateServiceType(
          @PathVariable String typeId,
          @Valid @RequestBody ServiceTypeDto dto) {
    ServiceType updated = serviceTypeService.updateServiceType(typeId, dto);
    return ResponseEntity.ok(ApiResponse.success("Service type updated successfully", updated));
  }

  @Operation(summary = "Remove a service type")
  @DeleteMapping("/{typeId}")
  public ResponseEntity<ApiResponse> removeServiceType(@PathVariable String typeId) {
    serviceTypeService.removeServiceType(typeId);
    return ResponseEntity.ok(ApiResponse.success("Service type removed successfully", null));
  }
}