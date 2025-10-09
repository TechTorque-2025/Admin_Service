package com.techtorque.admin_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "service_types")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceType {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(nullable = false, unique = true)
  private String name;

  private String description;

  @Column(nullable = false)
  private BigDecimal price;

  @Column(nullable = false)
  private int defaultDurationMinutes;

  private String category;
}