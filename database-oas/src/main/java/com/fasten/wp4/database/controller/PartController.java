package com.fasten.wp4.database.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.fasten.wp4.database.model.Part;
import com.fasten.wp4.database.model.PartPriority;
import com.fasten.wp4.database.repository.PartRepository;
import com.fasten.wp4.database.swagger.ApiPageable;
import com.fasten.wp4.database.util.ConversorUtil;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class PartController {

	@Autowired
	private PartRepository repository;

	@GetMapping(value="/part", params = {"!page","!size", "!sort"})
	@ApiOperation(nickname="retrieveAllPart", value = "List all parts")
	public List<Part> retrieveAll() {
		return repository.findAll();
	}
	
	@GetMapping(value = "/part/page")
	@ApiOperation(nickname="retrievePartPaged", value = "List paged")
	@ApiPageable
	public Page<Part> retrivePageable(@ApiIgnore @NonNull Pageable pageable){
		return repository.findAll(pageable);
	}
	
	@GetMapping(value = "/part/filter")
	@ApiOperation(nickname="retrievePartFilteredAndPaged", value = "List paged")
	@ApiPageable
	public Page<Part> retriveFilteredAndPaged(
				@ApiIgnore @NonNull 
				@PageableDefault(page = 0, size = 5) 
				@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
				Pageable pageable,
				String json) {
		Map<String,String> filters = ConversorUtil.toFilter(json);
		Page<Part> page = repository.filter(filters,pageable);
		return page;
	}

	@GetMapping("/part/{id:[\\d]+}")
	@ApiOperation(nickname="retrievePart", value = "Find one parts", notes = "Also returns a link to retrieve all part with rel - all")
	public Part retrieve(@PathVariable long id) {
		Optional<Part> retrivedObject = repository.findById(id);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}
	
	@GetMapping("/part/code/{code}")
	@ApiOperation(nickname="retrievePartByCode", value = "Find one part by code", notes = "Also returns a link to retrieve all part with rel - all")
	public Resource<Part> retrieveByCode(@PathVariable String code) {
		Optional<Part> retrivedObject = repository.findByCode(code);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		Resource<Part> resource = new Resource<Part>(retrivedObject.get());
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAll());
		resource.add(linkTo.withRel("all"));
		return resource;
	}
	
	@GetMapping(value="/part/name")
	@ApiOperation(nickname="retrieveAllDistinctByName", value = "Find parts name")
	public List<String> retrieveAllDistinctByName() {
		return repository.findAllNameOrderByName();
	}
	
	@GetMapping(value="/part/name/like/{name}")
	@ApiOperation(nickname="retrievePartByNameLike", value = "Find parts by name like")
	public List<String> retrievePartByNameLike(@PathVariable String name) {
		List<Part> retrivedList = repository.findByNameIgnoreCaseContaining(name);
		return retrivedList.stream().map(p->p.getName()).collect(Collectors.toList());
	}
	
	@GetMapping("/part/code/{code}/priority/{priority}")
	@ApiOperation(nickname="retrievePartByCodeAndPriority", value = "Find one part by code and priority", notes = "Also returns a link to retrieve all part with rel - all")
	public Resource<Part> retrieveByCodeAndPriority(@PathVariable String code, @PathVariable PartPriority priority) {
		Optional<Part> retrivedObject = repository.findByCodeAndPriority(code,priority);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		Resource<Part> resource = new Resource<Part>(retrivedObject.get());
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAll());
		resource.add(linkTo.withRel("all"));
		return resource;
	}
	
	
	
	@GetMapping("/part/priority/{priority}")
	@ApiOperation(nickname="retrievePartByPriority", value = "Find parts by priority")
	public List<Part> retrieveByPriority(@PathVariable PartPriority priority) {
		return repository.findByPriority(priority);
	}

	@DeleteMapping("/part/{id}")
	@ApiOperation(nickname="deletePart", value = "Delete the part")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}

	@PostMapping("/part")
	@ApiOperation(nickname="createPart", value = "Create a new part", notes = "Also returns the url to created data in header location")
	public ResponseEntity<Object> create(@RequestBody Part part) {
		Part savedObject = repository.save(part);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedObject.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/part/{id}")
	@ApiOperation(nickname="updatePart", value = "Edit the part")
	public ResponseEntity<Object> updatePart(@RequestBody Part object, @PathVariable long id) {
		Optional<Part> optional = repository.findById(id);
		if (!optional.isPresent())
			return ResponseEntity.notFound().build();
		object.setId(id);
		repository.save(object);
		return ResponseEntity.noContent().build();
	}
}
