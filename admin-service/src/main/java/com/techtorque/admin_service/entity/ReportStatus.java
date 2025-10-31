package com.techtorque.admin_service.entity;

public enum ReportStatus {
    PENDING,     // Queued for generation
    GENERATING,  // Currently being generated
    COMPLETED,   // Successfully generated
    FAILED       // Generation failed
}
