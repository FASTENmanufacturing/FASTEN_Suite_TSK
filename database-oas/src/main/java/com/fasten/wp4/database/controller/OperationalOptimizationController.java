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
import com.fasten.wp4.database.model.OperationalOptimization;
import com.fasten.wp4.database.repository.OperationalOptimizationRepository;
import com.fasten.wp4.database.swagger.ApiPageable;
import com.fasten.wp4.database.util.ConversorUtil;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class OperationalOptimizationController {

	@Autowired
	private OperationalOptimizationRepository repository;

	@GetMapping(value="/operationalOptimization", params = {"!page","!size", "!sort"})
	@ApiOperation(nickname="retrieveAllOperationalOptimization", value = "List all configured optimizations")
	public List<OperationalOptimization> retrieveAll() {
		return repository.findAll();
	}
	
	@GetMapping(value = "/operationalOptimization/page")
	@ApiOperation(nickname="retrieveOperationalOptimizationPaged", value = "List paged")
	@ApiPageable
	public Page<OperationalOptimization> retrivePageable(@ApiIgnore @NonNull Pageable pageable){
		return repository.findAll(pageable);
	}

	@GetMapping(value = "/operationalOptimization/filter")
	@ApiOperation(nickname="retrieveOperationalOptimizationFilteredAndPaged", value = "List paged")
	@ApiPageable
	public Page<OperationalOptimization> retriveFilteredAndPaged(
				@ApiIgnore @NonNull 
				@PageableDefault(page = 0, size = 5) 
				@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
				Pageable pageable,
				String json) {
		Map<String,String> filters = ConversorUtil.toFilter(json);
		Page<OperationalOptimization> page = repository.filter(filters,pageable);
		return page;
	}

	@GetMapping("/operationalOptimization/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveOperationalOptimization", value = "Find the Operational Optimization configurations", notes = "Also returns a link to retrieve all operational Optimization with rel - all")
	public OperationalOptimization retrieve(@PathVariable long id) {
		Optional<OperationalOptimization> retrivedObject = repository.findById(id);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}

	@DeleteMapping("/operationalOptimization/{id:[\\d]+}")
	@ApiOperation(nickname="deleteOperationalOptimization", value = "Delete the operational Optimization's configurations")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}

	@PostMapping("/operationalOptimization")
	@ApiOperation(nickname="createOperationalOptimization", value = "Configura a optimization", notes = "Also returns the url to created data in header location ")
	public ResponseEntity<Object> create(@RequestBody OperationalOptimization operationalOptimization) {
		OperationalOptimization savedObject = repository.save(operationalOptimization);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedObject.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/operationalOptimization/{id}")
	@ApiOperation(nickname="updateOperationalOptimization", value = "Update a optimization")
	public ResponseEntity<Object> updateOperationalOptimization(@RequestBody OperationalOptimization object, @PathVariable long id) {
		Optional<OperationalOptimization> optional = repository.findById(id);
		if (!optional.isPresent())
			return ResponseEntity.notFound().build();
		object.setId(id);
		repository.save(object);
		return ResponseEntity.noContent().build();
	}
}
