package com.fasten.wp4.database.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.database.model.TacticalOptimization;
import com.fasten.wp4.database.model.TacticalOptimizationResult;

@Repository
public interface TacticalOptimizationResultRepository extends JpaRepository<TacticalOptimizationResult, Long>, JpaLazyDataModel<TacticalOptimizationResult>{

	@Transactional
	void deleteByStudy(TacticalOptimization study);
	
	boolean existsByStudy(TacticalOptimization study);

	List<TacticalOptimizationResult> findByStudy(TacticalOptimization study);

}
