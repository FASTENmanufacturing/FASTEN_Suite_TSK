package com.fasten.wp4.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasten.wp4.database.model.DemandProjected;
import com.fasten.wp4.database.model.DemandProjectionStudy;

@Repository
public interface DemandProjectedRepository extends JpaRepository<DemandProjected, Long>,  JpaLazyDataModel<DemandProjected>{
	
	@Transactional
	void deleteByDemandProjectionStudy(DemandProjectionStudy demandProjectionStudy);

}
