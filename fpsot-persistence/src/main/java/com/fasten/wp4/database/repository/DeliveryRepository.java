package com.fasten.wp4.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.database.model.Delivery;
import com.fasten.wp4.database.model.RemoteStation;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long>, JpaLazyDataModel<Delivery>{

	Optional<Delivery> findByOriginAndDestination(RemoteStation origin, RemoteStation destination);
	Optional<Delivery> findByOriginCodeAndDestinationCode(String originCode, String destinationCode);
	
	@Query("Select delivery from Delivery delivery where delivery.origin 	in ( Select distinct(d.remoteStation) from Demand d where d.orderDate >= (select t.initialDate from TacticalOptimization t where t.id=:id) and d.orderDate <= (select t.endDate from TacticalOptimization t where t.id=:id)) "
										+ "and delivery.destination in ( Select distinct(d.remoteStation) from Demand d where d.orderDate >= (select t.initialDate from TacticalOptimization t where t.id=:id) and d.orderDate <= (select t.endDate from TacticalOptimization t where t.id=:id)) "
										+ "order by delivery.origin.code asc, delivery.destination.code asc")
	List<Delivery> retrieveDeliveryMatrixByTacticalOptimization(@Param("id") Long id);

}
