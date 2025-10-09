package com.techtorque.admin_service.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/service-types")
@Tag(name = "Admin: Service Configuration", description = "Endpoints for managing available service types.")
@PreAuthorize("hasRole('ADMIN')")
public class AdminConfigController {

  // @Autowired
  // private ServiceTypeService serviceTypeService;

  @GetMapping
  public ResponseEntity<?> listServiceTypes() {
    // TODO: Delegate to a local service that uses ServiceTypeRepository to fetch all types.
    return ResponseEntity.ok().build();
  }

  @PostMapping
  public ResponseEntity<?> addServiceType(/* @RequestBody ServiceTypeDto dto */) {
    // TODO: Delegate to a local service to create a new ServiceType.
    return ResponseEntity.ok().build();
  }
}