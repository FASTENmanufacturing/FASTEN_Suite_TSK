package com.fasten.wp4.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.database.model.State;

@Repository
public interface StateRepository extends JpaRepository<State, Long>, JpaLazyDataModel<State>{

	List<State> findByNameIgnoreCaseContainingOrderByNameAsc(String name);
	
	List<State> findAllByOrderByNameAsc();

}
