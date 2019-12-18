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
import com.fasten.wp4.database.model.Prediction;
import com.fasten.wp4.database.repository.DemandProjectionStudyRepository;
import com.fasten.wp4.database.swagger.ApiPageable;
import com.fasten.wp4.database.util.ConversorUtil;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class DemandProjectionStudyController {

	@Autowired
	private DemandProjectionStudyRepository repository;

	@GetMapping(value="/demandProjectionStudy", params = {"!page","!size", "!sort"})
	@ApiOperation(nickname="retrieveAllDemandProjectionStudy",value = "List all demandProjectionStudy")
	public List<DemandProjectionStudy> retrieveAll() {
		return repository.findAll();
	}

	@GetMapping(value = "/demandProjectionStudy/page")
	@ApiOperation(nickname="retrieveDemandProjectionStudyPaged", value = "List paged")
	@ApiPageable
	public Page<DemandProjectionStudy> retrivePageable(@ApiIgnore @NonNull Pageable pageable){
		return repository.findAll(pageable);
	}

	@GetMapping(value = "/demandProjectionStudy/filter")
	@ApiOperation(nickname="retrieveDemandProjectionStudyFilteredAndPaged", value = "List paged")
	@ApiPageable
	public Page<DemandProjectionStudy> retriveFilteredAndPaged(
				@ApiIgnore @NonNull 
				@PageableDefault(page = 0, size = 5) 
				@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
				Pageable pageable,
				String json) {
		Map<String,String> filters = ConversorUtil.toFilter(json);
		Page<DemandProjectionStudy> page = repository.filter(filters,pageable);
		return page;
	}
	
	@GetMapping("/demandProjectionStudy/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveDemandProjectionStudy", value = "Find one demandProjectionStudy")
	public DemandProjectionStudy retrieve(@PathVariable long id) {
		Optional<DemandProjectionStudy> retrivedObject = repository.findById(id);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}
	
	
	@GetMapping("/demandProjectionStudy/prediction/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveByStudy", value = "Find one demandProjectionStudy by prediction")
	public DemandProjectionStudy retrieveByStudy(@PathVariable long id) {
		Prediction p = new Prediction();
		p.setId(id);
		Optional<DemandProjectionStudy> retrivedObject = repository.findByStudy(p);
		List<DemandProjected> demandProjecteds = retrivedObject.get().getDemandProjecteds();
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}
	
	@GetMapping("/demandProjectionStudy/existsByPrediction/{id:[\\d]+}")
	@ApiOperation(nickname="existsByPrediction", value = "Find boolean demandProjectionStudy by prediction")
	public boolean existsByPrediction(@PathVariable long id) {
		Prediction p = new Prediction();
		p.setId(id);
		return repository.existsByStudy(p);
	}
	
	@DeleteMapping("/demandProjectionStudy/{id}")
	@ApiOperation(nickname="deleteDemandProjectionStudy", value = "Delete the demandProjectionStudy")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}

	@PostMapping("/demandProjectionStudy")
	@ApiOperation(nickname="createDemandProjectionStudy", value = "Create a new demandProjectionStudy ", notes = "Also returns the url to created data in header location")
	public ResponseEntity<Object> create(@RequestBody DemandProjectionStudy demandProjectionStudy) {
		
		Optional<DemandProjectionStudy> optional = repository.findByStudy(demandProjectionStudy.getStudy());
		if (optional.isPresent())
			demandProjectionStudy.setId(optional.get().getId());
		
		demandProjectionStudy.getDemandProjecteds().forEach(d->d.setDemandProjectionStudy(demandProjectionStudy));
		demandProjectionStudy.getErrors().forEach(d->d.setDemandProjectionStudy(demandProjectionStudy));
		
		DemandProjectionStudy savedObject = repository.save(demandProjectionStudy);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedObject.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/demandProjectionStudy/{id}")
	@ApiOperation(nickname="updateDemandProjectionStudy", value = "Edit demandProjectionStudy ")
	public ResponseEntity<Object> updateDemandProjectionStudy(@RequestBody DemandProjectionStudy object, @PathVariable long id) {
		Optional<DemandProjectionStudy> optional = repository.findById(id);
		if (!optional.isPresent())
			return ResponseEntity.notFound().build();
		object.setId(id);
		object.getDemandProjecteds().forEach(d->d.setDemandProjectionStudy(object));
		object.getErrors().forEach(d->d.setDemandProjectionStudy(object));
		repository.save(object);
		return ResponseEntity.noContent().build();
	}
	
}
