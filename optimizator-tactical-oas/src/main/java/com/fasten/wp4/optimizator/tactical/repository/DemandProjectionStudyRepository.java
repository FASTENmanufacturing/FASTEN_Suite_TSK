package com.fasten.wp4.optimizator.tactical.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.optimizator.tactical.model.DemandProjectionStudy;
import com.fasten.wp4.optimizator.tactical.model.Prediction;

@Repository
public interface DemandProjectionStudyRepository extends JpaRepository<DemandProjectionStudy, Long>,  JpaLazyDataModel<DemandProjectionStudy>{

	Optional<DemandProjectionStudy> findByStudy(Prediction study);
	
	boolean existsByStudy(Prediction study);
	
	@Transactional
	void deleteByStudy(Prediction study);
	
}
