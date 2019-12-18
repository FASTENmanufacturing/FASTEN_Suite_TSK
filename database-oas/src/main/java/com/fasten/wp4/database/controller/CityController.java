package com.fasten.wp4.database.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasten.wp4.database.exception.NotFoundException;
import com.fasten.wp4.database.model.City;
import com.fasten.wp4.database.model.State;
import com.fasten.wp4.database.repository.CityRepository;
import com.fasten.wp4.database.swagger.ApiPageable;
import com.fasten.wp4.database.util.ConversorUtil;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class CityController {

	@Autowired
	private CityRepository repository;

	@GetMapping(value="/city", params = {"!page","!size", "!sort"})
	@ApiOperation(nickname="retrieveAllCity", value = "List all")
	public List<City> retrieveAll() {
		return repository.findAll();
	}
	
	@GetMapping(value="/city/name/like/{name}")
	@ApiOperation(nickname="retrieveCityByNameLike", value = "Find cities by name like")
	public List<City> retrieveCityByNameLike(@PathVariable String name) {
		List<City> retrivedList = repository.findByNameIgnoreCaseContainingOrderByNameAsc(name);
		return retrivedList;
	}
	
	@GetMapping(value="/city/name/{name}")
	@ApiOperation(nickname="retrieveCityByName", value = "Find cities by name")
	public City retrieveCityByName(@PathVariable String name) {
		Optional<City> retrivedObject = repository.findByNameIgnoreCase(name);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}
	
	@GetMapping(value="/city/state/{id}")
	@ApiOperation(nickname="retrivedByState", value = "Find cities by state")
	public List<City> retrivedByState(@PathVariable Long id) {
		State state = new State();
		state.setId(id);
		List<City> retrivedList = repository.findByState(state);
		return retrivedList;
	}
	
	@GetMapping(value="/city/state/stateAcronym/{stateAcronym}")
	@ApiOperation(nickname="retrivedByStateAcronym", value = "Find cities by state acronym")
	public List<City> retrivedByStateAcronym(@PathVariable String stateAcronym) {
		List<City> retrivedList = repository.findByStateStateAcronym(stateAcronym);
		return retrivedList;
	}
	
	@GetMapping(value="/city/capital/{capital}")
	@ApiOperation(nickname="retrivedByCapital", value = "Find cities by capital")
	public List<City> retrivedByCapital(@PathVariable Boolean capital) {
		List<City> retrivedList = repository.findByCapital(capital);
		return retrivedList;
	}
	
	@GetMapping(value = "/city/page")
	@ApiOperation(nickname="retrieveCityPaged", value = "List paged")
	@ApiPageable
	public Page<City> retrivePageable(@ApiIgnore @NonNull Pageable pageable){
		return repository.findAll(pageable);
	}

	@GetMapping(value = "/city/filter")
	@ApiOperation(nickname="retrieveCityFilteredAndPaged", value = "List paged")
	@ApiPageable
	public Page<City> retriveFilteredAndPaged(
				@ApiIgnore @NonNull 
				@PageableDefault(page = 0, size = 5) 
				@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
				Pageable pageable,
				String json) {
		Map<String,String> filters = ConversorUtil.toFilter(json);
		Page<City> page = repository.filter(filters,pageable);
		return page;
	}

	@GetMapping("/city/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveCity", value = "Find a city")
	public City retrieve(@PathVariable long id) {
		Optional<City> retrivedObject = repository.findById(id);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}

	@DeleteMapping("/city/{id:[\\d]+}")
	@ApiOperation(nickname="deleteCity", value = "Delete a city")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}

	@PostMapping("/city")
	@ApiOperation(nickname="createCity", value = "Save a city", notes = "Also returns the url to created data in header location ")
	public ResponseEntity<Object> create(@RequestBody City city) {
		City savedObject = repository.save(city);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedObject.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/city/{id}")
	@ApiOperation(nickname="updateCity", value = "Update a city")
	public ResponseEntity<Object> updateCity(@RequestBody City object, @PathVariable long id) {
		Optional<City> optional = repository.findById(id);
		if (!optional.isPresent())
			return ResponseEntity.notFound().build();
		object.setId(id);
		repository.save(object);
		return ResponseEntity.noContent().build();
	}
}
