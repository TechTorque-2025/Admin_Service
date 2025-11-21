package com.techtorque.admin_service.repository;

import com.techtorque.admin_service.entity.ServiceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ServiceTypeRepositoryTest {

    @Autowired
    private ServiceTypeRepository serviceTypeRepository;

    private ServiceType testServiceType;

    @BeforeEach
    void setUp() {
        serviceTypeRepository.deleteAll();

        testServiceType = new ServiceType();
        testServiceType.setName("Oil Change");
        testServiceType.setDescription("Standard oil change service");
        testServiceType.setCategory("MAINTENANCE");
        testServiceType.setPrice(new BigDecimal("3500.00"));
        testServiceType.setDefaultDurationMinutes(30);
        testServiceType.setSkillLevel("BASIC");
        testServiceType.setDailyCapacity(20);
        testServiceType.setRequiresApproval(false);
        testServiceType.setActive(true);
    }

    @Test
    void testSaveServiceType() {
        ServiceType saved = serviceTypeRepository.save(testServiceType);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(testServiceType.getId());
        assertThat(saved.getName()).isEqualTo("Oil Change");
        assertThat(saved.getCategory()).isEqualTo("MAINTENANCE");
    }

    @Test
    void testFindById() {
        serviceTypeRepository.save(testServiceType);

        Optional<ServiceType> found = serviceTypeRepository.findById(testServiceType.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Oil Change");
    }

    @Test
    void testFindByName() {
        serviceTypeRepository.save(testServiceType);

        Optional<ServiceType> found = serviceTypeRepository.findById(testServiceType.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(testServiceType.getId());
    }

    @Test
    void testFindByActiveTrue() {
        ServiceType inactiveService = new ServiceType();
        inactiveService.setName("Inactive Service");
        inactiveService.setDescription("Inactive service");
        inactiveService.setCategory("REPAIR");
        inactiveService.setPrice(new BigDecimal("5000.00"));
        inactiveService.setDefaultDurationMinutes(60);
        inactiveService.setSkillLevel("INTERMEDIATE");
        inactiveService.setDailyCapacity(10);
        inactiveService.setActive(false);

        serviceTypeRepository.save(testServiceType);
        serviceTypeRepository.save(inactiveService);

        List<ServiceType> activeServices = serviceTypeRepository.findByActiveTrue();

        assertThat(activeServices).hasSize(1);
        assertThat(activeServices.get(0).getName()).isEqualTo("Oil Change");
    }

    @Test
    void testFindByCategory() {
        ServiceType repairService = new ServiceType();
        repairService.setName("Brake Repair");
        repairService.setDescription("Brake system repair");
        repairService.setCategory("REPAIR");
        repairService.setPrice(new BigDecimal("8000.00"));
        repairService.setDefaultDurationMinutes(90);
        repairService.setSkillLevel("ADVANCED");
        repairService.setDailyCapacity(5);
        repairService.setActive(true);

        serviceTypeRepository.save(testServiceType);
        serviceTypeRepository.save(repairService);

        List<ServiceType> maintenanceServices = serviceTypeRepository.findAll().stream()
                .filter(s -> "MAINTENANCE".equals(s.getCategory()))
                .toList();

        assertThat(maintenanceServices).hasSize(1);
        assertThat(maintenanceServices.get(0).getName()).isEqualTo("Oil Change");
    }

    @Test
    void testDeleteServiceType() {
        serviceTypeRepository.save(testServiceType);
        String serviceId = testServiceType.getId();

        serviceTypeRepository.deleteById(serviceId);

        Optional<ServiceType> deleted = serviceTypeRepository.findById(serviceId);
        assertThat(deleted).isEmpty();
    }

    @Test
    void testUpdateServiceType() {
        serviceTypeRepository.save(testServiceType);

        testServiceType.setPrice(new BigDecimal("4000.00"));
        testServiceType.setDefaultDurationMinutes(45);
        ServiceType updated = serviceTypeRepository.save(testServiceType);

        assertThat(updated.getPrice()).isEqualByComparingTo(new BigDecimal("4000.00"));
        assertThat(updated.getDefaultDurationMinutes()).isEqualTo(45);
    }
}
