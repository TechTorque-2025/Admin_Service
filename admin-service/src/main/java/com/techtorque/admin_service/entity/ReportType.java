package com.techtorque.admin_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReportType {
    SERVICE_PERFORMANCE("Service Performance Report"),
    REVENUE("Revenue Report"),
    EMPLOYEE_PRODUCTIVITY("Employee Productivity Report"),
    CUSTOMER_SATISFACTION("Customer Satisfaction Report"),
    INVENTORY("Inventory Report"),
    APPOINTMENT_SUMMARY("Appointment Summary Report"),
    CUSTOM("Custom Report");

    private final String displayName;
}
