package com.fasten.wp4.optimizator.tactical.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.optimizator.tactical.model.Route;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long>,  JpaLazyDataModel<Route>{

}
