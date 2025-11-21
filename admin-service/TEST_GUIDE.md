# Admin Service - Test Guide

## Test Summary

**Total Tests: 67**
**Status: ✅ All Passing**

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Suite
```bash
# Repository tests
mvn test -Dtest=*RepositoryTest

# Service tests
mvn test -Dtest=*ServiceTest

# Controller tests
mvn test -Dtest=*ControllerIntegrationTest

# Integration tests
mvn test -Dtest=*IntegrationTest
```

### Run Single Test Class
```bash
mvn test -Dtest=ServiceTypeRepositoryTest
```

## Test Coverage

### Repository Layer (25 tests)
- `AuditLogRepositoryTest` - 5 tests
- `ReportRepositoryTest` - 6 tests
- `ServiceTypeRepositoryTest` - 7 tests
- `SystemConfigurationRepositoryTest` - 7 tests

### Service Layer (25 tests)
- `AdminServiceConfigServiceTest` - 9 tests
- `AuditLogServiceTest` - 7 tests
- `SystemConfigurationServiceTest` - 9 tests

### Controller Layer (10 tests)
- `AdminServiceConfigControllerIntegrationTest` - 4 tests
- `AuditLogControllerIntegrationTest` - 2 tests
- `SystemConfigurationControllerIntegrationTest` - 4 tests

### Integration Tests (6 tests)
- `ServiceTypeIntegrationTest` - 3 tests
- `SystemConfigurationIntegrationTest` - 3 tests

### Application Test (1 test)
- `AdminServiceApplicationTests` - 1 test

## Test Configuration

- **Profile**: `test`
- **Database**: H2 in-memory
- **Framework**: JUnit 5 + Mockito + Spring Test
- **Security**: Mock authentication with `@WithMockUser`

## Notes

- All tests run in isolated transactions
- Database is reset before each test
- Tests use H2 instead of PostgreSQL for speed
- Security filters are active but authentication is mocked
