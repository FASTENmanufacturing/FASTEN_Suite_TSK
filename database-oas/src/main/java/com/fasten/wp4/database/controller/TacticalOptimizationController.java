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
import com.fasten.wp4.database.model.TacticalOptimization;
import com.fasten.wp4.database.model.TacticalOptimizationStatus;
import com.fasten.wp4.database.repository.TacticalOptimizationRepository;
import com.fasten.wp4.database.repository.TacticalOptimizationResultRepository;
import com.fasten.wp4.database.swagger.ApiPageable;
import com.fasten.wp4.database.util.ConversorUtil;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class TacticalOptimizationController {

	@Autowired
	private TacticalOptimizationRepository repository;

	@Autowired
	private TacticalOptimizationResultRepository resultRepository;

	@GetMapping(value="/tacticalOptimization", params = {"!page","!size", "!sort"})
	@ApiOperation(nickname="retrieveAllTacticalOptimization", value = "List all configured optimizations")
	public List<TacticalOptimization> retrieveAll() {
		return repository.findAll();
	}
	
	@GetMapping(value = "/tacticalOptimization/page")
	@ApiOperation(nickname="retrieveTacticalOptimizationPaged", value = "List paged")
	@ApiPageable
	public Page<TacticalOptimization> retrivePageable(@ApiIgnore @NonNull Pageable pageable){
		return repository.findAll(pageable);
	}

	@GetMapping(value = "/tacticalOptimization/filter")
	@ApiOperation(nickname="retrieveTacticalOptimizationFilteredAndPaged", value = "List paged")
	@ApiPageable
	public Page<TacticalOptimization> retriveFilteredAndPaged(
				@ApiIgnore @NonNull 
				@PageableDefault(page = 0, size = 5) 
				@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
				Pageable pageable,
				String json) {
		Map<String,String> filters = ConversorUtil.toFilter(json);
		Page<TacticalOptimization> page = repository.filter(filters,pageable);
		return page;
	}

	@GetMapping("/tacticalOptimization/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveTacticalOptimization", value = "Find the tacticalOptimization configurations", notes = "Also returns a link to retrieve all tacticalOptimization with rel - all")
	public TacticalOptimization retrieve(@PathVariable long id) {
		Optional<TacticalOptimization> retrivedObject = repository.findById(id);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}

	@DeleteMapping("/tacticalOptimization/{id:[\\d]+}")
	@ApiOperation(nickname="deleteTacticalOptimization", value = "Delete the tacticalOptimization's configurations")
	public void delete(@PathVariable long id) {
		
		TacticalOptimization o = new TacticalOptimization();
		o.setId(id);
		if(resultRepository.existsByStudy(o)) {
			resultRepository.deleteByStudy(o);
		}
		repository.deleteById(id);
	}

	@PostMapping("/tacticalOptimization")
	@ApiOperation(nickname="createTacticalOptimization", value = "Configura a optimization", notes = "Also returns the url to created data in header location ")
	public ResponseEntity<Object> create(@RequestBody TacticalOptimization tacticalOptimization) {
		TacticalOptimization savedObject = repository.save(tacticalOptimization);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedObject.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/tacticalOptimization/{id}")
	@ApiOperation(nickname="updateTacticalOptimization", value = "Update a optimization")
	public ResponseEntity<Object> updateTacticalOptimization(@RequestBody TacticalOptimization object, @PathVariable long id) {
		Optional<TacticalOptimization> optional = repository.findById(id);
		if (!optional.isPresent())
			return ResponseEntity.notFound().build();
		object.setId(id);
		repository.save(object);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/tacticalOptimization/status")
	@ApiOperation(nickname="validateTacticalOptimization", value = "Validate a tactical optimization")
	public ResponseEntity<Object> updateTacticalOptimization(@RequestParam(value = "id") Long id) {
		Optional<TacticalOptimization> optional = repository.findById(id);
		if (!optional.isPresent())
			return ResponseEntity.notFound().build();
		TacticalOptimization object = optional.get();
		object.setStatus(TacticalOptimizationStatus.Valid);
		repository.save(object);
		return ResponseEntity.noContent().build();
	}
}
