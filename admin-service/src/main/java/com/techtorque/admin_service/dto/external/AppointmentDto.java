package com.techtorque.admin_service.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto {

  private String id;
  private String customerId;
  private String vehicleId;
  private Set<String> assignedEmployeeIds;
  private String assignedBayId;
  private String confirmationNumber;
  private String serviceType;
  private LocalDateTime requestedDateTime;
  private String status; // Using String to avoid enum dependency issues
  private String specialInstructions;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime vehicleArrivedAt;
  private String vehicleAcceptedByEmployeeId;
}
