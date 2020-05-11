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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasten.wp4.database.exception.NotFoundException;
import com.fasten.wp4.database.model.WebHook;
import com.fasten.wp4.database.repository.WebHookRepository;
import com.fasten.wp4.database.swagger.ApiPageable;
import com.fasten.wp4.database.util.ConversorUtil;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class WebHookController {

	@Autowired
	private WebHookRepository repository;

	@GetMapping(value="/webhook", params = {"!page","!size", "!sort"})
	@ApiOperation(nickname="retrieveAllWebHook", value = "List all")
	public List<WebHook> retrieveAll() {
		return repository.findAll();
	}
	
	@GetMapping(value="/webhook/consumerServiceName/{consumerServiceName}")
	@ApiOperation(nickname="retrieveWebHookByConsumerServiceName", value = "Find WebHook by consumerServiceName equals")
	public WebHook retrieveWebHookByConsumerServiceName(@PathVariable String consumerServiceName) {
		Optional<WebHook> retrivedObject = repository.findByConsumerServiceName(consumerServiceName);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}
	
	@GetMapping(value="/webhook/event/{event}")
	@ApiOperation(nickname="retrieveWebHooksByEvent", value = "Find WebHooks by event")
	public List<WebHook> retrieveWebHooksByEvent(@PathVariable String event) {
		List<WebHook> retrivedList = repository.findByEvent(event);
		return retrivedList;
	}
	
	@GetMapping(value = "/webhook/page")
	@ApiOperation(nickname="retrieveWebHookPaged", value = "List paged")
	@ApiPageable
	public Page<WebHook> retrivePageable(@ApiIgnore @NonNull Pageable pageable){
		return repository.findAll(pageable);
	}

	@GetMapping(value = "/webhook/filter")
	@ApiOperation(nickname="retrieveWebHookFilteredAndPaged", value = "List paged")
	@ApiPageable
	public Page<WebHook> retriveFilteredAndPaged(
				@ApiIgnore @NonNull 
				@PageableDefault(page = 0, size = 5) 
				@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
				Pageable pageable,
				String json) {
		Map<String,String> filters = ConversorUtil.toFilter(json);
		Page<WebHook> page = repository.filter(filters,pageable);
		return page;
	}

	@GetMapping("/webhook/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveWebHook", value = "Find a webhook")
	public WebHook retrieve(@PathVariable long id) {
		Optional<WebHook> retrivedObject = repository.findById(id);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return retrivedObject.get();
	}

	@DeleteMapping("/webhook/{id:[\\d]+}")
	@ApiOperation(nickname="deleteWebHook", value = "Delete a webhook")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}

	@PostMapping("/webhook")
	@ApiOperation(nickname="createWebHook", value = "Save a webhook", notes = "Also returns the url to created data in header location ")
	public ResponseEntity<Object> create(@RequestBody WebHook webhook) {
		WebHook savedObject = repository.save(webhook);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedObject.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/webhook/{id}")
	@ApiOperation(nickname="updateWebHook", value = "Update a webhook")
	public ResponseEntity<Object> updateWebHook(@RequestBody WebHook object, @PathVariable long id) {
		Optional<WebHook> optional = repository.findById(id);
		if (!optional.isPresent())
			return ResponseEntity.notFound().build();
		object.setId(id);
		repository.save(object);
		return ResponseEntity.noContent().build();
	}
}

