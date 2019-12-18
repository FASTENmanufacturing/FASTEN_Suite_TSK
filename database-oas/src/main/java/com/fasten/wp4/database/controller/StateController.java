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
import com.fasten.wp4.database.model.State;
import com.fasten.wp4.database.repository.StateRepository;
import com.fasten.wp4.database.swagger.ApiPageable;
import com.fasten.wp4.database.util.ConversorUtil;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class StateController {

	@Autowired
	private StateRepository repository;

	@GetMapping(value="/state", params = {"!page","!size", "!sort"})
	@ApiOperation(nickname="retrieveAllState", value = "List all")
	public List<State> retrieveAll() {
		return repository.findAllByOrderByNameAsc();
	}
	
	@GetMapping(value="/state/name/like/{name}")
	@ApiOperation(nickname="retrieveStateByNameLike", value = "Find states by name like")
	public List<State> retrieveStateByNameLike(@PathVariable String name) {
		List<State> retrivedList = repository.findByNameIgnoreCaseContainingOrderByNameAsc(name);
		return retrivedList;
	}
	
	@GetMapping(value = "/state/page")
	@ApiOperation(nickname="retrieveStatePaged", value = "List paged")
	@ApiPageable
	public Page<State> retrivePageable(@ApiIgnore @NonNull Pageable pageable){
		return repository.findAll(pageable);
	}

	@GetMapping(value = "/state/filter")
	@ApiOperation(nickname="retrieveStateFilteredAndPaged", value = "List paged")
	@ApiPageable
	public Page<State> retriveFilteredAndPaged(
				@ApiIgnore @NonNull 
				@PageableDefault(page = 0, size = 5) 
				@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
				Pageable pageable,
				String json) {
		Map<String,String> filters = ConversorUtil.toFilter(json);
		Page<State> page = repository.filter(filters,pageable);
		return page;
	}

	@GetMapping("/state/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveState", value = "Find a state")
	public State retrieve(@PathVariable long id) {
		Optional<State> retrivedObject = repository.findById(id);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}

	@DeleteMapping("/state/{id:[\\d]+}")
	@ApiOperation(nickname="deleteState", value = "Delete a state")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}

	@PostMapping("/state")
	@ApiOperation(nickname="createState", value = "Save a state", notes = "Also returns the url to created data in header location ")
	public ResponseEntity<Object> create(@RequestBody State state) {
		State savedObject = repository.save(state);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedObject.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/state/{id}")
	@ApiOperation(nickname="updateState", value = "Update a state")
	public ResponseEntity<Object> updateState(@RequestBody State object, @PathVariable long id) {
		Optional<State> optional = repository.findById(id);
		if (!optional.isPresent())
			return ResponseEntity.notFound().build();
		object.setId(id);
		repository.save(object);
		return ResponseEntity.noContent().build();
	}
}
