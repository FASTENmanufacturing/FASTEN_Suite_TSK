package com.fasten.wp4.database.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.database.model.OperationalOptimization;
import com.fasten.wp4.database.model.OperationalOptimizationResult;

@Repository
public interface OperationalOptimizationResultRepository extends JpaRepository<OperationalOptimizationResult, Long>, JpaLazyDataModel<OperationalOptimizationResult>{

	@Transactional
	void deleteByStudy(OperationalOptimization study);
	
	boolean existsByStudy(OperationalOptimization study);

	List<OperationalOptimizationResult> findByStudy(OperationalOptimization study);

}
