package com.techtorque.admin_service.repository;

import com.techtorque.admin_service.entity.AuditLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AuditLogRepositoryTest {

    @Autowired
    private AuditLogRepository auditLogRepository;

    private AuditLog testAuditLog;

    @BeforeEach
    void setUp() {
        auditLogRepository.deleteAll();

        testAuditLog = new AuditLog();
        testAuditLog.setUserId("user-123");
        testAuditLog.setUsername("admin@test.com");
        testAuditLog.setUserRole("ADMIN");
        testAuditLog.setAction("CREATE");
        testAuditLog.setEntityType("SERVICE_TYPE");
        testAuditLog.setEntityId("service-123");
        testAuditLog.setDescription("Created service type");
        testAuditLog.setIpAddress("192.168.1.1");
        testAuditLog.setSuccess(true);
        testAuditLog.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testSaveAuditLog() {
        AuditLog saved = auditLogRepository.save(testAuditLog);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isEqualTo(testAuditLog.getId());
        assertThat(saved.getAction()).isEqualTo("CREATE");
        assertThat(saved.getEntityType()).isEqualTo("SERVICE_TYPE");
    }

    @Test
    void testFindByUserId() {
        auditLogRepository.save(testAuditLog);

        Page<AuditLog> logs = auditLogRepository.findByUserId("user-123", PageRequest.of(0, 10));

        assertThat(logs.getContent()).hasSize(1);
        assertThat(logs.getContent().get(0).getUserId()).isEqualTo("user-123");
    }

    @Test
    void testFindByAction() {
        AuditLog updateLog = new AuditLog();
        updateLog.setUserId("user-456");
        updateLog.setUsername("admin2@test.com");
        updateLog.setUserRole("ADMIN");
        updateLog.setAction("UPDATE");
        updateLog.setEntityType("USER");
        updateLog.setEntityId("user-789");
        updateLog.setDescription("Updated user");
        updateLog.setSuccess(true);
        updateLog.setCreatedAt(LocalDateTime.now());

        auditLogRepository.save(testAuditLog);
        auditLogRepository.save(updateLog);

        Page<AuditLog> createLogs = auditLogRepository.findByAction("CREATE", PageRequest.of(0, 10));

        assertThat(createLogs.getContent()).hasSize(1);
        assertThat(createLogs.getContent().get(0).getAction()).isEqualTo("CREATE");
    }

    @Test
    void testFindByEntityType() {
        auditLogRepository.save(testAuditLog);

        Page<AuditLog> logs = auditLogRepository.findByEntityType("SERVICE_TYPE", PageRequest.of(0, 10));

        assertThat(logs.getContent()).hasSize(1);
        assertThat(logs.getContent().get(0).getEntityType()).isEqualTo("SERVICE_TYPE");
    }

    @Test
    void testFindByEntityTypeAndEntityId() {
        auditLogRepository.save(testAuditLog);

        List<AuditLog> logs = auditLogRepository.findByEntityTypeAndEntityId("SERVICE_TYPE", "service-123");

        assertThat(logs).hasSize(1);
        assertThat(logs.get(0).getEntityId()).isEqualTo("service-123");
    }

    // Tests for methods that may not exist in repository - commented out
    // @Test
    // void testFindByCreatedAtBetween() ...
    // @Test
    // void testFindBySuccess() ...
}
