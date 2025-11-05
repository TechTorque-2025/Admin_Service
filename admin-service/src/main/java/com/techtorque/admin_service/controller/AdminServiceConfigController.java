package com.techtorque.admin_service.controller;

import com.techtorque.admin_service.dto.request.CreateServiceTypeRequest;
import com.techtorque.admin_service.dto.request.UpdateServiceTypeRequest;
import com.techtorque.admin_service.dto.response.ApiResponse;
import com.techtorque.admin_service.dto.response.ServiceTypeResponse;
import com.techtorque.admin_service.service.AdminServiceConfigService;
import com.techtorque.admin_service.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/service-types")
@Tag(name = "Admin: Service Configuration", description = "Endpoints for managing available service types.")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminServiceConfigController {

  private final AdminServiceConfigService serviceTypeService;
  private final AuditLogService auditLogService;

  @Operation(summary = "List all configurable service types")
  @GetMapping
  public ResponseEntity<ApiResponse<List<ServiceTypeResponse>>> listServiceTypes(
          @RequestParam(defaultValue = "true") boolean activeOnly) {
    List<ServiceTypeResponse> serviceTypes = serviceTypeService.getAllServiceTypes(activeOnly);
    return ResponseEntity.ok(ApiResponse.success("Service types retrieved successfully", serviceTypes));
  }

  @Operation(summary = "Get service type by ID")
  @GetMapping("/{typeId}")
  public ResponseEntity<ApiResponse<ServiceTypeResponse>> getServiceType(@PathVariable String typeId) {
    ServiceTypeResponse serviceType = serviceTypeService.getServiceTypeById(typeId);
    return ResponseEntity.ok(ApiResponse.success("Service type retrieved successfully", serviceType));
  }

  @Operation(summary = "Add a new service type")
  @PostMapping
  public ResponseEntity<ApiResponse<ServiceTypeResponse>> addServiceType(
          @Valid @RequestBody CreateServiceTypeRequest request,
          Authentication authentication) {
    String createdBy = authentication.getName();
    ServiceTypeResponse serviceType = serviceTypeService.createServiceType(request, createdBy);
    
    // Log audit
    auditLogService.logAction(
            createdBy, 
            authentication.getName(),
            "ADMIN",
            "CREATE",
            "SERVICE_TYPE",
            serviceType.getId(),
            "Created service type: " + serviceType.getName()
    );
    
    return ResponseEntity.ok(ApiResponse.success("Service type created successfully", serviceType));
  }

  @Operation(summary = "Update an existing service type")
  @PutMapping("/{typeId}")
  public ResponseEntity<ApiResponse<ServiceTypeResponse>> updateServiceType(
          @PathVariable String typeId,
          @Valid @RequestBody UpdateServiceTypeRequest request,
          Authentication authentication) {
    String updatedBy = authentication.getName();
    ServiceTypeResponse serviceType = serviceTypeService.updateServiceType(typeId, request, updatedBy);
    
    // Log audit
    auditLogService.logAction(
            updatedBy,
            authentication.getName(),
            "ADMIN",
            "UPDATE",
            "SERVICE_TYPE",
            typeId,
            "Updated service type: " + typeId
    );
    
    return ResponseEntity.ok(ApiResponse.success("Service type updated successfully", serviceType));
  }

  @Operation(summary = "Remove a service type")
  @DeleteMapping("/{typeId}")
  public ResponseEntity<ApiResponse<Void>> removeServiceType(
          @PathVariable String typeId,
          Authentication authentication) {
    String deletedBy = authentication.getName();
    serviceTypeService.deleteServiceType(typeId, deletedBy);
    
    // Log audit
    auditLogService.logAction(
            deletedBy,
            authentication.getName(),
            "ADMIN",
            "DELETE",
            "SERVICE_TYPE",
            typeId,
            "Deleted service type: " + typeId
    );
    
    return ResponseEntity.ok(ApiResponse.success("Service type deleted successfully", null));
  }
}