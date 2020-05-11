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
import com.fasten.wp4.database.model.DemandProjected;
import com.fasten.wp4.database.model.DemandProjectionStudy;
import com.fasten.wp4.database.repository.DemandProjectedRepository;
import com.fasten.wp4.database.swagger.ApiPageable;
import com.fasten.wp4.database.util.ConversorUtil;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class DemandProjectedController {

	@Autowired
	private DemandProjectedRepository repository;

	@GetMapping(value="/demandProjected", params = {"!page","!size", "!sort"})
	@ApiOperation(nickname="retrieveAllDemandProjected",value = "List all demandProjected")
	public List<DemandProjected> retrieveAll() {
		return repository.findAll();
	}

	@GetMapping(value = "/demandProjected/page")
	@ApiOperation(nickname="retrieveDemandProjectedPaged", value = "List paged")
	@ApiPageable
	public Page<DemandProjected> retrivePageable(@ApiIgnore @NonNull Pageable pageable){
		return repository.findAll(pageable);
	}

	@GetMapping(value = "/demandProjected/filter")
	@ApiOperation(nickname="retrieveDemandProjectedFilteredAndPaged", value = "List paged")
	@ApiPageable
	public Page<DemandProjected> retriveFilteredAndPaged(
				@ApiIgnore @NonNull 
				@PageableDefault(page = 0, size = 5) 
				@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
				Pageable pageable,
				String json) {
		Map<String,String> filters = ConversorUtil.toFilter(json);
		Page<DemandProjected> page = repository.filter(filters,pageable);
		return page;
	}
	
	@GetMapping("/demandProjected/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveDemandProjected", value = "Find one demandProjected")
	public DemandProjected retrieve(@PathVariable long id) {
		Optional<DemandProjected> retrivedObject = repository.findById(id);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}
	
	@DeleteMapping("/demandProjected/{id}")
	@ApiOperation(nickname="deleteDemandProjected", value = "Delete the demandProjected")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}
	
	@DeleteMapping("/demandProjected/demandProjectionStudy/{id}")
	@ApiOperation(nickname="deleteByDemandProjectionStudy", value = "Delete the demandProjected by demandProjectionStudy")
	public void deleteByDemandProjectionStudy(@PathVariable long id) {
		DemandProjectionStudy demandProjectionStudy = new DemandProjectionStudy();
		demandProjectionStudy.setId(id);
		repository.deleteByDemandProjectionStudy(demandProjectionStudy);
	}

	@PostMapping("/demandProjected")
	@ApiOperation(nickname="createDemandProjected", value = "Create a new demandProjected ", notes = "Also returns the url to created data in header location")
	public ResponseEntity<Object> create(@RequestBody DemandProjected demandProjected) {
		DemandProjected savedObject = repository.save(demandProjected);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedObject.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/demandProjected/{id}")
	@ApiOperation(nickname="updateDemandProjected", value = "Edit demandProjected ")
	public ResponseEntity<Object> updateDemandProjected(@RequestBody DemandProjected object, @PathVariable long id) {
		Optional<DemandProjected> optional = repository.findById(id);
		if (!optional.isPresent())
			return ResponseEntity.notFound().build();
		object.setId(id);
		repository.save(object);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/demandProjecteds")
	@ApiOperation(nickname="updateDemandProjecteds", value = "Batch update a list of demandProjecteds")
	public ResponseEntity<Object> updateDemandProjecteds(@RequestBody List<DemandProjected> list) {
		repository.saveAll(list);
		return ResponseEntity.noContent().build();
	}
}
