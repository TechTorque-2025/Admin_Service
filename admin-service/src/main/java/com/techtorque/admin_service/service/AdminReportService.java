package com.techtorque.admin_service.service;

import com.techtorque.admin_service.dto.ReportRequestDto;
import com.techtorque.admin_service.dto.ReportResponseDto;

import java.util.List;

public interface AdminReportService {
  ReportResponseDto generateReport(ReportRequestDto dto, String adminId);
  List<ReportResponseDto> listGeneratedReports();
  ReportResponseDto getReportDetails(String reportId);
}