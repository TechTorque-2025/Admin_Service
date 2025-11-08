package com.techtorque.admin_service.controller;

import com.techtorque.admin_service.dto.request.CreateSystemConfigRequest;
import com.techtorque.admin_service.dto.request.UpdateSystemConfigRequest;
import com.techtorque.admin_service.dto.response.ApiResponse;
import com.techtorque.admin_service.dto.response.SystemConfigurationResponse;
import com.techtorque.admin_service.service.AuditLogService;
import com.techtorque.admin_service.service.SystemConfigurationService;
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
@RequestMapping("/admin/config")
@Tag(name = "Admin: System Configuration", description = "Endpoints for managing system configuration")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class SystemConfigurationController {

    private final SystemConfigurationService configurationService;
    private final AuditLogService auditLogService;

    @Operation(summary = "Get all system configurations")
    @GetMapping
    public ResponseEntity<ApiResponse<List<SystemConfigurationResponse>>> getAllConfigs() {
        List<SystemConfigurationResponse> configs = configurationService.getAllConfigs();
        return ResponseEntity.ok(ApiResponse.success("Configurations retrieved successfully", configs));
    }

    @Operation(summary = "Get system configurations by category")
    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<SystemConfigurationResponse>>> getConfigsByCategory(
            @PathVariable String category) {
        List<SystemConfigurationResponse> configs = configurationService.getConfigsByCategory(category);
        return ResponseEntity.ok(ApiResponse.success("Configurations retrieved successfully", configs));
    }

    @Operation(summary = "Get configuration by key")
    @GetMapping("/{key}")
    public ResponseEntity<ApiResponse<SystemConfigurationResponse>> getConfig(@PathVariable String key) {
        SystemConfigurationResponse config = configurationService.getConfig(key);
        return ResponseEntity.ok(ApiResponse.success("Configuration retrieved successfully", config));
    }

    @Operation(summary = "Create new system configuration")
    @PostMapping
    public ResponseEntity<ApiResponse<SystemConfigurationResponse>> createConfig(
            @Valid @RequestBody CreateSystemConfigRequest request,
            Authentication authentication) {
        String createdBy = authentication.getName();
        SystemConfigurationResponse config = configurationService.createConfig(request, createdBy);

        // Log audit
        auditLogService.logAction(
                createdBy,
                authentication.getName(),
                "ADMIN",
                "CREATE",
                "SYSTEM_CONFIG",
                config.getConfigKey(),
                "Created system configuration: " + config.getConfigKey()
        );

        return ResponseEntity.ok(ApiResponse.success("Configuration created successfully", config));
    }

    @Operation(summary = "Update system configuration")
    @PutMapping("/{key}")
    public ResponseEntity<ApiResponse<SystemConfigurationResponse>> updateConfig(
            @PathVariable String key,
            @Valid @RequestBody UpdateSystemConfigRequest request,
            Authentication authentication) {
        String updatedBy = authentication.getName();
        SystemConfigurationResponse config = configurationService.updateConfig(key, request, updatedBy);

        // Log audit
        auditLogService.logAction(
                updatedBy,
                authentication.getName(),
                "ADMIN",
                "UPDATE",
                "SYSTEM_CONFIG",
                key,
                "Updated system configuration: " + key
        );

        return ResponseEntity.ok(ApiResponse.success("Configuration updated successfully", config));
    }

    @Operation(summary = "Delete system configuration")
    @DeleteMapping("/{key}")
    public ResponseEntity<ApiResponse<Void>> deleteConfig(
            @PathVariable String key,
            Authentication authentication) {
        String deletedBy = authentication.getName();
        configurationService.deleteConfig(key, deletedBy);

        // Log audit
        auditLogService.logAction(
                deletedBy,
                authentication.getName(),
                "ADMIN",
                "DELETE",
                "SYSTEM_CONFIG",
                key,
                "Deleted system configuration: " + key
        );

        return ResponseEntity.ok(ApiResponse.success("Configuration deleted successfully", null));
    }
}
