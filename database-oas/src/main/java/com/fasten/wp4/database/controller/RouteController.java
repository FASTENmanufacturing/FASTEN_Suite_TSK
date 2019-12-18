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
import com.fasten.wp4.database.model.Route;
import com.fasten.wp4.database.repository.RouteRepository;
import com.fasten.wp4.database.swagger.ApiPageable;
import com.fasten.wp4.database.util.ConversorUtil;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class RouteController {

	@Autowired
	private RouteRepository repository;

	@GetMapping(value = "/route", params = {"!page","!size", "!sort"})
	@ApiOperation(nickname="retrieveAllRoute", value = "List all routes")
	public List<Route> retrieveAll() {
		return repository.findAll();
	}
	
	@GetMapping(value = "/route/page")
	@ApiOperation(nickname="retrieveRoutePaged", value = "List paged")
	@ApiPageable
	public Page<Route> retrivePageable(@ApiIgnore @NonNull Pageable pageable){
		return repository.findAll(pageable);
	}

	@GetMapping(value = "/route/filter")
	@ApiOperation(nickname="retrieveRouteFilteredAndPaged", value = "List paged")
	@ApiPageable
	public Page<Route> retriveFilteredAndPaged(
				@ApiIgnore @NonNull 
				@PageableDefault(page = 0, size = 5) 
				@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
				Pageable pageable,
				String json) {
		Map<String,String> filters = ConversorUtil.toFilter(json);
		Page<Route> page = repository.filter(filters,pageable);
		return page;
	}

	@GetMapping("/route/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveRoute", value = "Find one route", notes = "Also returns a link to retrieve all routes with rel - all")
	public Resource<Route> retrieve(@PathVariable long id) {
		Optional<Route> retrivedObject = repository.findById(id);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		Resource<Route> resource = new Resource<Route>(retrivedObject.get());
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAll());
		resource.add(linkTo.withRel("all"));
		return resource;
	}

	@DeleteMapping("/route/{id}")
	@ApiOperation(nickname="deleteRoute", value = "Delete the route")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}

	@PostMapping("/route")
	@ApiOperation(nickname="createRoute", value = "Create a new route", notes = "Also returns the url to created data in header location")
	public ResponseEntity<Object> create(@RequestBody Route route) {
		Route savedObject = repository.save(route);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedObject.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/route/{id}")
	@ApiOperation(nickname="updateRoute", value = "Edit the route")
	public ResponseEntity<Object> updateRoute(@RequestBody Route object, @PathVariable long id) {
		Optional<Route> optional = repository.findById(id);
		if (!optional.isPresent())
			return ResponseEntity.notFound().build();
		object.setId(id);
		repository.save(object);
		return ResponseEntity.noContent().build();
	}
}
