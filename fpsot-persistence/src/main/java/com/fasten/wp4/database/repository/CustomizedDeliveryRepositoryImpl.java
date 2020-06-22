package com.fasten.wp4.database.repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.fasten.wp4.database.config.MatrixArrayType;

class CustomizedDeliveryRepositoryImpl implements CustomizedDeliveryRepository {

	@PersistenceContext
    private EntityManager entityManager;
	
	
	@Override
	public int[][] retrieveDistanceMatrix(int[] distributionCenters, int[] remoteStations){
		
//		Object o = entityManager
//				.createNativeQuery(
//						"select d1.origin_id, "
//								+ " array_agg(d1.distance ORDER BY d1.destination_id asc) as column "
//								+ "from delivery d1 "
//								+ "where d1.origin_id in (1,2,3,4) "
//								+ "and d1.destination_id in (1,2,3,4) "
//								+ "group by d1.origin_id "
//								+ "order by d1.origin_id")
//				.unwrap(org.hibernate.query.NativeQuery.class)
//				.addScalar("column", IntArrayType.INSTANCE)
//				.getResultList();
		
//		Array list = entityManager.unwrap(Connection.class).createArrayOf("int8", distributionCenters);
		
		List<Integer> distributionCentersArr =Arrays.stream(distributionCenters).boxed().collect(Collectors.toList()); 
		List<Integer> remoteStationsArr =Arrays.stream(remoteStations).boxed().collect(Collectors.toList()); 
		
		int[][] result = (int[][]) entityManager
				.createNativeQuery(
						"select array( "
								+ "select array_agg(d1.distance ORDER BY d1.destination_id asc) as column "
								+ "from delivery d1 "
								+ "where d1.origin_id in (:origins) "
								+ "and d1.destination_id in (:destinations) "
								+ "group by d1.origin_id "
								+ "order by d1.origin_id) as line")
				.setParameter("origins", distributionCentersArr)
				.setParameter("destinations", remoteStationsArr)
				.unwrap(org.hibernate.query.NativeQuery.class)
				.addScalar("line", MatrixArrayType.INSTANCE)
//				.addScalar("column", IntArrayType.INSTANCE)
				.getSingleResult();
		
		return result;
	}

	@Override
	public int[][] retrieveTimeMatrix(int[] distributionCenters, int[] remoteStations){
		
//		Object o = entityManager
//				.createNativeQuery(
//						"select d1.origin_id, "
//								+ " array_agg(d1.time ORDER BY d1.destination_id asc) as column "
//								+ "from delivery d1 "
//								+ "where d1.origin_id in (1,2,3,4) "
//								+ "and d1.destination_id in (1,2,3,4) "
//								+ "group by d1.origin_id "
//								+ "order by d1.origin_id")
//				.unwrap(org.hibernate.query.NativeQuery.class)
//				.addScalar("column", IntArrayType.INSTANCE)
//				.getResultList();
		
//		Array list = entityManager.unwrap(Connection.class).createArrayOf("int8", distributionCenters);
		
		List<Integer> distributionCentersArr =Arrays.stream(distributionCenters).sorted().boxed().collect(Collectors.toList()); 
		List<Integer> remoteStationsArr =Arrays.stream(remoteStations).sorted().boxed().collect(Collectors.toList()); 
		
		int[][] result = (int[][]) entityManager
				.createNativeQuery(
						"select array( "
								+ "select array_agg(d1.time ORDER BY d1.destination_id asc) as column "
								+ "from delivery d1 "
								+ "where d1.origin_id in (:origins) "
								+ "and d1.destination_id in (:destinations) "
								+ "group by d1.origin_id "
								+ "order by d1.origin_id) as line")
				.setParameter("origins", distributionCentersArr)
				.setParameter("destinations", remoteStationsArr)
				.unwrap(org.hibernate.query.NativeQuery.class)
				.addScalar("line", MatrixArrayType.INSTANCE)
//				.addScalar("column", IntArrayType.INSTANCE)
				.getSingleResult();
		
		return result;
	}
}
