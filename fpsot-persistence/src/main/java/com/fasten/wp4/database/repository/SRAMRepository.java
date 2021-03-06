package com.fasten.wp4.database.repository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.fasten.wp4.database.model.SRAM;

@Repository
public interface SRAMRepository extends JpaRepository<SRAM, Long>,  JpaLazyDataModel<SRAM>{

	@Transactional
	Optional<SRAM> findByCode(String code);
	
	List<SRAM> findByCodeIgnoreCaseContaining(String code);

	@Query("select distinct(s.code) from SRAM s order by s.code asc")
	List<String> findAllCodeOrderByCode();

}
