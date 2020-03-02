package com.fasten.wp4.database.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.fasten.wp4.database.model.Demand;
import com.fasten.wp4.database.model.Prediction;
import com.fasten.wp4.database.model.RemoteStation;
import com.fasten.wp4.database.repository.DemandRepository;
import com.fasten.wp4.database.repository.PredictionRepository;
import com.fasten.wp4.database.swagger.ApiPageable;
import com.fasten.wp4.database.util.ConversorUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class DemandController {

	@Autowired
	private DemandRepository repository;
	
	@Autowired
	private PredictionRepository predictionRepository;

	@GetMapping(value="/demand",params = {"!page","!size", "!sort"})
	@ApiOperation(nickname="retrieveAllDemand", value = "List all demands")
	public List<Demand> retrieveAll() {
		return repository.findAll();
	}
	
	@GetMapping(value="/demand/orderDate",params = {"!page","!size", "!sort"})
	@ApiOperation(nickname="retrieveAllOrderByOrderDate", value = "List all demands")
	public List<Demand> retrieveAllOrderByOrderDate() {
		return repository.retrieveAllOrderByOrderDate();
	}
	
	@GetMapping(value = "/demand/page")
	@ApiOperation(nickname="retrieveDemandPaged", value = "List paged")
	@ApiPageable
	public Page<Demand> retrivePageable(@ApiIgnore @NonNull Pageable pageable){
		return repository.findAll(pageable);
	}

	@GetMapping(value = "/demand/filter")
	@ApiOperation(nickname="retrieveDemandFilteredAndPaged", value = "List paged")
	@ApiPageable
	public Page<Demand> retriveFilteredAndPaged(
				@ApiIgnore @NonNull 
				@PageableDefault(page = 0, size = 5) 
				@SortDefault.SortDefaults({ @SortDefault(sort = "id", direction = Sort.Direction.DESC) })  
				Pageable pageable,
				String json) {
		Map<String,String> filters = ConversorUtil.toFilter(json);
		Page<Demand> page = repository.filter(filters,pageable);
		return page;
	}
	
//	@GetMapping(value="/demand/candidates", params = { "start", "end"})
//	public Integer retrieveCandidates(	@ApiParam(example="2015/06/26") @RequestParam(value = "start") 	@DateTimeFormat(pattern="yyyy/MM/dd") Date start,
//										@ApiParam(example="2017/06/28")	@RequestParam(value = "end")	@DateTimeFormat(pattern="yyyy/MM/dd") Date end) {
//		Integer count = repository.retrieveCandidates(start, end);
//		return count;
//	}
	
	@GetMapping(value="/demand/candidates", params = { "start", "end"})
	@ApiOperation(nickname="retrieveCandidates", value = "Count candidates in damand range (date format MM/dd/yyyy)")
	public Integer retrieveCandidatesStr(	@ApiParam(example="06/26/2015") @RequestParam(value = "start") 	String start,
											@ApiParam(example="06/28/2017")	@RequestParam(value = "end")	String end
										) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		Date startDate = format.parse(start);
		Date endDate=format.parse(end);
		Integer count = repository.retrieveCandidates(startDate, endDate);
		return count;
	}
	
	@GetMapping(value="/demand/quantity", params = { "start", "end"})
	@ApiOperation(nickname="retrieveQuantityBetween", value = "Count demands in damand range (date format MM/dd/yyyy)")
	public Integer retrieveQuantityBetween(	@ApiParam(example="06/26/2015") @RequestParam(value = "start") 	String start,
											@ApiParam(example="06/28/2017")	@RequestParam(value = "end")	String end
										) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		Date startDate = format.parse(start);
		Date endDate=format.parse(end);
		Integer count = repository.retrieveQuantityBetween(startDate, endDate);
		return count;
	}
	
	@GetMapping(value="/demand/between", params = { "start", "end"})
	@ApiOperation(nickname="retrieveBetween", value = "Count demands in damand range (date format MM/dd/yyyy)")
	public List<Demand> retrieveBetween(	@ApiParam(example="06/26/2015") @RequestParam(value = "start") 	String start,
											@ApiParam(example="06/28/2017")	@RequestParam(value = "end")	String end
										) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		Date startDate = format.parse(start);
		Date endDate=format.parse(end);
		List<Demand> list = repository.retrieveBetween(startDate, endDate);
		return list;
	}
	
	@GetMapping(value="/demand/prediction", params = { "prediction"})
	@ApiOperation(nickname="retrieveByPrediction", value = "Retrieve demands for a prediction study")
	public List<Demand> retrieveByPrediction( @RequestParam(value = "prediction") Long id) throws ParseException {
		Optional<Prediction> retrivedObject = predictionRepository.findById(id);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		return repository.retrieveByPrediction(retrivedObject.get());
	}
	
	@GetMapping(value="/demand/predictionParams")
	@ApiOperation(nickname="retrieveByPredictionParams", value = "Retrieve demands for a prediction study")
	public Integer retrieveByPredictionParams( @ApiParam(example="06/26/2015",required=false) @RequestParam(value = "start",required=false) String start,
													@ApiParam(example="06/28/2017",required=false)	@RequestParam(value = "end",required=false)	String end,
													@ApiParam(example="1",required=false) @RequestParam(value = "remoteStationId",required=false) Long remoteStationId,
													@ApiParam(example="1",required=false) @RequestParam(value = "partId",required=false) Long partId) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		Date startDate = null;
		try{startDate = format.parse(start);}catch(Exception e){};
		Date endDate=null;
		try{endDate=format.parse(end);}catch(Exception e){};

		return repository.retrieveByPredictionParams(startDate, endDate, remoteStationId, partId).size();
	}
	
	@GetMapping(value = "/demand/tacticalOptimization", params = { "id"})
	@ApiOperation(nickname="retrieveDemandByTacticalOptimization", value = "Retrive a list of demands in a Tactical Optimization Study")
	public List<Demand> retrieveByTacticalOptimization(@ApiParam(example="1") @RequestParam(value = "id") Long id) {
		return repository.retrieveDemandByTacticalOptimization(id);
	}
	
	@GetMapping(value="/demand/candidates/withoutCoordinates", params = { "start", "end"})
	@ApiOperation(nickname="retrieveCandidatesWithoutCoordinates", value = "Count candidates in damand range (date format MM/dd/yyyy)")
	public Integer retrieveCandidatesWithoutCoordinatesStr(	@ApiParam(example="06/26/2015") @RequestParam(value = "start") 	String start,
											@ApiParam(example="06/28/2017")	@RequestParam(value = "end")	String end
										) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		Date startDate = format.parse(start);
		Date endDate=format.parse(end);
		Integer count = repository.retrieveCandidatesWithoutCoordinates(startDate, endDate);
		return count;
	}

	
	@GetMapping("/demand/code/{code}")
	@ApiOperation(nickname="retrieveDemandByCode", value = "Find one demand by code", notes = "Also returns a link to retrieve all demand with rel - all")
	public Resource<Demand> retrieveByCode(@PathVariable String code) {
		Optional<Demand> retrivedObject = repository.findByCode(code);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		Resource<Demand> resource = new Resource<Demand>(retrivedObject.get());
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAll());
		resource.add(linkTo.withRel("all"));
		return resource;
	}

	@GetMapping("/demand/{id:[\\d]+}")
	@ApiOperation(nickname="retrieveDemand", value = "Find one demand", notes = "Also returns a link to retrieve all demand with rel - all")
	public Resource<Demand> retrieve(@PathVariable long id) {
		Optional<Demand> retrivedObject = repository.findById(id);
		if (!retrivedObject.isPresent())
			throw new NotFoundException();
		Resource<Demand> resource = new Resource<Demand>(retrivedObject.get());
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAll());
		resource.add(linkTo.withRel("all"));
		return resource;
	}

	@DeleteMapping("/demand/{id}")
	@ApiOperation(nickname="deleteDemand",value = "Delete the demand")
	public void delete(@PathVariable long id) {
		repository.deleteById(id);
	}

	@PostMapping("/demand")
	@ApiOperation(nickname="createDemand",value = "Create a new demand", notes = "Also returns the url to created data in header location")
	public ResponseEntity<Object> create(@RequestBody Demand demand) {
		Demand savedObject = repository.save(demand);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedObject.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/demand/{id}")
	@ApiOperation(nickname="updateDemand",value = "Edit the demand")
	public ResponseEntity<Object> updateDemand(@RequestBody Demand object, @PathVariable long id) {
		Optional<Demand> optional = repository.findById(id);
		if (!optional.isPresent())
			return ResponseEntity.notFound().build();
		object.setId(id);
		repository.save(object);
		return ResponseEntity.noContent().build();
	}
}
