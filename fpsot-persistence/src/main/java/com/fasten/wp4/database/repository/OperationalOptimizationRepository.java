package com.fasten.wp4.database.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.database.model.City;
import com.fasten.wp4.database.model.OperationalOptimization;

@Repository
public interface OperationalOptimizationRepository extends JpaRepository<OperationalOptimization, Long>,  JpaLazyDataModel<OperationalOptimization>{

	Optional<OperationalOptimization> findByOrderID(String orderID);
}
