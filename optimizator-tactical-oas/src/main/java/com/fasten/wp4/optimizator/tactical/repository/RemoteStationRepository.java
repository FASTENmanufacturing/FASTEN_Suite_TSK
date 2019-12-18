package com.fasten.wp4.optimizator.tactical.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.optimizator.tactical.model.City;
import com.fasten.wp4.optimizator.tactical.model.RemoteStation;
import com.fasten.wp4.optimizator.tactical.model.RemoteStationPriority;

@Repository
public interface RemoteStationRepository extends JpaRepository<RemoteStation, Long>,  JpaLazyDataModel<RemoteStation>{

	Optional<RemoteStation> findByCode(String code);

	List<RemoteStation> findByPriority(RemoteStationPriority priority);
	
	List<RemoteStation> findByCityName(String cityName);

	@Query("select r.name from RemoteStation r order by r.name asc")
	List<String> findAllByNameOrderByName();

	Optional<RemoteStation> findByName(String name);

}
