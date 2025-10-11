# 📊 Admin & Reporting Service

## 🚦 Build Status

**main**

[![Build and Test Admin Service](https://github.com/TechTorque-2025/Admin_Service/actions/workflows/buildtest.yaml/badge.svg)](https://github.com/TechTorque-2025/Admin_Service/actions/workflows/buildtest.yaml)

**dev**

[![Build and Test Admin Service](https://github.com/TechTorque-2025/Admin_Service/actions/workflows/buildtest.yaml/badge.svg?branch=dev)](https://github.com/TechTorque-2025/Admin_Service/actions/workflows/buildtest.yaml)

This microservice provides administrative oversight and business intelligence. It acts as an **orchestrator**, securely communicating with other services to aggregate data and perform admin actions.

**Assigned Team:** Randitha, Suweka

### 🎯 Key Responsibilities

- **User Management:** (Via API calls to `auth-service`) List, view, and update user accounts and roles.
- **Service Configuration:** Manage the list of available `ServiceTypes`, including their pricing and durations.
- **Reporting & Analytics:** Generate on-demand reports by aggregating data from other services (e.g., revenue reports from `payment-service`).

### ⚙️ Tech Stack

![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white) ![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

- **Framework:** Java / Spring Boot
- **Database:** PostgreSQL
- **Security:** Spring Security (consumes JWTs)

### ℹ️ API Information

- **Local Port:** `8087`
- **Swagger UI:** [http://localhost:8087/swagger-ui.html](http://localhost:8087/swagger-ui.html)

### 🚀 Running Locally

This service is designed to be run as part of the main `docker-compose` setup from the project's root directory.

```bash
# From the root of the TechTorque-2025 project
docker-compose up --build admin-service
```
