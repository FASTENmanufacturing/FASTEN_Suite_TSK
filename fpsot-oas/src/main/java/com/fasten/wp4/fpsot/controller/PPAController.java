package com.fasten.wp4.fpsot.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class PPAController {

//	@Autowired
//	private PredictionRepository repository;
//
//	@GetMapping(value="/prediction", params = {"!page","!size", "!sort"})
//	@ApiOperation(nickname="retrieveAllPrediction", value = "List all")
//	public List<Prediction> retrieveAll() {
//		return repository.findAll();
//	}
//	
//	@GetMapping(value = "/prediction/page")
//	@ApiOperation(nickname="retrievePredictionPaged", value = "List paged")
//	@ApiPageable
//	public Page<Prediction> retrivePageable(@ApiIgnore @NonNull Pageable pageable){
//		return repository.findAll(pageable);
//	}
//
//	@GetMapping(value = "/prediction/filter")
//	@ApiOperation(nickname="retrievePredictionFilteredAndPaged", value = "List paged and filtered")
//	@ApiPageable
//	public Page<Prediction> retriveFilteredAndPaged(
//				@ApiIgnore @NonNull 
//				@PageableDefault(page = 0, size = 5) 
//				@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
//				Pageable pageable,
//				String json) {
//		Map<String,String> filters = ConversorUtil.toFilter(json);
//		Page<Prediction> page = repository.filter(filters,pageable);
//		return page;
//	}
//
//	@GetMapping("/prediction/{id:[\\d]+}")
//	@ApiOperation(nickname="retrievePrediction", value = "Find the prediction configurations")
//	public Prediction retrieve(@PathVariable long id) {
//		Optional<Prediction> retrivedObject = repository.findById(id);
//		if (!retrivedObject.isPresent())
//			throw new NotFoundException();
//		return retrivedObject.get();
//	}
//
//	@DeleteMapping("/prediction/{id:[\\d]+}")
//	@ApiOperation(nickname="deletePrediction", value = "Delete")
//	public void delete(@PathVariable long id) {
//		repository.deleteById(id);
//	}
//
//	@PostMapping("/prediction")
//	@ApiOperation(nickname="createPrediction", value = "Create", notes = "Also returns the url to created data in header location ")
//	public ResponseEntity<Object> create(@RequestBody Prediction prediction) {
//		Prediction savedObject = repository.save(prediction);
//		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedObject.getId()).toUri();
//		return ResponseEntity.created(location).build();
//	}
//	
//	@PutMapping("/prediction/{id}")
//	@ApiOperation(nickname="updatePrediction", value = "Update a optimization")
//	public ResponseEntity<Object> updatePrediction(@RequestBody Prediction object, @PathVariable long id) {
//		Optional<Prediction> optional = repository.findById(id);
//		if (!optional.isPresent())
//			return ResponseEntity.notFound().build();
//		object.setId(id);
//		repository.save(object);
//		return ResponseEntity.noContent().build();
//	}
}
