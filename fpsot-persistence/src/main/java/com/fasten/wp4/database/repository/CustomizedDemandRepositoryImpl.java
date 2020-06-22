package com.fasten.wp4.database.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.fasten.wp4.database.config.MatrixArrayType;
import com.vladmihalcea.hibernate.type.array.IntArrayType;

class CustomizedDemandRepositoryImpl implements CustomizedDemandRepository {

	@PersistenceContext
	private EntityManager entityManager;


	@Override
	public int[] retrieveDemandOrdersByTacticalOptimizationOrderByDistributionCenterId(Long tacticalOptimizationId) {
		int[] result = (int[]) entityManager
				.createNativeQuery(
						"select array (select count(*)\\:\\:INTEGER from demand d " + 
								"where d.order_date >= ( " + 
								"	select t.initial_date from tactical_optimization t where t.id=:tacticalOptimizationId " + 
								") and d.order_date <= ( " + 
								"	select t.end_date from tactical_optimization t where t.id=:tacticalOptimizationId " + 
								")  " + 
								"group by d.distribution_center_id  " + 
						"order by d.distribution_center_id asc) as vector")
				.setParameter("tacticalOptimizationId", tacticalOptimizationId)
				.unwrap(org.hibernate.query.NativeQuery.class)
				.addScalar("vector", IntArrayType.INSTANCE)
				.getSingleResult();

		return result;
	}
	
	@Override
	public int[][] retrieveDemandPartsByTacticalOptimizationOrderByDistributionCenterId(Long tacticalOptimizationId){
		int[][] result = (int[][]) entityManager
				.createNativeQuery(
						"select array (\n" + 
						"	select array_agg(quantity\\:\\:INTEGER ORDER BY distribution_center_id asc, part_id asc) as column from ( " + 
						"		select t1.distribution_center_id, t1.part_id, case when (t2.quantity is null) then 0 else t2.quantity end as quantity  " + 
						"		from ( " + 
						"			select distribution_center.id as distribution_center_id, part.id as part_id from distribution_center, part  " + 
						"				where distribution_center.id in ( " + 
						"						select d.distribution_center_id from demand d where d.order_date >= ( " + 
						"							select t.initial_date from tactical_optimization t where t.id=:tacticalOptimizationId " + 
						"						) and d.order_date <= (  " + 
						"							select t.end_date from tactical_optimization t where t.id=:tacticalOptimizationId " + 
						"						)  " + 
						"				) and part.id in ( " + 
						"						select d.part_id from demand d where d.order_date >= (  " + 
						"							select t.initial_date from tactical_optimization t where t.id=:tacticalOptimizationId " + 
						"						) and d.order_date <= (  " + 
						"							select t.end_date from tactical_optimization t where t.id=:tacticalOptimizationId " + 
						"						) " + 
						"				) order by distribution_center_id, part_id " + 
						"			) t1  " + 
						"		left outer join ( " + 
						"			select d.distribution_center_id, d.part_id ,sum(quantity) as quantity from demand d  " + 
						"				where d.order_date >= ( " + 
						"					select t.initial_date from tactical_optimization t where t.id=:tacticalOptimizationId " + 
						"				) and d.order_date <= ( " + 
						"					select t.end_date from tactical_optimization t where t.id=:tacticalOptimizationId " + 
						"				) group by d.distribution_center_id, d.part_id  " + 
						"			order by d.distribution_center_id asc, d.part_id asc " + 
						"		) t2 " + 
						"		on(t1.distribution_center_id = t2.distribution_center_id and t1.part_id = t2.part_id) " + 
						"	) t3 group by distribution_center_id order by distribution_center_id " + 
						") as line")
				.setParameter("tacticalOptimizationId", tacticalOptimizationId)
				.unwrap(org.hibernate.query.NativeQuery.class)
				.addScalar("line", MatrixArrayType.INSTANCE) 
				.getSingleResult();
		return result;
	}

	@Override
	public int[][] retrieveDemandOrdersPartsByTacticalOptimizationOrderByDistributionCenterId(Long tacticalOptimizationId){
		int[][] result = (int[][]) entityManager
				.createNativeQuery(
						"select array (\n" + 
								"	select array_agg(numberOforders\\:\\:INTEGER ORDER BY distribution_center_id asc, part_id asc) as column from ( " + 
								"		select t1.distribution_center_id, t1.part_id, case when (t2.numberOforders is null) then 0 else t2.numberOforders end as numberOforders  " + 
								"		from ( " + 
								"			select distribution_center.id as distribution_center_id, part.id as part_id from distribution_center, part  " + 
								"				where distribution_center.id in ( " + 
								"						select d.distribution_center_id from demand d where d.order_date >= ( " + 
								"							select t.initial_date from tactical_optimization t where t.id=:tacticalOptimizationId " + 
								"						) and d.order_date <= (  " + 
								"							select t.end_date from tactical_optimization t where t.id=:tacticalOptimizationId " + 
								"						)  " + 
								"				) and part.id in ( " + 
								"						select d.part_id from demand d where d.order_date >= (  " + 
								"							select t.initial_date from tactical_optimization t where t.id=:tacticalOptimizationId " + 
								"						) and d.order_date <= (  " + 
								"							select t.end_date from tactical_optimization t where t.id=:tacticalOptimizationId " + 
								"						) " + 
								"				) order by distribution_center_id, part_id " + 
								"			) t1  " + 
								"		left outer join ( " + 
								"			select d.distribution_center_id, d.part_id ,count(*) as numberOfOrders from demand d  " + 
								"				where d.order_date >= ( " + 
								"					select t.initial_date from tactical_optimization t where t.id=:tacticalOptimizationId " + 
								"				) and d.order_date <= ( " + 
								"					select t.end_date from tactical_optimization t where t.id=:tacticalOptimizationId " + 
								"				) group by d.distribution_center_id, d.part_id  " + 
								"			order by d.distribution_center_id asc, d.part_id asc " + 
								"		) t2 " + 
								"		on(t1.distribution_center_id = t2.distribution_center_id and t1.part_id = t2.part_id) " + 
								"	) t3 group by distribution_center_id order by distribution_center_id " + 
						") as line")
				.setParameter("tacticalOptimizationId", tacticalOptimizationId)
				.unwrap(org.hibernate.query.NativeQuery.class)
				.addScalar("line", MatrixArrayType.INSTANCE) 
				.getSingleResult();
		return result;
	}
}
