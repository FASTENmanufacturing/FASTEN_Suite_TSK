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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasten.wp4.database.exception.NotFoundException;
import com.fasten.wp4.database.model.Delivery;
import com.fasten.wp4.database.model.RemoteStation;
import com.fasten.wp4.database.repository.DeliveryRepository;
import com.fasten.wp4.database.swagger.ApiPageable;
import com.fasten.wp4.database.util.ConversorUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class DeliveryController {

	@Autowired
	private DeliveryRepository repository;

	@GetMapping(value="/delivery", params = {"!page","!size", "!sort"})
	@ApiOperation(nickname="retrieveAllDelivery",value = "List all delivery's cost and time")
	public List<Delivery> retrieveAll() {
		return repository.findAll();
	}

	@GetMapping(value = "/delivery/page")
	@ApiOperation(nickname="retrieveDeliveryPaged", value = "List paged")
	@ApiPageable
	public Page<Delivery> retrivePageable(@ApiIgnore @NonNull Pageable pageable){
		return repository.findAll(pageable);
	}

	@GetMapping(value = "/delivery/filter")
	@ApiOperation(nickname="retrieveDeliveryFilteredAndPaged", value = "List paged")
	@ApiPageable
	public Page<Delivery> retriveFilteredAndPaged(
				@ApiIgnore @NonNull 
				@PageableDefault(page = 0, size = 5) 
				@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
				Pageable pageable,
				String json) {
		Map<String,String> filters = ConversorUtil.toFilter(json);
		Page<Delivery> page = repository.filter(filters,pageable);
		return page;
	}
	
	@GetMapping("/delivery/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveDelivery", value = "Find one delivery", notes = "Also returns a link to retrieve all delivery with rel - all")
	public Delivery retrieve(@PathVariable long id) {
		Optional<Delivery> retrivedObject = repository.findById(id);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}
	
	@GetMapping(value = "/delivery/tacticalOptimization", params = { "id"})
	@ApiOperation(nickname="retrieveDeliveryMatrixByTacticalOptimization", value = "Retrive a list of Delivery locations in a Tactical Optimization Study")
	public List<Delivery> retrieveDeliveryMatrixByTacticalOptimization(@ApiParam(example="1") @RequestParam(value = "id") Long id) {
		return repository.retrieveDeliveryMatrixByTacticalOptimization(id);
	}
	
	
	
	@GetMapping(value="/delivery/route", params = {"origin","destination"})
	@ApiOperation(nickname="retrieveDeliveryByRoute", value = "Find one delivery by Route", notes = "Also returns a link to retrieve all delivery with rel - all")
	public Resource<Delivery> retrieveByRoute(@RequestParam(name = "origin") RemoteStation origin, @RequestParam(name = "destination")RemoteStation destination) {
		Optional<Delivery> retrivedObject = repository.findByOriginAndDestination(origin,destination);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		Resource<Delivery> resource = new Resource<Delivery>(retrivedObject.get());
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAll());
		resource.add(linkTo.withRel("all"));
		return resource;
	}
	
	//https://stackoverflow.com/questions/55000123/how-to-map-two-same-urls-with-different-params-in-springfox-with-springboot-rest
	@GetMapping(value="/delivery/routeCode", params = {"originCode","destinationCode"})
	@ApiOperation(nickname="retrieveDeliveryByRouteCode", value = "Find one delivery by Route", notes = "Also returns a link to retrieve all delivery with rel - all")
	public Resource<Delivery> retrieveByRouteCode(@RequestParam(name = "originCode") String originCode, @RequestParam(name = "destinationCode")String destinationCode) {
		Optional<Delivery> retrivedObject = repository.findByOriginCodeAndDestinationCode(originCode,destinationCode);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		Resource<Delivery> resource = new Resource<Delivery>(retrivedObject.get());
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAll());
		resource.add(linkTo.withRel("all"));
		return resource;
	}

	@DeleteMapping("/delivery/{id}")
	@ApiOperation(nickname="deleteDelivery", value = "Delete the delivery")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}

	@PostMapping("/delivery")
	@ApiOperation(nickname="createDelivery", value = "Create a new delivery cost", notes = "Also returns the url to created data in header location")
	public ResponseEntity<Object> create(@RequestBody Delivery delivery) {
		Delivery savedObject = repository.save(delivery);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedObject.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/delivery/{id}")
	@ApiOperation(nickname="updateDelivery", value = "Edit the inserted data about delivery costs")
	public ResponseEntity<Object> updateDelivery(@RequestBody Delivery object, @PathVariable long id) {
		Optional<Delivery> optional = repository.findById(id);
		if (!optional.isPresent())
			return ResponseEntity.notFound().build();
		object.setId(id);
		repository.save(object);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/deliveries")
	@ApiOperation(nickname="updateDeliveries", value = "Batch update a list of deliveries")
	public ResponseEntity<Object> updateDeliveries(@RequestBody List<Delivery> list) {
		repository.saveAll(list);
		return ResponseEntity.noContent().build();
	}
}
