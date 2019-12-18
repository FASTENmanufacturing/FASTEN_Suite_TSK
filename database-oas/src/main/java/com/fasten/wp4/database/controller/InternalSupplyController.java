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
import com.fasten.wp4.database.model.InternalSupply;
import com.fasten.wp4.database.repository.InternalSupplyRepository;
import com.fasten.wp4.database.swagger.ApiPageable;
import com.fasten.wp4.database.util.ConversorUtil;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class InternalSupplyController {

	@Autowired
	private InternalSupplyRepository repository;

	@GetMapping(value = "/internalSupply",params = {"!page","!size", "!sort"})
	@ApiOperation(nickname="retrieveAllInternalSupply", value = "List all internal supply")
	public List<InternalSupply> retrieveAll() {
		return repository.findAll();
	}
	
	@GetMapping(value = "/internalSupply/page")
	@ApiOperation(nickname="retrieveInternalSupplyPaged", value = "List paged")
	@ApiPageable
	public Page<InternalSupply> retrivePageable(@ApiIgnore @NonNull Pageable pageable){
		return repository.findAll(pageable);
	}

	@GetMapping(value = "/internalSupply/filter")
	@ApiOperation(nickname="retrieveInternalSupplyFilteredAndPaged", value = "List paged")
	@ApiPageable
	public Page<InternalSupply> retriveFilteredAndPaged(
				@ApiIgnore @NonNull 
				@PageableDefault(page = 0, size = 5) 
				@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
				Pageable pageable,
				String json) {
		Map<String,String> filters = ConversorUtil.toFilter(json);
		Page<InternalSupply> page = repository.filter(filters,pageable);
		return page;
	}

	@GetMapping("/internalSupply/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveInternalSupply", value = "Find one internal supply", notes = "Also returns a link to retrieve all internal supply with rel - all")
	public Resource<InternalSupply> retrieve(@PathVariable long id) {
		Optional<InternalSupply> retrivedObject = repository.findById(id);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		Resource<InternalSupply> resource = new Resource<InternalSupply>(retrivedObject.get());
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAll());
		resource.add(linkTo.withRel("all"));
		return resource;
	}

	@DeleteMapping("/internalSupply/{id}")
	@ApiOperation(nickname="deleteInternalSupply", value = "Delete the internal supply")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}

	@PostMapping("/internalSupply")
	@ApiOperation(nickname="createInternalSupply", value = "Create a new internal supply", notes = "Also returns the url to created data in header location")
	public ResponseEntity<Object> create(@RequestBody InternalSupply internalSupply) {
		InternalSupply savedObject = repository.save(internalSupply);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedObject.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/internalSupply/{id}")
	@ApiOperation(nickname="updateInternalSupply", value = "Edit the internal supply")
	public ResponseEntity<Object> updateInternalSupply(@RequestBody InternalSupply object, @PathVariable long id) {
		Optional<InternalSupply> optional = repository.findById(id);
		if (!optional.isPresent())
			return ResponseEntity.notFound().build();
		object.setId(id);
		repository.save(object);
		return ResponseEntity.noContent().build();
	}
}
