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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasten.wp4.database.exception.NotFoundException;
import com.fasten.wp4.database.model.DistributionCenter;
import com.fasten.wp4.database.model.DistributionCenterPriority;
import com.fasten.wp4.database.repository.DistributionCenterRepository;
import com.fasten.wp4.database.swagger.ApiPageable;
import com.fasten.wp4.database.util.ConversorUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class DistributionCenterController {

	@Autowired
	private DistributionCenterRepository repository;

	@GetMapping("/distributioncenter")
	@ApiOperation(nickname="retrieveAllDistributionCenter", value = "List all Distribution Centers")
	public List<DistributionCenter> retrieveAll() {
		return repository.findAll();
	}
	
	@GetMapping(value = "/distributioncenter/page")
	@ApiOperation(nickname="retrieveDistributionCenterPaged", value = "List paged")
	@ApiPageable
	public Page<DistributionCenter> retrivePageable(@ApiIgnore @NonNull Pageable pageable){
		return repository.findAll(pageable);
	}

	@GetMapping(value = "/distributioncenter/filter")
	@ApiOperation(nickname="retrieveDistributionCenterFilteredAndPaged", value = "List paged")
	@ApiPageable
	public Page<DistributionCenter> retriveFilteredAndPaged(
				@ApiIgnore @NonNull 
				@PageableDefault(page = 0, size = 5) 
				@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
				Pageable pageable,
				String json) {
		Map<String,String> filters = ConversorUtil.toFilter(json);
		Page<DistributionCenter> page = repository.filter(filters,pageable);
		return page;
	}

	@GetMapping("/distributioncenter/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveDistributionCenter", value = "Retrive one Distribution Center", notes = "Also returns a link to retrieve all distribution center with rel - all")
	public DistributionCenter retrieve(@PathVariable long id) {
		
		Optional<DistributionCenter> retrivedObject = repository.findById(id);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}
	
	@GetMapping(value = "/distributioncenter/tacticalOptimization", params = { "id"})
	@ApiOperation(nickname="retrieveDistributionCenterByTacticalOptimization", value = "Retrive a list of Distribution Center candidates in a Tactical Optimization Study")
	public List<DistributionCenter> retrieveByTacticalOptimization(@ApiParam(example="1") @RequestParam(value = "id") Long id) {
		return repository.retrieveDistributionCenterByTacticalOptimization(id);
	}
	
	@GetMapping("/distributioncenter/code/{code}")
	@ApiOperation(nickname="retrieveDistributionCenterByCode", value = "Retrive one Distribution Center by code", notes = "Also returns a link to retrieve all distribution center with rel - all")
	public DistributionCenter retrieveByCode(@PathVariable String code) {
		Optional<DistributionCenter> retrivedObject = repository.findByCode(code);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}
	
	@GetMapping("/distributioncenter/priority/{priority}")
	@ApiOperation(nickname="retrieveDistributionCenterByPriority", value = "Find distribution centers by priority")
	public List<DistributionCenter> retrieveByPriority(@PathVariable DistributionCenterPriority priority) {
		return repository.findByPriority(priority);
	}
	
	@GetMapping("/distributioncenter/name")
	@ApiOperation(nickname="retrieveDistributionCenterByName", value = "Retrive one Distribution Center by name", notes = "Also returns a link to retrieve all distribution center with rel - all")
	public DistributionCenter retrieveByName(@RequestParam(value = "name") String name) {
		Optional<DistributionCenter> retrivedObject = repository.findByName(name);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}
	
	@GetMapping("/distributioncenter/city")
	@ApiOperation(nickname="retrieveDistributionCenterByCityName", value = "Retrive Distribution Centers by city name")
	public List<DistributionCenter> retrieveByCityName(@RequestParam(value = "city") String cityName) {
		List<DistributionCenter> retrivedObject = repository.findByAddressCityName(cityName);
		return retrivedObject;
	}
	
	@GetMapping("/distributioncenter/excellName")
	@ApiOperation(nickname="retrieveDistributionCenterByExcellName", value = "Retrive Distribution Centers by name without space and uppercased")
	public DistributionCenter retrieveDistributionCenterByExcellName(@RequestParam(value = "excellName") String excellName) {
		Optional<DistributionCenter> retrivedObject = repository.findByExcellName(excellName);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}
	
	@GetMapping(value="/distributioncenter/names")
	@ApiOperation(nickname="retrieveAllDistributionCenterByName", value = "Find distribution centers by name")
	public List<String> retrieveAllByName() {
		return repository.findAllByNameOrderByName();
	}

	@DeleteMapping("/distributioncenter/{id}")
	@ApiOperation(nickname="deleteDistributionCenter", value = "Delete the Distribution Center")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}

	@PostMapping("/distributioncenter")
	@ApiOperation(nickname="createDistributionCenter", value = "Register Distribution Center", notes = "Also returns the url to created data in header location")
	public ResponseEntity<Object> create(@RequestBody DistributionCenter distributionCenter) {
		DistributionCenter savedObject = repository.save(distributionCenter);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedObject.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/distributioncenter/{id}")
	@ApiOperation(nickname="updateDistributionCenter", value = "Update a Distribution Center")
	public ResponseEntity<Object> updateDistributionCenter(@RequestBody DistributionCenter object, @PathVariable long id) {
		Optional<DistributionCenter> optional = repository.findById(id);
		if (!optional.isPresent())
			return ResponseEntity.notFound().build();
		object.setId(id);
		repository.save(object);
		return ResponseEntity.noContent().build();
	}
}
