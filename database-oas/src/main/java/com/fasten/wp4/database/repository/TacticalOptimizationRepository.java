package com.fasten.wp4.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.database.model.TacticalOptimization;

@Repository
public interface TacticalOptimizationRepository extends JpaRepository<TacticalOptimization, Long>,  JpaLazyDataModel<TacticalOptimization>{

}
