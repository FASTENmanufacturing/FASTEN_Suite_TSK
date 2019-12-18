package com.fasten.wp4.optimizator.tactical.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.optimizator.tactical.model.Part;
import com.fasten.wp4.optimizator.tactical.model.PartPriority;

@Repository
public interface PartRepository extends JpaRepository<Part, Long>, JpaLazyDataModel<Part>{

	Optional<Part> findByCode(String code);
	
	List<Part> findByNameLike(String name);

	Optional<Part> findByCodeAndPriority(String code,PartPriority priority);

	List<Part> findByPriority(PartPriority priority);

	List<Part> findByNameIgnoreCaseContaining(String name);

	@Query("select distinct(p.name) from Part p order by p.name asc")
	List<String> findAllNameOrderByName();

}