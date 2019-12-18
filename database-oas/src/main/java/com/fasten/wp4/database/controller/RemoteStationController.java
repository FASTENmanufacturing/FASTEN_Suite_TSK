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
import com.fasten.wp4.database.model.RemoteStation;
import com.fasten.wp4.database.model.RemoteStationPriority;
import com.fasten.wp4.database.repository.RemoteStationRepository;
import com.fasten.wp4.database.swagger.ApiPageable;
import com.fasten.wp4.database.util.ConversorUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class RemoteStationController {

	@Autowired
	private RemoteStationRepository repository;

	@GetMapping("/remotestation")
	@ApiOperation(nickname="retrieveAllRemoteStation", value = "List all Remote Stations")
	public List<RemoteStation> retrieveAll() {
		return repository.findAll();
	}
	
	@GetMapping(value = "/remotestation/page")
	@ApiOperation(nickname="retrieveRemoteStationPaged", value = "List paged")
	@ApiPageable
	public Page<RemoteStation> retrivePageable(@ApiIgnore @NonNull Pageable pageable){
		return repository.findAll(pageable);
	}

	@GetMapping(value = "/remotestation/filter")
	@ApiOperation(nickname="retrieveRemoteStationFilteredAndPaged", value = "List paged")
	@ApiPageable
	public Page<RemoteStation> retriveFilteredAndPaged(
				@ApiIgnore @NonNull 
				@PageableDefault(page = 0, size = 5) 
				@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
				Pageable pageable,
				String json) {
		Map<String,String> filters = ConversorUtil.toFilter(json);
		Page<RemoteStation> page = repository.filter(filters,pageable);
		return page;
	}

	@GetMapping("/remotestation/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveRemoteStation", value = "Retrive one Remote Station", notes = "Also returns a link to retrieve all remote station with rel - all")
	public RemoteStation retrieve(@PathVariable long id) {
		
		//TODO trecho de codigo para simular async
//		try {Thread.sleep(30000);} catch (InterruptedException e) {}
		
		Optional<RemoteStation> retrivedObject = repository.findById(id);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}
	
	@GetMapping(value = "/remotestation/tacticalOptimization", params = { "id"})
	@ApiOperation(nickname="retrieveRemoteStationByTacticalOptimization", value = "Retrive a list of Remote Station candidates in a Tactical Optimization Study")
	public List<RemoteStation> retrieveByTacticalOptimization(@ApiParam(example="1") @RequestParam(value = "id") Long id) {
		return repository.retrieveRemoteStationByTacticalOptimization(id);
	}
	
	@GetMapping("/remotestation/code/{code}")
	@ApiOperation(nickname="retrieveRemoteStationByCode", value = "Retrive one Remote Station by code", notes = "Also returns a link to retrieve all remote station with rel - all")
	public RemoteStation retrieveByCode(@PathVariable String code) {
		Optional<RemoteStation> retrivedObject = repository.findByCode(code);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}
	
	@GetMapping("/remotestation/priority/{priority}")
	@ApiOperation(nickname="retrieveRemoteStationByPriority", value = "Find remote stations by priority")
	public List<RemoteStation> retrieveByPriority(@PathVariable RemoteStationPriority priority) {
		return repository.findByPriority(priority);
	}
	
	@GetMapping("/remotestation/name")
	@ApiOperation(nickname="retrieveRemoteStationByName", value = "Retrive one Remote Station by name", notes = "Also returns a link to retrieve all remote station with rel - all")
	public RemoteStation retrieveByName(@RequestParam(value = "name") String name) {
		Optional<RemoteStation> retrivedObject = repository.findByName(name);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}
	
	@GetMapping("/remotestation/city")
	@ApiOperation(nickname="retrieveRemoteStationByCityName", value = "Retrive Remote Stations by city name")
	public List<RemoteStation> retrieveByCityName(@RequestParam(value = "city") String cityName) {
		List<RemoteStation> retrivedObject = repository.findByCityName(cityName);
		return retrivedObject;
	}
	
	@GetMapping(value="/remotestation/names")
	@ApiOperation(nickname="retrieveAllByName", value = "Find remote stations by name")
	public List<String> retrieveAllByName() {
		return repository.findAllByNameOrderByName();
	}

	@DeleteMapping("/remotestation/{id}")
	@ApiOperation(nickname="deleteRemoteStation", value = "Delete the Remote Station")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}

	@PostMapping("/remotestation")
	@ApiOperation(nickname="createRemoteStation", value = "Register Remote Station", notes = "Also returns the url to created data in header location")
	public ResponseEntity<Object> create(@RequestBody RemoteStation remoteStation) {
		RemoteStation savedObject = repository.save(remoteStation);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedObject.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/remotestation/{id}")
	@ApiOperation(nickname="updateRemoteStation", value = "Update a Remote Station")
	public ResponseEntity<Object> updateRemoteStation(@RequestBody RemoteStation object, @PathVariable long id) {
		Optional<RemoteStation> optional = repository.findById(id);
		if (!optional.isPresent())
			return ResponseEntity.notFound().build();
		object.setId(id);
		repository.save(object);
		return ResponseEntity.noContent().build();
	}
}
