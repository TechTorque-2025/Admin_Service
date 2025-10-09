package com.techtorque.admin_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/service-types")
@Tag(name = "Admin: Service Configuration", description = "Endpoints for managing available service types.")
@PreAuthorize("hasRole('ADMIN')")
public class AdminServiceConfigController {

  // @Autowired private ServiceTypeService serviceTypeService;

  @Operation(summary = "List all configurable service types")
  @GetMapping
  public ResponseEntity<?> listServiceTypes() {
    // TODO: Delegate to a local service to fetch all types.
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Add a new service type")
  @PostMapping
  public ResponseEntity<?> addServiceType(/* @RequestBody ServiceTypeDto dto */) {
    // TODO: Delegate to a local service to create a new ServiceType.
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Update an existing service type")
  @PutMapping("/{typeId}")
  public ResponseEntity<?> updateServiceType(@PathVariable String typeId /*, @RequestBody ServiceTypeDto dto */) {
    // TODO: Delegate to a local service to update the ServiceType.
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Remove a service type")
  @DeleteMapping("/{typeId}")
  public ResponseEntity<?> removeServiceType(@PathVariable String typeId) {
    // TODO: Delegate to a local service to delete the ServiceType.
    return ResponseEntity.ok().build();
  }
}