package com.fasten.wp4.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fasten.wp4.database.model.Demand;
import com.fasten.wp4.database.model.ProcessingPart;

@Repository
public interface ProcessingPartRepository extends JpaRepository<ProcessingPart, Long>, JpaLazyDataModel<ProcessingPart>{
	
	@Query("SELECT processingPart FROM ProcessingPart processingPart order by processingPart.SRAM.code asc, processingPart.part.id asc")
	List<ProcessingPart> retrieveAllOrderBySramCode();

	@Query("SELECT processingPart FROM ProcessingPart processingPart WHERE processingPart.part.code = :partCode and processingPart.SRAM.code = :SRAMCode")
	Optional<ProcessingPart> findByPartCodeAndSRAMCode(@Param("partCode") String partCode, @Param("SRAMCode") String SRAMCode);

	List<ProcessingPart> findByPartNameIgnoreCase(String partName);
	
	@Query("SELECT ceil(coalesce(max( (processingPart.avgProducingTime + processingPart.stdProducingTime + processingPart.avgSetupTime + processingPart.stdSetupTime) ), 0)/3600) FROM ProcessingPart processingPart")
	Long retrieveMaxHoursProcessing();

	@Query("select max((processingPart.avgProducingTime + processingPart.stdProducingTime + processingPart.avgSetupTime + processingPart.stdSetupTime)*d.quantity)/3600"
			+ " from ProcessingPart processingPart "
			+ " inner join Demand d on processingPart.part.id = d.part.id "
			+ " where d.orderDate >= (select t.initialDate from TacticalOptimization t where t.id=:tacticalOptimizationId) and "
			+ " d.orderDate <= (select t.endDate from TacticalOptimization t where t.id=:tacticalOptimizationId) "
			+ " and processingPart.SRAM.id=:sramId")
	Double retrieveMinProcessingPartByTacticalOptimization(@Param("tacticalOptimizationId") Long tacticalOptimizationId, @Param("sramId") Long sramId);

}

//	@SuppressWarnings({"rawtypes","unchecked"})
//	default Page<ProcessingPart> filter(Map<String, String> filters, Pageable pageable){
//
//		Specification<ProcessingPart> specs = (root, query, builder) -> {
//			final List<Predicate> predicates = new ArrayList<>();
//		    for (Entry<String, String> filter : filters.entrySet()) {
//		        Path path = getPropertyOrderPath(root, filter.getKey());
//		        predicates.add(
//		        		filter.getValue().matches("[0-9]+")? builder.equal(path, Long.valueOf(filter.getValue())) :
//		        			filter.getValue().matches("\\d+\\.\\d+")? builder.equal(path, Double.valueOf(filter.getValue())) :
//		        			builder.like(builder.upper(path), "%" + filter.getValue().toUpperCase() + "%"));
//		    }
//
//			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
//		};
//
//		return findAll(specs, pageable);
//	}
//	
//	default  <E> Path<?> getPropertyOrderPath(Root<E> root, String propertyPath) {
//		String[] pathItems = propertyPath.split("\\.");
//	    String pathItem = pathItems[0];
//	    Path<?> path = root.get(pathItem);
//	    for (int i = 1; i < pathItems.length; i++) {
//	        path = path.get(pathItems[i]);
//	    }
//	    return path;
//	}

//default Page<ProcessingPart> filter(ProcessingPart processingPart, Pageable pageable){
//	Specification<ProcessingPart> specs = (root, query, builder) -> {
//		final List<Predicate> predicates = new ArrayList<>();
//		if(processingPart!=null) {
//			predicates.add(QueryByExamplePredicateBuilder.getPredicate(root, builder, Example.of(processingPart)));
//		}
//		return builder.and(predicates.toArray(new Predicate[predicates.size()]));
//	};
//	return findAll(specs, pageable);
//}
//default Page<ProcessingPart> filter(Filter<ProcessingPart> filter, Pageable pageable) {
//	Specification<ProcessingPart> specs = (root, query, builder) -> {
//		final List<Predicate> predicates = new ArrayList<>();
//		if(filter.hasParam("id")) {
//			predicates.add(builder.equal(root.get("id"), filter.getParam("id")));
//		}
//		if(filter.hasParam("cost")) {
//			predicates.add(builder.equal(root.get("cost"), filter.getParam("cost")));
//		}
//		if(filter.hasParam("avgSetupTime")) {
//			predicates.add(builder.equal(root.get("avgSetupTime"), filter.getParam("avgSetupTime")));
//		}
//		if(filter.hasParam("avgProducingTime")) {
//			predicates.add(builder.equal(root.get("avgProducingTime"), filter.getParam("avgProducingTime")));
//		}
//		if(filter.hasParam("stdSetupTime")) {
//			predicates.add(builder.equal(root.get("stdSetupTime"), filter.getParam("stdSetupTime")));
//		}
//		if(filter.hasParam("stdProducingTime")) {
//			predicates.add(builder.equal(root.get("stdProducingTime"), filter.getParam("stdProducingTime")));
//		}
//		if(filter.hasParam("part.name")) {
//			predicates.add(builder.equal(root.get("part").get("name"), filter.getParam("part.name")));
//		}
//		if(filter.hasParam("sram.code")) {
//			predicates.add(builder.equal(root.get("SRAM").get("code"), filter.getParam("sram.code")));
//		}
//		//			if(filter.hasParam("")) {
//		//				predicates.add(builder.equal(root.get(""), filter.getParam("")));
//		//			}
//		//			if(filter.hasParam("")) {
//		//				predicates.add(builder.equal(root.get(""), filter.getParam("")));
//		//			}
//
//		if(filter.hasParam("minAvgProducingTime") && filter.hasParam("maxAvgProducingTime")) {
//			predicates.add(builder.between(root.get("avgProducingTime"), filter.getParam("minAvgProducingTime"), filter.getParam("maxAvgProducingTime")));
//		}else if(filter.hasParam("minAvgProducingTime") && !filter.hasParam("maxAvgProducingTime")) {
//			predicates.add(builder.greaterThanOrEqualTo(root.get("avgProducingTime"), filter.getParam("minAvgProducingTime")));
//		}else if(!filter.hasParam("minAvgProducingTime") && filter.hasParam("maxAvgProducingTime")) {
//			predicates.add(builder.lessThanOrEqualTo(root.get("avgProducingTime"), filter.getParam("maxAvgProducingTime")));
//		}
//		if(filter.hasParam("minAvgSetupTime") && filter.hasParam("maxAvgSetupTime")) {
//			predicates.add(builder.between(root.get("avgSetupTime"), filter.getParam("minAvgSetupTime"), filter.getParam("maxAvgSetupTime")));
//		}else if(filter.hasParam("minAvgSetupTime") && !filter.hasParam("maxAvgSetupTime")) {
//			predicates.add(builder.greaterThanOrEqualTo(root.get("avgSetupTime"), filter.getParam("minAvgSetupTime")));
//		}else if(!filter.hasParam("minAvgSetupTime") && filter.hasParam("maxAvgSetupTime")) {
//			predicates.add(builder.lessThanOrEqualTo(root.get("avgSetupTime"), filter.getParam("maxAvgSetupTime")));
//		}
//		if(filter.hasParam("minStdProducingTime") && filter.hasParam("maxStdProducingTime")) {
//			predicates.add(builder.between(root.get("stdProducingTime"), filter.getParam("minStdProducingTime"), filter.getParam("maxStdProducingTime")));
//		}else if(filter.hasParam("minStdProducingTime") && !filter.hasParam("maxStdProducingTime")) {
//			predicates.add(builder.greaterThanOrEqualTo(root.get("stdProducingTime"), filter.getParam("minStdProducingTime")));
//		}else if(!filter.hasParam("minStdProducingTime") && filter.hasParam("maxStdProducingTime")) {
//			predicates.add(builder.lessThanOrEqualTo(root.get("stdProducingTime"), filter.getParam("maxStdProducingTime")));
//		}
//		if(filter.hasParam("minStdSetupTime") && filter.hasParam("maxStdSetupTime")) {
//			predicates.add(builder.between(root.get("stdSetupTime"), filter.getParam("minStdSetupTime"), filter.getParam("maxStdSetupTime")));
//		}else if(filter.hasParam("minStdSetupTime") && !filter.hasParam("maxStdSetupTime")) {
//			predicates.add(builder.greaterThanOrEqualTo(root.get("stdSetupTime"), filter.getParam("minStdSetupTime")));
//		}else if(!filter.hasParam("minStdSetupTime") && filter.hasParam("maxStdSetupTime")) {
//			predicates.add(builder.lessThanOrEqualTo(root.get("stdSetupTime"), filter.getParam("maxStdSetupTime")));
//		}
//		if(filter.hasParam("minCost") && filter.hasParam("maxCost")) {
//			predicates.add(builder.between(root.get("cost"), filter.getParam("minCost"), filter.getParam("maxCost")));
//		}else if(filter.hasParam("minCost") && !filter.hasParam("maxCost")) {
//			predicates.add(builder.greaterThanOrEqualTo(root.get("cost"), filter.getParam("minCost")));
//		}else if(!filter.hasParam("minCost") && filter.hasParam("maxCost")) {
//			predicates.add(builder.lessThanOrEqualTo(root.get("cost"), filter.getParam("maxCost")));
//		}
//		if(filter.getEntity()!=null) {
//			predicates.add(getPredicate(root, builder, Example.of(filter.getEntity())));
//		}
//
//
//		return builder.and(predicates.toArray(new Predicate[predicates.size()]));
//	};
//
//	return findAll(specs, pageable);
//}

//if(filter.get("id")!=null) {
//predicates.add(builder.equal(root.get("id"), filter.get("id")));
//}
//if(filter.get("cost")!=null) {
//predicates.add(builder.equal(root.get("cost"), filter.get("cost")));
//}
//if(filter.get("avgSetupTime")!=null) {
//predicates.add(builder.equal(root.get("avgSetupTime"), filter.get("avgSetupTime")));
//}
//if(filter.get("avgProducingTime")!=null) {
//predicates.add(builder.equal(root.get("avgProducingTime"), filter.get("avgProducingTime")));
//}
//if(filter.get("stdSetupTime")!=null) {
//predicates.add(builder.equal(root.get("stdSetupTime"), filter.get("stdSetupTime")));
//}
//if(filter.get("stdProducingTime")!=null) {
//predicates.add(builder.equal(root.get("stdProducingTime"), filter.get("stdProducingTime")));
//}
//if(filter.get("part.name")!=null) {
//predicates.add(builder.equal(root.get("part").get("name"), filter.get("part.name")));
//}
//if(filter.get("SRAM.code")!=null) {
//predicates.add(builder.equal(root.get("SRAM").get("code"), filter.get("sram.code")));
//}

//@SuppressWarnings("rawtypes")
//default Path<?> getPath(String field, Root root, Join join) {
//    String[] fields = field.split("\\.", 2);
//    return fields.length == 1 ? root.get(fields[0]): join.get(fields[1]);
//}


//	default public Specification<ProcessingPart> getSpecsFromFilter(Filter<ProcessingPart> filter) {
//		
//		return (Specification<ProcessingPart>) (root, query, builder) -> {
//			final List<Predicate> predicates = new ArrayList<>();
//			if(filter.hasParam("id")) {
//				predicates.add(builder.equal(root.get("id"), filter.getParam("id")));
//			}
//			if(filter.hasParam("minAvgProducingTime") && filter.hasParam("maxAvgProducingTime")) {
//				predicates.add(builder.between(root.get("avgProducingTime"), filter.getParam("minAvgProducingTime"), filter.getParam("maxAvgProducingTime")));
//			}else if(filter.hasParam("minAvgProducingTime") && !filter.hasParam("maxAvgProducingTime")) {
//				predicates.add(builder.greaterThanOrEqualTo(root.get("avgProducingTime"), filter.getParam("minAvgProducingTime")));
//			}else if(!filter.hasParam("minAvgProducingTime") && filter.hasParam("maxAvgProducingTime")) {
//				predicates.add(builder.lessThanOrEqualTo(root.get("avgProducingTime"), filter.getParam("maxAvgProducingTime")));
//			}
//			if(filter.hasParam("minAvgSetupTime") && filter.hasParam("maxAvgSetupTime")) {
//				predicates.add(builder.between(root.get("avgSetupTime"), filter.getParam("minAvgSetupTime"), filter.getParam("maxAvgSetupTime")));
//			}else if(filter.hasParam("minAvgSetupTime") && !filter.hasParam("maxAvgSetupTime")) {
//				predicates.add(builder.greaterThanOrEqualTo(root.get("avgSetupTime"), filter.getParam("minAvgSetupTime")));
//			}else if(!filter.hasParam("minAvgSetupTime") && filter.hasParam("maxAvgSetupTime")) {
//				predicates.add(builder.lessThanOrEqualTo(root.get("avgSetupTime"), filter.getParam("maxAvgSetupTime")));
//			}
//			if(filter.hasParam("minStdProducingTime") && filter.hasParam("maxStdProducingTime")) {
//				predicates.add(builder.between(root.get("stdProducingTime"), filter.getParam("minStdProducingTime"), filter.getParam("maxStdProducingTime")));
//			}else if(filter.hasParam("minStdProducingTime") && !filter.hasParam("maxStdProducingTime")) {
//				predicates.add(builder.greaterThanOrEqualTo(root.get("stdProducingTime"), filter.getParam("minStdProducingTime")));
//			}else if(!filter.hasParam("minStdProducingTime") && filter.hasParam("maxStdProducingTime")) {
//				predicates.add(builder.lessThanOrEqualTo(root.get("stdProducingTime"), filter.getParam("maxStdProducingTime")));
//			}
//			if(filter.hasParam("minStdSetupTime") && filter.hasParam("maxStdSetupTime")) {
//				predicates.add(builder.between(root.get("stdSetupTime"), filter.getParam("minStdSetupTime"), filter.getParam("maxStdSetupTime")));
//			}else if(filter.hasParam("minStdSetupTime") && !filter.hasParam("maxStdSetupTime")) {
//				predicates.add(builder.greaterThanOrEqualTo(root.get("stdSetupTime"), filter.getParam("minStdSetupTime")));
//			}else if(!filter.hasParam("minStdSetupTime") && filter.hasParam("maxStdSetupTime")) {
//				predicates.add(builder.lessThanOrEqualTo(root.get("stdSetupTime"), filter.getParam("maxStdSetupTime")));
//			}
//			if(filter.hasParam("minCost") && filter.hasParam("maxCost")) {
//				predicates.add(builder.between(root.get("cost"), filter.getParam("minCost"), filter.getParam("maxCost")));
//			}else if(filter.hasParam("minCost") && !filter.hasParam("maxCost")) {
//				predicates.add(builder.greaterThanOrEqualTo(root.get("cost"), filter.getParam("minCost")));
//			}else if(!filter.hasParam("minCost") && filter.hasParam("maxCost")) {
//				predicates.add(builder.lessThanOrEqualTo(root.get("cost"), filter.getParam("maxCost")));
//			}
//			if(filter.getEntity()!=null) {
//				predicates.add(getPredicate(root, builder, Example.of(filter.getEntity())));
//			}
//			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
//		};
//	}

//static Specification<ProcessingPart> hasId(Long id) {
//	return (processingPart, cq, cb) -> cb.equal(processingPart.get("id"), id);
//}
//
//static Specification<ProcessingPart> hasAvgProducingTimeBetween( Double minAvgProducingTime, Double maxAvgProducingTime) {
//	if (minAvgProducingTime!=null && maxAvgProducingTime!=null) {
//		return (processingPart, cq, cb) -> cb.between(processingPart.get("avgProducingTime"), minAvgProducingTime, maxAvgProducingTime);
//	}else if(minAvgProducingTime!=null) {
//		return (processingPart, cq, cb) -> cb.greaterThanOrEqualTo(processingPart.get("avgProducingTime"), minAvgProducingTime);
//	}else /*if(maxAvgProducingTime!=null)*/ {
//		return (processingPart, cq, cb) -> cb.lessThanOrEqualTo(processingPart.get("avgProducingTime"), maxAvgProducingTime);
//	}
//}
//
//static Specification<ProcessingPart> hasAvgSetupTimeBetween( Double minAvgProducingTime, Double maxAvgProducingTime) {
//	if (minAvgProducingTime!=null && maxAvgProducingTime!=null) {
//		return (processingPart, cq, cb) -> cb.between(processingPart.get("avgSetupTime"), minAvgProducingTime, maxAvgProducingTime);
//	}else if(minAvgProducingTime!=null) {
//		return (processingPart, cq, cb) -> cb.greaterThanOrEqualTo(processingPart.get("avgSetupTime"), minAvgProducingTime);
//	}else /*if(maxAvgProducingTime!=null)*/ {
//		return (processingPart, cq, cb) -> cb.lessThanOrEqualTo(processingPart.get("avgSetupTime"), maxAvgProducingTime);
//	}
//}
//
//static Specification<ProcessingPart> hasPartName(String partName) {
//	return (processingPart, cq, cb) -> cb.equal(processingPart.get("part.name"), partName);
//}
//
//static Specification<ProcessingPart> hasSRAMCode(String SRAMCode) {
//	return (processingPart, cq, cb) -> cb.equal(processingPart.get("SRAM.code"), SRAMCode);
//}
//
//static Specification<ProcessingPart> hasExample(Example<ProcessingPart> example) {
//	return (processingPart, cq, cb) -> getPredicate(processingPart, cb, example);
//}
//		Map<String,String> params = filter.getParams();
//		ProcessingPart example = filter.getEntity();
//		List<Specification<ProcessingPart>> predicates = new ArrayList<>();
//		if (params.containsKey("id") && params.get("id") != null) {
//			predicates.add(hasId(Long.valueOf(params.get("id"))));
//		}
//		if ( (params.containsKey("minAvgProducingTime") && params.get("minAvgProducingTime") != null) || (params.containsKey("maxAvgProducingTime") && params.get("maxAvgProducingTime") != null)) {
//			predicates.add(hasAvgProducingTimeBetween(Double.valueOf(params.get("minAvgProducingTime")), Double.valueOf(params.get("maxAvgProducingTime"))));
//		}
//		if ( (params.containsKey("minAvgSetupTime") && params.get("minAvgSetupTime") != null) || (params.containsKey("maxAvgSetupTime") && params.get("maxAvgSetupTime") != null)) {
//			predicates.add(hasAvgSetupTimeBetween(Double.valueOf(params.get("minAvgSetupTime")), Double.valueOf(params.get("maxAvgSetupTime"))));
//		}
//		if(example!=null) {
//			predicates.add(hasExample(Example.of(example)));
//		}
//
//		Specification<ProcessingPart> specs = (root, cq, cb)->{ return cb.and(predicates.toArray(new Predicate[predicates.size()]));};


//	    	if((processingPart.getPart()!=null) && (processingPart.getPart().getName()!=null && processingPart.getPart().getName().trim().length() > 0)) {
//    			predicates.add(hasPartName(processingPart.getPart().getName()));
//	    	}
//	    	if((processingPart.getSRAM()!=null) && (processingPart.getSRAM().getCode()!=null && processingPart.getSRAM().getCode().trim().length()>0)){
//	    		predicates.add(hasSRAMCode(processingPart.getSRAM().getCode()));
//	    	}