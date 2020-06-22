package com.fasten.wp4.database.controller;

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

import com.fasten.wp4.database.exception.NotFoundException;
import com.fasten.wp4.database.model.TacticalOptimization;
import com.fasten.wp4.database.model.TacticalOptimizationResult;
import com.fasten.wp4.database.repository.DistributionCenterRepository;
import com.fasten.wp4.database.repository.PartRepository;
import com.fasten.wp4.database.repository.TacticalOptimizationResultRepository;
import com.fasten.wp4.database.swagger.ApiPageable;
import com.fasten.wp4.database.util.ConversorUtil;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class TacticalOptimizationResultController {

	@Autowired
	private TacticalOptimizationResultRepository repository;
	
	@Autowired
	private DistributionCenterRepository distributionCenterRepository;

	@Autowired
	private PartRepository partRepository;

	@GetMapping(value="/tacticalOptimizationResult", params = {"!page","!size", "!sort"})
	@ApiOperation(nickname="retrieveAllTacticalOptimizationResult", value = "List all")
	public List<TacticalOptimizationResult> retrieveAll() {
		return repository.findAll();
	}
	
	@GetMapping(value = "/tacticalOptimizationResult/page")
	@ApiOperation(nickname="retrieveTacticalOptimizationResultPaged", value = "List paged")
	@ApiPageable
	public Page<TacticalOptimizationResult> retrivePageable(@ApiIgnore @NonNull Pageable pageable){
		return repository.findAll(pageable);
	}

	@GetMapping(value = "/tacticalOptimizationResult/filter")
	@ApiOperation(nickname="retrieveTacticalOptimizationResultFilteredAndPaged", value = "List paged")
	@ApiPageable
	public Page<TacticalOptimizationResult> retriveFilteredAndPaged(
				@ApiIgnore @NonNull 
				@PageableDefault(page = 0, size = 5) 
				@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
				Pageable pageable,
				String json) {
		Map<String,String> filters = ConversorUtil.toFilter(json);
		Page<TacticalOptimizationResult> page = repository.filter(filters,pageable);
		return page;
	}

	@GetMapping("/tacticalOptimizationResult/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveTacticalOptimizationResult", value = "Find a tacticalOptimizationResult")
	public TacticalOptimizationResult retrieve(@PathVariable long id) {
		Optional<TacticalOptimizationResult> retrivedObject = repository.findById(id);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}
	
	@GetMapping("/tacticalOptimizationResults/tacticalOptimization/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveByTacticalOptimization", value = "Find by study")
	public List<TacticalOptimizationResult> retrieveByStudy(@PathVariable long id) {
		TacticalOptimization o = new TacticalOptimization();
		o.setId(id);
		List<TacticalOptimizationResult> retrivedObject = repository.findByStudy(o);
		return retrivedObject;
	}

	@GetMapping("/tacticalOptimizationResult/tacticalOptimization/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveOneByTacticalOptimization", value = "Find by study")
	public TacticalOptimizationResult retrieveOneByStudy(@PathVariable long id) {
		TacticalOptimizationResult retrivedObject = repository.findByStudyId(id);
		return retrivedObject;
	}
	
	@GetMapping("/tacticalOptimizationResult/existsByTacticalOptimization/{id:[\\d]+}")
	@ApiOperation(nickname="existsByTacticalOptimization", value = "Find boolean by study")
	public boolean existsByTacticalOptimization(@PathVariable long id) {
		TacticalOptimization o = new TacticalOptimization();
		o.setId(id);
		return repository.existsByStudy(o);
	}

	@DeleteMapping("/tacticalOptimizationResult/{id:[\\d]+}")
	@ApiOperation(nickname="deleteTacticalOptimizationResult", value = "Delete a tacticalOptimizationResult")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}

	@PutMapping("/tacticalOptimizationResult/{id}")
	@ApiOperation(nickname="updateTacticalOptimizationResult", value = "Update a tacticalOptimizationResult")
	public ResponseEntity<Object> updateTacticalOptimizationResult(@RequestBody TacticalOptimizationResult object, @PathVariable long id) {
		Optional<TacticalOptimizationResult> optional = repository.findById(id);
		if (!optional.isPresent())
			return ResponseEntity.notFound().build();
		object.setId(id);
		repository.save(object);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/tacticalOptimizationResults")
	@ApiOperation(nickname="createTacticalOptimizationResults", value = "Save a results list")
	public ResponseEntity<Object> create(@RequestBody List<TacticalOptimizationResult> tacticalOptimizationResults) {
		if(!tacticalOptimizationResults.isEmpty()) {
			repository.deleteByStudy(tacticalOptimizationResults.get(0).getStudy());
			repository.saveAll(tacticalOptimizationResults);
		}
		return ResponseEntity.ok().build();
	}
	
	
	
}
