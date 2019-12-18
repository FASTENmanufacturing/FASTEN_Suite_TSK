package com.github.adminfaces.starter.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class LazyDataModelUtils {
	
	private static ObjectMapper objectMapper;
	static {
		objectMapper=new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
				 .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}
	
	@Inject
	public static void setObjectMapper(ObjectMapper objectMapper) {
		LazyDataModelUtils.objectMapper = objectMapper;
	}

	public static List<String> toApiSort(String sortField, SortOrder sortOrder) {
		List<String> sort = new ArrayList<String>();
		if(sortField!=null)
		sort.add(toApiSortItem(sortField, toApiOrder(sortOrder)));
		return sort;
	};

	private static String toApiOrder(SortOrder sortOrder) {
		return sortOrder.equals(SortOrder.ASCENDING) ? "ASC" : sortOrder.equals(SortOrder.DESCENDING) ? "DESC" : "UNSORTED";
	}

	private static String toApiSortItem(String field, String order) {
		return field+","+order;
	}

	public static List<String> toApiSort(List<SortMeta> multiSortMeta) {
		List<String> sort = new ArrayList<String>();
		if(multiSortMeta!=null) {
			for (SortMeta sortMeta : multiSortMeta) {
				if(sortMeta.getSortField()!=null)
				sort.add(toApiSortItem(sortMeta.getSortField(), toApiOrder(sortMeta.getSortOrder())));
			}
		}
		return sort;
	}

	public static String toApiFilters(Map<String, Object> filters){
		try {
			return objectMapper.writeValueAsString(filters);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return "{}";
	}

	public static Integer toApiPage(int first, int pageSize) {
		return first/pageSize;
	}

	public static Integer toApiSize(int pageSize) {
		return pageSize;
	}

}

//public static Map<String,String> toApiFilters(Map<String, Object> filters){
//return filters.entrySet().stream().collect(Collectors.toMap(
//		entry -> entry.getKey(), 
//		entry -> entry.getValue().toString())
//		);
//}