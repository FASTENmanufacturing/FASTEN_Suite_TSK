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
import com.fasten.wp4.database.model.Stock;
import com.fasten.wp4.database.repository.StockRepository;
import com.fasten.wp4.database.swagger.ApiPageable;
import com.fasten.wp4.database.util.ConversorUtil;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class StockController {

	@Autowired
	private StockRepository repository;

	@GetMapping(value="/stock", params = {"!page","!size", "!sort"})
	@ApiOperation(nickname="retrieveAllStock", value = "List all stock's configuration")
	public List<Stock> retrieveAll() {
		return repository.findAll();
	}
	
	@GetMapping(value = "/stock/page")
	@ApiOperation(nickname="retrieveStockPaged", value = "List paged")
	@ApiPageable
	public Page<Stock> retrivePageable(@ApiIgnore @NonNull Pageable pageable){
		return repository.findAll(pageable);
	}

	@GetMapping(value = "/stock/filter")
	@ApiOperation(nickname="retrieveStockFilteredAndPaged", value = "List paged")
	@ApiPageable
	public Page<Stock> retriveFilteredAndPaged(
				@ApiIgnore @NonNull 
				@PageableDefault(page = 0, size = 5) 
				@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
				Pageable pageable,
				String json) {
		Map<String,String> filters = ConversorUtil.toFilter(json);
		Page<Stock> page = repository.filter(filters,pageable);
		return page;
	}

	@GetMapping("/stock/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveStock", value = "Find one stock configuration", notes = "Also returns a link to retrieve all stock's configuration with rel - all")
	public Resource<Stock> retrieve(@PathVariable long id) {
		Optional<Stock> retrivedObject = repository.findById(id);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		Resource<Stock> resource = new Resource<Stock>(retrivedObject.get());
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAll());
		resource.add(linkTo.withRel("all"));
		return resource;
	}

	@DeleteMapping("/stock/{id}")
	@ApiOperation(nickname="deleteStock", value = "Delete the stock configuration")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}

	@PostMapping("/stock")
	@ApiOperation(nickname="createStock", value = "Create a new stock configuration", notes = "Also returns the url to created data in header location")
	public ResponseEntity<Object> create(@RequestBody Stock stock) {
		Stock savedObject = repository.save(stock);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedObject.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/stock/{id}")
	@ApiOperation(nickname="updateStock", value = "Edit the configured stock")
	public ResponseEntity<Object> updateStock(@RequestBody Stock object, @PathVariable long id) {
		Optional<Stock> optional = repository.findById(id);
		if (!optional.isPresent())
			return ResponseEntity.notFound().build();
		object.setId(id);
		repository.save(object);
		return ResponseEntity.noContent().build();
	}
}
