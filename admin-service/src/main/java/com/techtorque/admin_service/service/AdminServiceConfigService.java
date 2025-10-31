package com.techtorque.admin_service.service;

import com.techtorque.admin_service.dto.request.CreateServiceTypeRequest;
import com.techtorque.admin_service.dto.request.UpdateServiceTypeRequest;
import com.techtorque.admin_service.dto.response.ServiceTypeResponse;

import java.util.List;

public interface AdminServiceConfigService {
    ServiceTypeResponse createServiceType(CreateServiceTypeRequest request, String createdBy);
    List<ServiceTypeResponse> getAllServiceTypes(boolean activeOnly);
    ServiceTypeResponse getServiceTypeById(String id);
    ServiceTypeResponse updateServiceType(String id, UpdateServiceTypeRequest request, String updatedBy);
    void deleteServiceType(String id, String deletedBy);
}