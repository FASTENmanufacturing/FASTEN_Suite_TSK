package com.fasten.wp4.optimizator.tactical.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.optimizator.tactical.model.SRAM;

@Repository
public interface SRAMRepository extends JpaRepository<SRAM, Long>,  JpaLazyDataModel<SRAM>{

	Optional<SRAM> findByCode(String code);
	
	List<SRAM> findByCodeIgnoreCaseContaining(String code);

	@Query("select distinct(s.code) from SRAM s order by s.code asc")
	List<String> findAllCodeOrderByCode();

}
