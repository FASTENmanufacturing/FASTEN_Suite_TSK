package com.fasten.wp4.optimizator.tactical.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasten.wp4.optimizator.tactical.model.DemandProjected;
import com.fasten.wp4.optimizator.tactical.model.DemandProjectionStudy;

@Repository
public interface DemandProjectedRepository extends JpaRepository<DemandProjected, Long>,  JpaLazyDataModel<DemandProjected>{
	
	@Transactional
	void deleteByDemandProjectionStudy(DemandProjectionStudy demandProjectionStudy);

}
