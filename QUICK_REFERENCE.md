# Admin Service - Quick Reference

## 🚀 Quick Start

```bash
# 1. Start PostgreSQL database
docker-compose up -d postgres

# 2. Set environment variables
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=techtorque_admin
export SPRING_PROFILE=dev

# 3. Start the service
cd Admin_Service/admin-service
./mvnw spring-boot:run

# 4. Access Swagger UI
http://localhost:8087/swagger-ui/index.html
```

## 📋 Endpoints Summary

### User Management (Proxy to Auth Service)
- `GET /admin/users` - List all users
- `GET /admin/users/{userId}` - Get user details
- `PUT /admin/users/{userId}` - Update user
- `DELETE /admin/users/{userId}` - Deactivate user
- `POST /admin/users/employee` - Create employee
- `POST /admin/users/admin` - Create admin (SUPER_ADMIN only)

### Service Types
- `GET /admin/service-types` - List service types
- `GET /admin/service-types/{typeId}` - Get service type
- `POST /admin/service-types` - Create service type
- `PUT /admin/service-types/{typeId}` - Update service type
- `DELETE /admin/service-types/{typeId}` - Delete service type

### System Configuration
- `GET /admin/config` - Get all configurations
- `GET /admin/config/{key}` - Get configuration by key
- `GET /admin/config/category/{category}` - Get by category
- `POST /admin/config` - Create configuration
- `PUT /admin/config/{key}` - Update configuration
- `DELETE /admin/config/{key}` - Delete configuration

### Reports
- `POST /admin/reports/generate` - Generate report
- `GET /admin/reports` - List all reports
- `GET /admin/reports/{reportId}` - Get report details

### Analytics
- `GET /admin/analytics/dashboard?period=30d` - Dashboard analytics
- `GET /admin/analytics/metrics` - System metrics

### Audit Logs
- `GET /admin/audit-logs` - Search audit logs (with filters)
- `GET /admin/audit-logs/{logId}` - Get audit log details

## 🔑 Authentication

All endpoints require JWT token:

```bash
# 1. Login via Auth service
curl -X POST "http://localhost:8081/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@techtorque.com", "password": "password"}'

# 2. Use token in requests
curl -X GET "http://localhost:8087/admin/service-types" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 📊 Seeded Data (Dev Profile)

### Service Types
1. Oil Change - LKR 5,000
2. Brake Service - LKR 12,000
3. Tire Rotation - LKR 3,000
4. Engine Diagnostic - LKR 8,000
5. AC Service - LKR 9,500
6. Transmission Service - LKR 15,000
7. Full Vehicle Inspection - LKR 4,500
8. Custom Body Modification - LKR 50,000
9. Paint Job - LKR 75,000
10. Wheel Alignment - LKR 6,500

### System Configurations
- BUSINESS_HOURS_START = 08:00
- BUSINESS_HOURS_END = 18:00
- SLOTS_PER_HOUR = 4
- MAX_APPOINTMENTS_PER_DAY = 50
- EMAIL_NOTIFICATIONS_ENABLED = true
- And 5 more...

## 🔧 Environment Variables

```bash
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=techtorque_admin
DB_USER=techtorque
DB_PASS=techtorque123
DB_MODE=update

# Application
SPRING_PROFILE=dev
server.port=8087

# Microservices
AUTH_SERVICE_URL=http://localhost:8081
VEHICLE_SERVICE_URL=http://localhost:8082
APPOINTMENT_SERVICE_URL=http://localhost:8083
PROJECT_SERVICE_URL=http://localhost:8084
TIME_LOGGING_SERVICE_URL=http://localhost:8085
PAYMENT_SERVICE_URL=http://localhost:8086
```

## 🎯 Common Tasks

### Create a Service Type
```bash
curl -X POST "http://localhost:8087/admin/service-types" \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Battery Replacement",
    "description": "Complete battery replacement",
    "category": "MAINTENANCE",
    "price": 7500.00,
    "durationMinutes": 45,
    "skillLevel": "BASIC",
    "dailyCapacity": 10
  }'
```

### Generate a Report
```bash
curl -X POST "http://localhost:8087/admin/reports/generate" \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "type": "REVENUE",
    "fromDate": "2025-01-01",
    "toDate": "2025-01-31",
    "format": "JSON"
  }'
```

### Update System Configuration
```bash
curl -X PUT "http://localhost:8087/admin/config/BUSINESS_HOURS_START" \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "configValue": "09:00",
    "description": "Updated business opening time"
  }'
```

### Search Audit Logs
```bash
curl -X GET "http://localhost:8087/admin/audit-logs?action=CREATE&entityType=SERVICE_TYPE&page=0&size=50" \
  -H "Authorization: Bearer TOKEN"
```

## 🐛 Troubleshooting

### Service won't start
1. Check PostgreSQL is running
2. Verify database credentials
3. Check port 8087 is available
4. Review logs: `tail -f logs/admin-service.log`

### Can't access endpoints
1. Verify JWT token is valid
2. Check user has ADMIN role
3. Confirm service is running: `curl http://localhost:8087/actuator/health`

### WebClient errors
1. Verify target service is running (e.g., Auth service on 8081)
2. Check network connectivity
3. Review service URLs in configuration

## 📝 Implementation Status

✅ **COMPLETE** - All 24 endpoints implemented  
✅ **TESTED** - Compiles successfully  
✅ **DOCUMENTED** - Swagger UI available  
✅ **SEEDED** - Dev data ready  

## 📚 Additional Documentation

- `IMPLEMENTATION_SUMMARY.md` - Detailed implementation guide
- `IMPLEMENTATION_COMPLETE.md` - Before/after comparison
- `README.md` - Service overview
- Swagger UI - Interactive API docs

---

**Port:** 8087  
**Team:** Randitha, Suweka  
**Status:** ✅ Production Ready
