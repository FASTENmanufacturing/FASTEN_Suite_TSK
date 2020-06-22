package com.fasten.wp4.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.database.model.DistributionCenter;
import com.fasten.wp4.database.model.DistributionCenterPriority;

@Repository
public interface DistributionCenterRepository extends JpaRepository<DistributionCenter, Long>,  JpaLazyDataModel<DistributionCenter>, CustomizedDistributionCenterRepository{

	Optional<DistributionCenter> findByCode(String code);

	List<DistributionCenter> findByPriority(DistributionCenterPriority priority);
	
	List<DistributionCenter> findByAddressCityName(String cityName);

	@Query("select distributionCenter from DistributionCenter distributionCenter where distributionCenter.id in ( "
			+ "Select d.distributionCenter.id from Demand d "
			+ " where d.orderDate >= (select t.initialDate from TacticalOptimization t where t.id=:id) and "
			+ " d.orderDate <= (select t.endDate from TacticalOptimization t where t.id=:id) "
			+ ") order by distributionCenter.code ")
	List<DistributionCenter> retrieveDistributionCenterByTacticalOptimization(@Param("id") Long id);

	@Query("select distributionCenter from DistributionCenter distributionCenter where distributionCenter.id in ( "
			+ "Select d.distributionCenter.id from Demand d "
			+ " where d.orderDate >= (select t.initialDate from TacticalOptimization t where t.id=:id) and "
			+ " d.orderDate <= (select t.endDate from TacticalOptimization t where t.id=:id) "
			+ ") order by distributionCenter.name ")
	List<DistributionCenter> retrieveDistributionCenterByTacticalOptimizationOrderByName(@Param("id") Long id);
	
	@Query("select r.name from DistributionCenter r order by r.name asc")
	List<String> findAllByNameOrderByName();

	Optional<DistributionCenter> findByName(String name);

	@Query("select distributionCenter from DistributionCenter distributionCenter where upper(regexp_replace(distributionCenter.name, '\\s*', '', 'g'))=:distributionCenterName")
	Optional<DistributionCenter> findByExcellName(String distributionCenterName);

}
