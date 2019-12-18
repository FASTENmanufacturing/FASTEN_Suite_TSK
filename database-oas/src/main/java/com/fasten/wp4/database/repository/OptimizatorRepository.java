package com.fasten.wp4.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.database.model.Optimizator;

@Repository
public interface OptimizatorRepository extends JpaRepository<Optimizator, Long>,  JpaLazyDataModel<Optimizator>{

}
