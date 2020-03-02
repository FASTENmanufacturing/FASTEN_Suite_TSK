package com.fasten.wp4.optimizator.tactical.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.optimizator.tactical.model.OperationalOptimization;

@Repository
public interface OperationalOptimizationRepository extends JpaRepository<OperationalOptimization, Long>,  JpaLazyDataModel<OperationalOptimization>{

}
