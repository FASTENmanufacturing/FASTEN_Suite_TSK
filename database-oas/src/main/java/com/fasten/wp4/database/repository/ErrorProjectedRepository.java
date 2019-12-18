package com.fasten.wp4.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasten.wp4.database.model.DemandProjectionStudy;
import com.fasten.wp4.database.model.ErrorProjected;

@Repository
public interface ErrorProjectedRepository extends JpaRepository<ErrorProjected, Long>,  JpaLazyDataModel<ErrorProjected>{
	
	@Transactional
	void deleteByDemandProjectionStudy(DemandProjectionStudy demandProjectionStudy);

}
