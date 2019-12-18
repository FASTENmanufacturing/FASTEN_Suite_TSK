package com.fasten.wp4.optimizator.tactical.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.optimizator.tactical.model.InternalSupply;

@Repository
public interface InternalSupplyRepository extends JpaRepository<InternalSupply, Long>, JpaLazyDataModel<InternalSupply>{

}
