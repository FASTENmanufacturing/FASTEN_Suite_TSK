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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasten.wp4.database.exception.NotFoundException;
import com.fasten.wp4.database.model.ProcessingPart;
import com.fasten.wp4.database.repository.ProcessingPartRepository;
import com.fasten.wp4.database.swagger.ApiPageable;
import com.fasten.wp4.database.util.ConversorUtil;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class ProcessingPartController {

	@Autowired
	private ProcessingPartRepository repository;
	
	@GetMapping(value = "/processingPart", params = {"!page","!size", "!sort"})
	@ApiOperation(nickname="retrieveAllProcessingPart", value = "List all processing parts info")
	public List<ProcessingPart> retrieveAll() {
		return repository.retrieveAllOrderBySramCode();
	}
	
	@GetMapping(value = "/processingPart/page")
	@ApiOperation(nickname="retrieveProcessingPartPaged", value = "List paged")
	@ApiPageable
	public Page<ProcessingPart> retrivePageable(@ApiIgnore @NonNull Pageable pageable){
		return repository.findAll(pageable);
	}

	@GetMapping(value = "/processingPart/filter")
	@ApiOperation(nickname="retrieveProcessingPartFilteredAndPaged", value = "List paged")
	@ApiPageable
	public Page<ProcessingPart> retriveFilteredAndPaged(
				@ApiIgnore @NonNull 
				@PageableDefault(page = 0, size = 5) 
				@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
				Pageable pageable,
				String json) {
		Map<String,String> filters = ConversorUtil.toFilter(json);
		Page<ProcessingPart> page = repository.filter(filters,pageable);
		return page;
	}
	
	@GetMapping("/processingPart/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveProcessingPart", value = "Find one info about processing a parts", notes = "Also returns a link to retrieve all processing parts with rel - all")
	public ProcessingPart retrieve(@PathVariable long id) {
		Optional<ProcessingPart> retrivedObject = repository.findById(id);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}
	
	@GetMapping(value="/processingPart/SRAMCode", params = {"partCode","SRAMCode"})
	@ApiOperation(nickname="retrieveProcessingPartByPartCodeAndSRAMCode", value = "Find one info about processing a part by part code and SRAM code", notes = "Also returns a link to retrieve all processing parts with rel - all")
	public ProcessingPart retrieveByPartCodeAndSRAMCode(@RequestParam(name = "partCode") String partCode, @RequestParam(name = "SRAMCode") String SRAMCode) {
		Optional<ProcessingPart> retrivedObject = repository.findByPartCodeAndSRAMCode(partCode, SRAMCode);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}
	
	@GetMapping(value="/processingPart/partName/{partName}")
	@ApiOperation(nickname="retrieveProcessingPartByPart", value = "Find processing parts by part name")
	public List<ProcessingPart> retrieveByPart(@PathVariable String partName) {
		return repository.findByPartNameIgnoreCase(partName);
	}

	@GetMapping(value="/processingPart/maxHoursProcessing")
	@ApiOperation(nickname="retrieveMaxHoursProcessing", value = "Find max time in hours spent on processing parts ")
	public Long maxHoursProcessing() {
		return repository.retrieveMaxHoursProcessing();
	}

	@DeleteMapping("/processingPart/{id}")
	@ApiOperation(nickname="deleteProcessingPart", value = "Delete the processing part info")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}

	@PostMapping("/processingPart")
	@ApiOperation(nickname="createProcessingPart", value = "Create a new processing part info", notes = "Also returns the url to created data in header location")
	public ResponseEntity<Object> create(@RequestBody ProcessingPart processingPart) {
		ProcessingPart savedObject = repository.save(processingPart);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedObject.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/processingPart/{id}")
	@ApiOperation(nickname="updateProcessingPart", value = "Edit the processing part info")
	public ResponseEntity<Object> updateProcessingPart(@RequestBody ProcessingPart object, @PathVariable long id) {
		Optional<ProcessingPart> optional = repository.findById(id);
		if (!optional.isPresent())
			return ResponseEntity.notFound().build();
		object.setId(id);
		repository.save(object);
		return ResponseEntity.noContent().build();
	}
}


//@PostMapping(value = "/processingPart/page")
//@ApiOperation(nickname="retrieveProcessingPartPaged", value = "List paged ")
//public Page<ProcessingPart> retrivePaged(	
////		@RequestParam(value = "page", required = false, defaultValue = "0") int page, 
////		@RequestParam(value = "size", required = false, defaultValue = "5") int size, 
////		@RequestParam(value = "sort", required = false, defaultValue = "id,desc") String sort,
//		@PageableDefault(page = 0, size = 5) @SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) }) Pageable pageable,
//		@RequestBody(required = false) Filter<ProcessingPart> filter){
//	Map<String,String> params =filter.getParams();
//	ProcessingPart processingPart = filter.getEntity();
////	Pageable pageable = PageRequest.of(page,size, ConversorUtil.toSort(sort));
//	return repository.findAll(pageable);
////	return repository.filter(params, processingPart, pageable);
//}

//@PostMapping(value = "/processingPart/filter")
//@ApiOperation(nickname="retrieveProcessingPartFilteredAndPagedPost", value = "List paged")
//@ApiPageable
//public Page<ProcessingPart> retriveFilteredAndPagedPost(
//			@ApiIgnore @NonNull 
//			@PageableDefault(page = 0, size = 5) 
//			@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
//			Pageable pageable,
//			@RequestBody(required = false) Filter<ProcessingPart> filter){
//	Page<ProcessingPart> page = repository.filter(filter, pageable);
//	return page;
//}

//@GetMapping(value = "/processingPart/filter")
//@ApiOperation(nickname="retrieveProcessingPartFilteredAndPaged", value = "List paged")
//@ApiPageable
//public Page<ProcessingPart> retriveFilteredAndPaged(
//			@ApiIgnore @NonNull 
//			@PageableDefault(page = 0, size = 5) 
//			@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
//			Pageable pageable,
//			ProcessingPart processingPart){
//	Page<ProcessingPart> page = repository.filter(processingPart, pageable);
//	return page;
//}