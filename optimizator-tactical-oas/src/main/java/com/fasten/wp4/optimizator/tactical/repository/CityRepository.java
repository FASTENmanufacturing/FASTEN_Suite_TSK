package com.fasten.wp4.optimizator.tactical.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.optimizator.tactical.model.City;
import com.fasten.wp4.optimizator.tactical.model.State;

@Repository
public interface CityRepository extends JpaRepository<City, Long>, JpaLazyDataModel<City>{

	Optional<City> findByNameIgnoreCase(String name);
	
	List<City> findByNameIgnoreCaseContainingOrderByNameAsc(String name);
	
	List<City> findByState(State state);
	
	List<City> findByStateStateAcronym(String stateAcronym);
	
	List<City> findByCapital(Boolean capital);
	
	List<City> findAllByOrderByCapitalDesc();


}
