//package com.fasten.wp4.database.controller;
//
//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
//
//import java.net.URI;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.data.web.SortDefault;
//import org.springframework.hateoas.Resource;
//import org.springframework.hateoas.mvc.ControllerLinkBuilder;
//import org.springframework.http.ResponseEntity;
//import org.springframework.lang.NonNull;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import com.fasten.wp4.database.exception.NotFoundException;
//import com.fasten.wp4.database.model.Production;
//import com.fasten.wp4.database.repository.ProductionRepository;
//import com.fasten.wp4.database.swagger.ApiPageable;
//import com.fasten.wp4.database.util.ConversorUtil;
//
//import io.swagger.annotations.ApiOperation;
//import springfox.documentation.annotations.ApiIgnore;
//
//@RestController
//public class ProductionController {
//
//	@Autowired
//	private ProductionRepository repository;
//
//	@GetMapping(value="/production", params = {"!page","!size", "!sort"})
//	@ApiOperation(nickname="retrieveAllProduction", value = "List all RS that has productions")
//	public List<Production> retrieveAll() {
//		return repository.findAll();
//	}
//	
//	@GetMapping(value = "/production/page")
//	@ApiOperation(nickname="retrieveProcessingPartPaged", value = "List paged")
//	@ApiPageable
//	public Page<Production> retrivePageable(@ApiIgnore @NonNull Pageable pageable){
//		return repository.findAll(pageable);
//	}
//
//	@GetMapping(value = "/production/filter")
//	@ApiOperation(nickname="retrieveProcessingPartFilteredAndPaged", value = "List paged")
//	@ApiPageable
//	public Page<Production> retriveFilteredAndPaged(
//				@ApiIgnore @NonNull 
//				@PageableDefault(page = 0, size = 5) 
//				@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
//				Pageable pageable,
//				String json) {
//		Map<String,String> filters = ConversorUtil.toFilter(json);
//		Page<Production> page = repository.filter(filters,pageable);
//		return page;
//	}
//
//	@GetMapping("/production/{id:[\\d]+}")
//	@ApiOperation(nickname="retrieveProduction", value = "Find one RS that has production", notes = "Also returns a link to retrieve all production with rel - all")
//	public Resource<Production> retrieve(@PathVariable long id) {
//		Optional<Production> retrivedObject = repository.findById(id);
//		if (!retrivedObject.isPresent())
//			throw new NotFoundException();
//		Resource<Production> resource = new Resource<Production>(retrivedObject.get());
//		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAll());
//		resource.add(linkTo.withRel("all"));
//		return resource;
//	}
//
//	@DeleteMapping("/production/{id}")
//	@ApiOperation(nickname="deleteProduction", value = "Delete the production condition")
//	public void delete(@PathVariable long id) {
//		repository.deleteById(id);
//	}
//
//	@PostMapping("/production")
//	@ApiOperation(nickname="createProduction", value = "Create a new production configuration", notes = "Also returns the url to created data in header location")
//	public ResponseEntity<Object> create(@RequestBody Production production) {
//		Production savedObject = repository.save(production);
//		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedObject.getId()).toUri();
//		return ResponseEntity.created(location).build();
//	}
//	
//	@PutMapping("/production/{id}")
//	@ApiOperation(nickname="updateProduction", value = "Edit the production of a supply")
//	public ResponseEntity<Object> updateProduction(@RequestBody Production object, @PathVariable long id) {
//		Optional<Production> optional = repository.findById(id);
//		if (!optional.isPresent())
//			return ResponseEntity.notFound().build();
//		object.setId(id);
//		repository.save(object);
//		return ResponseEntity.noContent().build();
//	}
//}
