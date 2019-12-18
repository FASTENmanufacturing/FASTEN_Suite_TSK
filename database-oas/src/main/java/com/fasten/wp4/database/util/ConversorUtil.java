package com.fasten.wp4.database.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.fasten.wp4.database.config.AppContext;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConversorUtil {

	public static Sort toSort(String sortString) {
		
		List<String> properties = new ArrayList<String>();
		List<Direction> directions = new ArrayList<Direction>();
		for (String sortSplit : sortString.split(",")) {
			if(sortSplit.toLowerCase().equals("asc")||sortSplit.toLowerCase().equals("desc")) {
				directions.add(Direction.fromString(sortSplit));
			}else {
				properties.add(sortSplit);
			}
		}

		List<Order> orders = new ArrayList<Order>();
		for (int i = 0; i < properties.size(); i++) {
			String property = properties.get(i);
			Direction direction = Sort.DEFAULT_DIRECTION;
			try {direction = directions.get(i);}catch(Exception e) {}
			orders.add(new Order(direction, property));
		}
		Sort sort = Sort.by(orders);
		return sort;
	}
	
	
	private static ObjectMapper objectMapper= AppContext.getApplicationContext().getBean(ObjectMapper.class);
	

	@SuppressWarnings("unchecked")
	public static Map<String,String> toFilter(String json){
		Map<String, String> filters = new HashMap<String,String>();
		if(json!=null)
		try { filters = objectMapper.readValue(json, Map.class); } catch (IOException e) { e.printStackTrace(); }
		return filters;
	}
	

}
