package com.fasten.wp4.iot.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasten.wp4.iot.exception.NotFoundException;
import com.fasten.wp4.iot.kafka.publisher.GetHistoricalDemandEventPublisher;
import com.fasten.wp4.iot.kafka.publisher.GetHistoricalTimesEventPublisher;
import com.fasten.wp4.iot.kafka.publisher.GetProbabilityDistribuitionsEventPublisher;
import com.fasten.wp4.iot.kafka.request.HistoricalDemandRequest;
import com.fasten.wp4.iot.kafka.request.HistoricalTimesRequest;
import com.fasten.wp4.iot.kafka.request.ProbabilityDistribuitionsRequest;
import com.fasten.wp4.iot.model.Topic;
import com.fasten.wp4.iot.repository.TopicRepository;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
public class KafkaRequestController {

	
	@Autowired
	private TopicRepository topicRepository;
	
	@Autowired
	private GetHistoricalDemandEventPublisher getHistoricalDemandEventPublisher;

	@Autowired
	private GetHistoricalTimesEventPublisher getHistoricalTimesEventPublisher;

	@Autowired
	private GetProbabilityDistribuitionsEventPublisher getProbabilityDistribuitionsEventPublisher;

	@GetMapping("/fire")
	@ApiOperation(nickname="fire", value = "Fires a event on a Topic")
	public ResponseEntity<Object> create(
			@ApiParam(example="06/25/2016") @RequestParam(value = "start",required=false) 	String start,
			@ApiParam(example="06/28/2017")	@RequestParam(value = "end",required=false)	String end,
			@ApiParam(example="get_historical_times")	@RequestParam(value = "topic",required=true) String topicName) throws ParseException {
		
		
		Optional<Topic> retrivedObject = topicRepository.findByName(topicName);
		if (!retrivedObject.isPresent()) {
			throw new NotFoundException();
		}
		
		if(topicName.equals("get_historical_demand")) {
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			if(start!=null && end!=null) {
				Date initialDate = format.parse(start);
				Date endDate=format.parse(end);
			
				HistoricalDemandRequest request = new HistoricalDemandRequest();
				request.setInitialDate(initialDate);
				request.setEndDate(endDate);
			
				getHistoricalDemandEventPublisher.publish(request);
			}
		}else if(topicName.equals("get_historical_times")) {
			
			HistoricalTimesRequest request = new HistoricalTimesRequest();
			getHistoricalTimesEventPublisher.publish(request);
			
		}else if(topicName.equals("get_probability_distribuitions")) {
			
			ProbabilityDistribuitionsRequest request = new ProbabilityDistribuitionsRequest();
			getProbabilityDistribuitionsEventPublisher.publish(request);
			
		}else {
			throw new NotFoundException();
		}
		
		return ResponseEntity.noContent().build();
	}
	
	
	
	@PostMapping("/getHistoricalDemand")
	@ApiOperation(nickname="getHistoricalDemand", value = "Fires a Historical Demand Request")
	public ResponseEntity<Object> getHistoricalDemand(@RequestBody HistoricalDemandRequest request) throws ParseException {
		
		
		Optional<Topic> retrivedObject = topicRepository.findByName("get_historical_demand");
		if (!retrivedObject.isPresent()) {
			throw new NotFoundException();
		}
		getHistoricalDemandEventPublisher.publish(request);
		
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/getHistoricalTimes")
	@ApiOperation(nickname="getHistoricalTimes", value = "Fires a Historical Times Request")
	public ResponseEntity<Object> getHistoricalTimes(@RequestBody HistoricalTimesRequest request) throws ParseException {
		
		Optional<Topic> retrivedObject = topicRepository.findByName("get_historical_times");
		if (!retrivedObject.isPresent()) {
			throw new NotFoundException();
		}
		getHistoricalTimesEventPublisher.publish(request);
		
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/getProbabilityDistribuitions")
	@ApiOperation(nickname="getProbabilityDistribuitions", value = "Fires a Probability Distribuitions Request")
	public ResponseEntity<Object> getProbabilityDistribuitions(@RequestBody ProbabilityDistribuitionsRequest request) throws ParseException {
		
		Optional<Topic> retrivedObject = topicRepository.findByName("get_probability_distribuitions");
		if (!retrivedObject.isPresent()) {
			throw new NotFoundException();
		}
		getProbabilityDistribuitionsEventPublisher.publish(request);
		
		return ResponseEntity.noContent().build();
	}
	
}
