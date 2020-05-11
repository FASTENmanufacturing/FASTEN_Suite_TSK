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
import com.fasten.wp4.database.model.OperationalOptimizationResult;
import com.fasten.wp4.database.repository.OperationalOptimizationResultRepository;
import com.fasten.wp4.database.swagger.ApiPageable;
import com.fasten.wp4.database.util.ConversorUtil;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class OperationalOptimizationResultController {

	@Autowired
	private OperationalOptimizationResultRepository repository;

	@GetMapping(value="/operationalOptimizationResult", params = {"!page","!size", "!sort"})
	@ApiOperation(nickname="retrieveAllOperationalOptimizationResult", value = "List all")
	public List<OperationalOptimizationResult> retrieveAll() {
		return repository.findAll();
	}
	
	@GetMapping(value = "/operationalOptimizationResult/page")
	@ApiOperation(nickname="retrieveOperationalOptimizationResultPaged", value = "List paged")
	@ApiPageable
	public Page<OperationalOptimizationResult> retrivePageable(@ApiIgnore @NonNull Pageable pageable){
		return repository.findAll(pageable);
	}

	@GetMapping(value = "/operationalOptimizationResult/filter")
	@ApiOperation(nickname="retrieveOperationalOptimizationResultFilteredAndPaged", value = "List paged")
	@ApiPageable
	public Page<OperationalOptimizationResult> retriveFilteredAndPaged(
				@ApiIgnore @NonNull 
				@PageableDefault(page = 0, size = 5) 
				@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
				Pageable pageable,
				String json) {
		Map<String,String> filters = ConversorUtil.toFilter(json);
		Page<OperationalOptimizationResult> page = repository.filter(filters,pageable);
		return page;
	}

	@GetMapping("/operationalOptimizationResult/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveOperationalOptimizationResult", value = "Find a operationalOptimizationResult")
	public OperationalOptimizationResult retrieve(@PathVariable long id) {
		Optional<OperationalOptimizationResult> retrivedObject = repository.findById(id);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}
	
	@GetMapping("/operationalOptimizationResult/operationalOptimization/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveByOperationalOptimization", value = "Find by study")
	public List<OperationalOptimizationResult> retrieveByStudy(@PathVariable long id) {
		OperationalOptimization o = new OperationalOptimization();
		o.setId(id);
		List<OperationalOptimizationResult> retrivedObject = repository.findByStudy(o);
		return retrivedObject;
	}

	@GetMapping("/operationalOptimizationResult/orderID/{orderID}")
	@ApiOperation(nickname="retrieveByOrderID", value = "Find by orderID")
	public List<OperationalOptimizationResult> retrieveByOrderID(@PathVariable String orderID) {
		List<OperationalOptimizationResult> retrivedObject = repository.retrieveByOrderID(orderID);
		return retrivedObject;
	}
	
	@GetMapping("/operationalOptimizationResult/existsByOperationalOptimization/{id:[\\d]+}")
	@ApiOperation(nickname="existsByOperationalOptimization", value = "Find boolean by study")
	public boolean existsByOperationalOptimization(@PathVariable long id) {
		OperationalOptimization o = new OperationalOptimization();
		o.setId(id);
		return repository.existsByStudy(o);
	}

	@DeleteMapping("/operationalOptimizationResult/{id:[\\d]+}")
	@ApiOperation(nickname="deleteOperationalOptimizationResult", value = "Delete a operationalOptimizationResult")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}

	@PostMapping("/operationalOptimizationResult")
	@ApiOperation(nickname="createOperationalOptimizationResult", value = "Save a operationalOptimizationResult", notes = "Also returns the url to created data in header location ")
	public ResponseEntity<Object> create(@RequestBody OperationalOptimizationResult operationalOptimizationResult) {
		OperationalOptimizationResult savedObject = repository.save(operationalOptimizationResult);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedObject.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/operationalOptimizationResult/{id}")
	@ApiOperation(nickname="updateOperationalOptimizationResult", value = "Update a operationalOptimizationResult")
	public ResponseEntity<Object> updateOperationalOptimizationResult(@RequestBody OperationalOptimizationResult object, @PathVariable long id) {
		Optional<OperationalOptimizationResult> optional = repository.findById(id);
		if (!optional.isPresent())
			return ResponseEntity.notFound().build();
		object.setId(id);
		repository.save(object);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/operationalOptimizationResults")
	@ApiOperation(nickname="createOperationalOptimizationResults", value = "Save a results list")
	public ResponseEntity<Object> create(@RequestBody List<OperationalOptimizationResult> operationalOptimizationResults) {
		if(!operationalOptimizationResults.isEmpty()) {
			repository.deleteByStudy(operationalOptimizationResults.get(0).getStudy());
			repository.saveAll(operationalOptimizationResults);
		}
		return ResponseEntity.ok().build();
	}
	
	
	
}
