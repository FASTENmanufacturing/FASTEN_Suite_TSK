package com.fasten.wp4.database.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JpaLazyDataModel<T> extends JpaSpecificationExecutor<T>{

	@SuppressWarnings({"rawtypes","unchecked"})
	default Page<T> filter(Map<String, String> filters, Pageable pageable){

		Specification<T> specs = (root, query, builder) -> {
			final List<Predicate> predicates = new ArrayList<>();
		    for (Entry<String, String> filter : filters.entrySet()) {
		        Path path = getPropertyOrderPath(root, filter.getKey());
		        if(path.getJavaType().isEnum()) {
					predicates.add(builder.equal(path, Enum.valueOf(path.getJavaType(), filter.getValue())));
				}else {
					predicates.add(
							filter.getValue().matches("[0-9]+")? builder.equal(path, Long.valueOf(filter.getValue())) :
								filter.getValue().matches("(?i)true|(?i)false")?builder.equal(path, Boolean.valueOf(filter.getValue())) :
								filter.getValue().matches("\\d+\\.\\d+")? builder.equal(path, Double.valueOf(filter.getValue())) :
									builder.like(builder.upper(path), "%" + filter.getValue().toUpperCase() + "%"));
				}
		    }

			return builder.and(predicates.toArray(new Predicate[predicates.size()]));
		};

		return findAll(specs, pageable);
	}
	
	default  <E> Path<?> getPropertyOrderPath(Root<E> root, String propertyPath) {
		String[] pathItems = propertyPath.split("\\.");
	    String pathItem = pathItems[0];
	    Path<?> path = root.get(pathItem);
	    for (int i = 1; i < pathItems.length; i++) {
	        path = path.get(pathItems[i]);
	    }
	    return path;
	}
	
}
