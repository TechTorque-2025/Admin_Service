package com.techtorque.admin_service.service;

import com.techtorque.admin_service.dto.request.CreateServiceTypeRequest;
import com.techtorque.admin_service.dto.request.UpdateServiceTypeRequest;
import com.techtorque.admin_service.dto.response.ServiceTypeResponse;
import com.techtorque.admin_service.entity.ServiceType;
import com.techtorque.admin_service.repository.ServiceTypeRepository;
import com.techtorque.admin_service.service.impl.AdminServiceConfigServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceConfigServiceTest {

    @Mock
    private ServiceTypeRepository serviceTypeRepository;

    @InjectMocks
    private AdminServiceConfigServiceImpl serviceConfigService;

    private ServiceType testServiceType;
    private CreateServiceTypeRequest createRequest;

    @BeforeEach
    void setUp() {
        testServiceType = new ServiceType();
        testServiceType.setId(UUID.randomUUID().toString());
        testServiceType.setName("Oil Change");
        testServiceType.setDescription("Standard oil change service");
        testServiceType.setCategory("MAINTENANCE");
        testServiceType.setPrice(new BigDecimal("3500.00"));
        testServiceType.setDefaultDurationMinutes(30);
        testServiceType.setSkillLevel("BASIC");
        testServiceType.setDailyCapacity(20);
        testServiceType.setRequiresApproval(false);
        testServiceType.setActive(true);
        testServiceType.setCreatedAt(LocalDateTime.now());
        testServiceType.setUpdatedAt(LocalDateTime.now());

        createRequest = new CreateServiceTypeRequest();
        createRequest.setName("Oil Change");
        createRequest.setDescription("Standard oil change service");
        createRequest.setCategory("MAINTENANCE");
        createRequest.setPrice(new BigDecimal("3500.00"));
        createRequest.setDurationMinutes(30);
        createRequest.setSkillLevel("BASIC");
        createRequest.setDailyCapacity(20);
        createRequest.setRequiresApproval(false);
    }

    @Test
    void testCreateServiceType_Success() {
        when(serviceTypeRepository.existsByName(anyString())).thenReturn(false);
        when(serviceTypeRepository.save(any(ServiceType.class))).thenReturn(testServiceType);

        ServiceTypeResponse response = serviceConfigService.createServiceType(createRequest, "admin@test.com");

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Oil Change");
        assertThat(response.getCategory()).isEqualTo("MAINTENANCE");
        verify(serviceTypeRepository, times(1)).save(any(ServiceType.class));
    }

    @Test
    void testCreateServiceType_AlreadyExists() {
        when(serviceTypeRepository.existsByName(anyString())).thenReturn(true);

        assertThatThrownBy(() -> serviceConfigService.createServiceType(createRequest, "admin@test.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Service type already exists");

        verify(serviceTypeRepository, never()).save(any(ServiceType.class));
    }

    @Test
    void testGetAllServiceTypes_ActiveOnly() {
        ServiceType activeService = new ServiceType();
        activeService.setId(UUID.randomUUID().toString());
        activeService.setName("Brake Service");
        activeService.setActive(true);

        when(serviceTypeRepository.findByActiveTrue()).thenReturn(Arrays.asList(testServiceType, activeService));

        List<ServiceTypeResponse> responses = serviceConfigService.getAllServiceTypes(true);

        assertThat(responses).hasSize(2);
        verify(serviceTypeRepository, times(1)).findByActiveTrue();
        verify(serviceTypeRepository, never()).findAll();
    }

    @Test
    void testGetAllServiceTypes_AllServices() {
        ServiceType inactiveService = new ServiceType();
        inactiveService.setId(UUID.randomUUID().toString());
        inactiveService.setName("Inactive Service");
        inactiveService.setActive(false);

        when(serviceTypeRepository.findAll()).thenReturn(Arrays.asList(testServiceType, inactiveService));

        List<ServiceTypeResponse> responses = serviceConfigService.getAllServiceTypes(false);

        assertThat(responses).hasSize(2);
        verify(serviceTypeRepository, times(1)).findAll();
        verify(serviceTypeRepository, never()).findByActiveTrue();
    }

    @Test
    void testGetServiceTypeById_Success() {
        when(serviceTypeRepository.findById(anyString())).thenReturn(Optional.of(testServiceType));

        ServiceTypeResponse response = serviceConfigService.getServiceTypeById(testServiceType.getId());

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Oil Change");
        verify(serviceTypeRepository, times(1)).findById(testServiceType.getId());
    }

    @Test
    void testGetServiceTypeById_NotFound() {
        when(serviceTypeRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serviceConfigService.getServiceTypeById("non-existent-id"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Service type not found");
    }

    @Test
    void testUpdateServiceType_Success() {
        UpdateServiceTypeRequest updateRequest = new UpdateServiceTypeRequest();
        updateRequest.setDescription("Updated description");
        updateRequest.setPrice(new BigDecimal("4000.00"));
        updateRequest.setDurationMinutes(45);

        when(serviceTypeRepository.findById(anyString())).thenReturn(Optional.of(testServiceType));
        when(serviceTypeRepository.save(any(ServiceType.class))).thenReturn(testServiceType);

        ServiceTypeResponse response = serviceConfigService.updateServiceType(testServiceType.getId(), updateRequest, "admin@test.com");

        assertThat(response).isNotNull();
        verify(serviceTypeRepository, times(1)).save(any(ServiceType.class));
    }

    @Test
    void testDeleteServiceType_Success() {
        when(serviceTypeRepository.findById(anyString())).thenReturn(Optional.of(testServiceType));
        doNothing().when(serviceTypeRepository).delete(any(ServiceType.class));

        serviceConfigService.deleteServiceType(testServiceType.getId(), "admin@test.com");

        verify(serviceTypeRepository, times(1)).findById(anyString());
        verify(serviceTypeRepository, times(1)).delete(any(ServiceType.class));
    }

    @Test
    void testDeleteServiceType_NotFound() {
        when(serviceTypeRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> serviceConfigService.deleteServiceType("non-existent-id", "admin@test.com"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
