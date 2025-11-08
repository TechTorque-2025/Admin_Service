package com.techtorque.admin_service.service;

import com.techtorque.admin_service.dto.request.GenerateReportRequest;
import com.techtorque.admin_service.dto.response.ReportResponse;

import java.util.List;

public interface AdminReportService {
    ReportResponse generateReport(GenerateReportRequest request, String generatedBy);
    List<ReportResponse> getAllReports(int page, int limit);
    ReportResponse getReportById(String reportId);
}