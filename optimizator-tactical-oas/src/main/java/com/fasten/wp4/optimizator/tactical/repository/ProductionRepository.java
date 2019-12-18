package com.fasten.wp4.optimizator.tactical.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.optimizator.tactical.model.Production;

@Repository
public interface ProductionRepository extends JpaRepository<Production, Long>,  JpaLazyDataModel<Production>{

}
