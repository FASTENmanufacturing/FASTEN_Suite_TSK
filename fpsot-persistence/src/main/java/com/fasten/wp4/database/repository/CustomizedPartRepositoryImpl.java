package com.fasten.wp4.database.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.vladmihalcea.hibernate.type.array.IntArrayType;
import com.vladmihalcea.hibernate.type.array.StringArrayType;

class CustomizedPartRepositoryImpl implements CustomizedPartRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public String[] retrievePartNamesByTacticalOptimizationOrderById(Long tacticalOptimizationId) {
		String[] result = (String[]) entityManager
				.createNativeQuery(
						"select array (select name from part where id in ( "
								+ "select part_id from demand "
								+ " where demand.order_date >= (select initial_date from tactical_optimization where tactical_optimization.id=:tacticalOptimizationId) and "
								+ " demand.order_date <= (select tactical_optimization.end_date from tactical_optimization where tactical_optimization.id=:tacticalOptimizationId) "
								+ ") order by part.id ) as vector")
				.setParameter("tacticalOptimizationId", tacticalOptimizationId)
				.unwrap(org.hibernate.query.NativeQuery.class)
				.addScalar("vector", StringArrayType.INSTANCE)
				.getSingleResult();

		return result;
	}

	@Override
	public int[] retrievePartIdsByTacticalOptimizationOrderById(Long tacticalOptimizationId) {
		int[] result = (int[]) entityManager
				.createNativeQuery(
						"select array (select id\\:\\:INTEGER from part where id in ( "
								+ "select part_id from demand "
								+ " where demand.order_date >= (select initial_date from tactical_optimization where tactical_optimization.id=:tacticalOptimizationId) and "
								+ " demand.order_date <= (select tactical_optimization.end_date from tactical_optimization where tactical_optimization.id=:tacticalOptimizationId) "
								+ ") order by part.id ) as vector")
				.setParameter("tacticalOptimizationId", tacticalOptimizationId)
				.unwrap(org.hibernate.query.NativeQuery.class)
				.addScalar("vector", IntArrayType.INSTANCE)
				.getSingleResult();
		return result;
	}

}
