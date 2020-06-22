package com.fasten.wp4.database.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.vladmihalcea.hibernate.type.array.DoubleArrayType;

class CustomizedProcessingPartRepositoryImpl implements CustomizedProcessingPartRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public double[] retrieveProcessingPartsByTacticalOptimizationOrderById(Long tacticalOptimizationId) {
		double[] result = (double[]) entityManager
				.createNativeQuery("select array ( " + 
						"select avg(average_print_time) from processing_part " + 
						" where part_id in (select distinct(part_id) from demand where " + 
						" demand.order_date >= (select initial_date from tactical_optimization where tactical_optimization.id=:tacticalOptimizationId) and  " + 
						" demand.order_date <= (select tactical_optimization.end_date from tactical_optimization where tactical_optimization.id=:tacticalOptimizationId)  " + 
						" order by part_id)  " + 
						"group by part_id order by part_id ) as vector")
				.setParameter("tacticalOptimizationId", tacticalOptimizationId)
				.unwrap(org.hibernate.query.NativeQuery.class)
				.addScalar("vector", DoubleArrayType.INSTANCE)
				.getSingleResult();

		return result;
	}
}
