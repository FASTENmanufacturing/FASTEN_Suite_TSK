package com.fasten.wp4.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.database.model.OperationalOptimization;

@Repository
public interface OperationalOptimizationRepository extends JpaRepository<OperationalOptimization, Long>,  JpaLazyDataModel<OperationalOptimization>{

}
