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

	@Query("select remoteStation from RemoteStation remoteStation where remoteStation.id in ( "
			+ "Select d.remoteStation.id from Demand d "
			+ " where d.orderDate >= (select t.initialDate from TacticalOptimization t where t.id=:id) and "
			+ " d.orderDate <= (select t.endDate from TacticalOptimization t where t.id=:id) "
			+ ") order by remoteStation.code ")
	List<RemoteStation> retrieveRemoteStationByTacticalOptimization(@Param("id") Long id);
	
	@Query("select r.name from RemoteStation r order by r.name asc")
	List<String> findAllByNameOrderByName();

	Optional<RemoteStation> findByName(String name);

	@Query("select remoteStation from RemoteStation remoteStation where upper(regexp_replace(remoteStation.name, '\\s*', '', 'g'))=:remoteStationName")
	Optional<RemoteStation> findByExcellName(String remoteStationName);

}
