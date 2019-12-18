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
import com.fasten.wp4.database.model.SRAMsAllocated;
import com.fasten.wp4.database.repository.SRAMsAllocatedRepository;
import com.fasten.wp4.database.swagger.ApiPageable;
import com.fasten.wp4.database.util.ConversorUtil;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class AllocatedSRAMController {

	@Autowired
	private SRAMsAllocatedRepository repository;

	@GetMapping(value="/SRAMsAllocated", params = {"!page","!size", "!sort"})
	@ApiOperation(nickname="retrieveAllSRAMsAllocated", value = "List all SRAMs allocated")
	public List<SRAMsAllocated> retrieveAll() {
		return repository.findAll();
	}
	
	@GetMapping(value = "/SRAMsAllocated/page")
	@ApiOperation(nickname="retrieveSRAMsAllocatedPaged", value = "List paged")
	@ApiPageable
	public Page<SRAMsAllocated> retrivePageable(@ApiIgnore @NonNull Pageable pageable){
		return repository.findAll(pageable);
	}
	
	@GetMapping(value = "/SRAMsAllocated/filter")
	@ApiOperation(nickname="retrieveSRAMsAllocatedFilteredAndPaged", value = "List paged")
	@ApiPageable
	public Page<SRAMsAllocated> retriveFilteredAndPaged(
				@ApiIgnore @NonNull 
				@PageableDefault(page = 0, size = 5) 
				@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
				Pageable pageable,
				String json) {
		Map<String,String> filters = ConversorUtil.toFilter(json);
		Page<SRAMsAllocated> page = repository.filter(filters,pageable);
		return page;
	}

	@GetMapping("/SRAMsAllocated/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveSRAMsAllocated", value = "Find one configuration", notes = "Also returns a link to retrieve all SRAMs allocated with rel - all")
	public Resource<SRAMsAllocated> retrieve(@PathVariable long id) {
		Optional<SRAMsAllocated> retrivedObject = repository.findById(id);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		Resource<SRAMsAllocated> resource = new Resource<SRAMsAllocated>(retrivedObject.get());
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAll());
		resource.add(linkTo.withRel("all"));
		return resource;
	}

	@DeleteMapping("/SRAMsAllocated/{id}")
	@ApiOperation(nickname="deleteSRAMsAllocated", value = "Delete the SRAM allocated in the RS")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}

	@PostMapping("/SRAMsAllocated")
	@ApiOperation(nickname="createSRAMsAllocated", value = "Allocate a new SRAM in some Remote Station", notes = "Also returns the url to created data in header location")
	public ResponseEntity<Object> create(@RequestBody SRAMsAllocated object) {
		SRAMsAllocated savedObject = repository.save(object);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedObject.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/SRAMsAllocated/{id}")
	@ApiOperation(nickname="updateSRAMsAllocated", value = "Edit the aumont of SRAMs allocated in a RS")
	public ResponseEntity<Object> updateSRAMsAllocated(@RequestBody SRAMsAllocated object, @PathVariable long id) {
		Optional<SRAMsAllocated> optional = repository.findById(id);
		if (!optional.isPresent())
			return ResponseEntity.notFound().build();
		object.setId(id);
		repository.save(object);
		return ResponseEntity.noContent().build();
	}
}
