
# 📊 Admin & Reporting Service

This microservice provides administrative oversight and business intelligence. It acts as an **orchestrator**, securely communicating with other services to aggregate data and perform admin actions.

**Assigned Team:** Randitha, Suweka

### 🎯 Key Responsibilities

-   **User Management:** (Via API calls to `auth-service`) List, view, and update user accounts and roles.
-   **Service Configuration:** Manage the list of available `ServiceTypes`, including their pricing and durations.
-   **Reporting & Analytics:** Generate on-demand reports by aggregating data from other services (e.g., revenue reports from `payment-service`).

### ⚙️ Tech Stack

-   **Framework:** Java / Spring Boot
-   **Database:** PostgreSQL
-   **Security:** Spring Security (consumes JWTs)

### ℹ️ API Information

-   **Local Port:** `8087`
-   **Swagger UI:** [http://localhost:8087/swagger-ui.html](http://localhost:8087/swagger-ui.html)

### 🚀 Running Locally

This service is designed to be run as part of the main `docker-compose` setup from the project's root directory.

```bash
# From the root of the TechTorque-2025 project
docker-compose up --build admin-service