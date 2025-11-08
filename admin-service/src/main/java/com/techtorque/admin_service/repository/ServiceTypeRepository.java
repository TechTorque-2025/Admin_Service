package com.techtorque.admin_service.repository;

import com.techtorque.admin_service.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, String> {
	boolean existsByName(String name);
	List<ServiceType> findByActiveTrue();
}