package com.fasten.wp4.database.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.Predicate;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.database.model.Demand;
import com.fasten.wp4.database.model.Part;
import com.fasten.wp4.database.model.Prediction;

@Repository
public interface DemandRepository extends JpaRepository<Demand, Long>,  JpaLazyDataModel<Demand>, CustomizedDemandRepository{

	Optional<Demand> findByCode(String code);

	@Query("Select count(distinct d.distributionCenter) from Demand d where d.orderDate >= :initialDate and d.orderDate <= :endDate")
	Integer retrieveCandidates(@Param("initialDate") Date initialDate, @Param("endDate") Date endDate);

	@Query("Select count(distinct d.distributionCenter) from Demand d where d.orderDate >= :initialDate and d.orderDate <= :endDate and (d.distributionCenter.address.latitude is null or d.distributionCenter.address.longitude is null)")
	Integer retrieveCandidatesWithoutCoordinates(@Param("initialDate") Date initialDate, @Param("endDate") Date endDate);

	@Query("Select count(d) from Demand d where d.orderDate >= :initialDate and d.orderDate <= :endDate")
	Integer retrieveQuantityBetween(@Param("initialDate") Date initialDate, @Param("endDate") Date endDate);

	@Query("Select d from Demand d where d.orderDate >= :initialDate and d.orderDate <= :endDate order by orderDate asc")
	List<Demand> retrieveBetween(@Param("initialDate") Date initialDate, @Param("endDate") Date endDate);
	
	@Query("Select d from Demand d where d.id >= :initialId and d.id <= :endId and d.part=:part order by orderDate asc")
	List<Demand> retrievePartBetweenIds(@Param("initialId") Long initialId, @Param("endId") Long endId, @Param("part") Part part);
	
	@Query("Select d from Demand d order by orderDate asc")
	List<Demand> retrieveAllOrderByOrderDate();
	
	@Query("Select d from Demand d where d.orderDate >= (select t.initialDate from TacticalOptimization t where t.id=:id) and d.orderDate <= (select t.endDate from TacticalOptimization t where t.id=:id)  order by d.code asc")
	List<Demand> retrieveDemandByTacticalOptimization(@Param("id") Long id);

	@Query("Select d from Demand d where d.orderDate >= (select t.initialDate from TacticalOptimization t where t.id=:id) and d.orderDate <= (select t.endDate from TacticalOptimization t where t.id=:id)  order by d.distributionCenter.name asc")
	List<Demand> retrieveDemandByTacticalOptimizationOrderByDistributionCenterName(@Param("id") Long id);

	@Query("Select max(d.orderDate)-min(d.orderDate) from Demand d where d.orderDate >= (select t.initialDate from TacticalOptimization t where t.id=:id) and d.orderDate <= (select t.endDate from TacticalOptimization t where t.id=:id) ")
	Integer retrieveHorizonByTacticalOptimization(@Param("id") Long id);
	
	default List<Demand> retrieveByPrediction(Prediction prediction) {
		Specification<Demand> specs = (root, query, builder) -> {
			final List<Predicate> predicates = new ArrayList<>();
			if(prediction.getInitialDate()!=null && prediction.getEndDate()!=null) {
				predicates.add(builder.between(root.get("orderDate"), prediction.getInitialDate(), prediction.getEndDate()));
			}else if(prediction.getInitialDate()!=null && prediction.getEndDate()==null) {
				predicates.add(builder.greaterThanOrEqualTo(root.get("orderDate"), prediction.getInitialDate()));
			}else if(prediction.getInitialDate()==null && prediction.getEndDate()!=null) {
				predicates.add(builder.lessThanOrEqualTo(root.get("orderDate"), prediction.getEndDate()));
			}
			if(prediction.getPart()!=null) {
				predicates.add(builder.equal(root.get("part"), prediction.getPart()));
			}
			if(prediction.getDistributionCenter()!=null) {
				predicates.add(builder.equal(root.get("distributionCenter"), prediction.getDistributionCenter()));
			}

			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
		
		return findAll(specs,Sort.by(Direction.ASC,"orderDate"));
	}
	
	default List<Demand> retrieveByPredictionParams(Date initialDate, Date endDate, Long distributionCenterId, Long partId) {
		Specification<Demand> specs = (root, query, builder) -> {
			final List<Predicate> predicates = new ArrayList<>();
			if(initialDate!=null && endDate!=null) {
				predicates.add(builder.between(root.get("orderDate"), initialDate, endDate));
			}else if(initialDate!=null && endDate==null) {
				predicates.add(builder.greaterThanOrEqualTo(root.get("orderDate"), initialDate));
			}else if(initialDate==null && endDate!=null) {
				predicates.add(builder.lessThanOrEqualTo(root.get("orderDate"), endDate));
			}
			if(partId!=null) {
				predicates.add(builder.equal(root.get("part").get("id"), partId));
			}
			if(distributionCenterId!=null) {
				predicates.add(builder.equal(root.get("distributionCenter").get("id"), distributionCenterId));
			}

			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};
		
		return findAll(specs,Sort.by(Direction.ASC,"orderDate"));
	}

}
