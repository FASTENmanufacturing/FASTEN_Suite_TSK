package com.fasten.wp4.database.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
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
import com.fasten.wp4.database.model.Optimizator;
import com.fasten.wp4.database.repository.OptimizatorRepository;
import com.fasten.wp4.database.swagger.ApiPageable;
import com.fasten.wp4.database.util.ConversorUtil;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class OptimizatorController {

	@Autowired
	private OptimizatorRepository repository;

	@GetMapping(value="/optimizator", params = {"!page","!size", "!sort"})
	@ApiOperation(nickname="retrieveAllOptimizator", value = "List all configured optimizations")
	public List<Optimizator> retrieveAll() {
		return repository.findAll();
	}
	
	@GetMapping(value = "/optimizator/page")
	@ApiOperation(nickname="retrieveOptimizatorPaged", value = "List paged")
	@ApiPageable
	public Page<Optimizator> retrivePageable(@ApiIgnore @NonNull Pageable pageable){
		return repository.findAll(pageable);
	}

	@GetMapping(value = "/optimizator/filter")
	@ApiOperation(nickname="retrieveOptimizatorFilteredAndPaged", value = "List paged")
	@ApiPageable
	public Page<Optimizator> retriveFilteredAndPaged(
				@ApiIgnore @NonNull 
				@PageableDefault(page = 0, size = 5) 
				@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
				Pageable pageable,
				String json) {
		Map<String,String> filters = ConversorUtil.toFilter(json);
		Page<Optimizator> page = repository.filter(filters,pageable);
		return page;
	}

	@GetMapping("/optimizator/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveOptimizator", value = "Find the optimizator configurations", notes = "Also returns a link to retrieve all optimizator with rel - all")
	public Resource<Optimizator> retrieve(@PathVariable long id) {
		Optional<Optimizator> retrivedObject = repository.findById(id);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		Resource<Optimizator> resource = new Resource<Optimizator>(retrivedObject.get());
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAll());
		resource.add(linkTo.withRel("all"));
		return resource;
	}

	@DeleteMapping("/optimizator/{id:[\\d]+}")
	@ApiOperation(nickname="deleteOptimizator", value = "Delete the optimizator's configurations")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}

	@PostMapping("/optimizator")
	@ApiOperation(nickname="createOptimizator", value = "Configura a optimization", notes = "Also returns the url to created data in header location ")
	public ResponseEntity<Object> create(@RequestBody Optimizator optimizator) {
		Optimizator savedObject = repository.save(optimizator);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedObject.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/optimizator/{id}")
	@ApiOperation(nickname="updateOptimizator", value = "Update a optimization")
	public ResponseEntity<Object> updateOptimizator(@RequestBody Optimizator object, @PathVariable long id) {
		Optional<Optimizator> optional = repository.findById(id);
		if (!optional.isPresent())
			return ResponseEntity.notFound().build();
		object.setId(id);
		repository.save(object);
		return ResponseEntity.noContent().build();
	}
}
