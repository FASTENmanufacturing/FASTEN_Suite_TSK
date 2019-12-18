package com.fasten.wp4.iot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasten.wp4.iot.config.KafkaUtils;
import com.fasten.wp4.iot.model.Listner;
import com.fasten.wp4.iot.model.Producer;
import com.fasten.wp4.iot.repository.ListnerRepository;
import com.fasten.wp4.iot.repository.ProducerRepository;
import com.fasten.wp4.iot.repository.TopicRepository;

@Controller
public class IndexController {

	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private TopicConfiguration topicConfiguration;
	
	@Autowired
	private TopicRepository topicRepository;
	
	@Autowired
	private ListnerRepository listnerRepository;
	
	@Autowired
	private ProducerRepository producerRepository;
	
	@Autowired
	KafkaUtils kafkaUtils;
	
	@RequestMapping(value = {"/","/index*"}, method = RequestMethod.GET)
	public String index(Model model) {
		model.addAttribute("topicConfiguration",topicConfiguration);
		model.addAttribute("topics",topicRepository.findAll());
		model.addAttribute("listners",listnerRepository.findAll());
		model.addAttribute("producers",producerRepository.findAll());
		return "index";
	}
	
	@GetMapping("/listner/toggle/{id}")
	public String onOffListner(@PathVariable("id") Listner listner, Model model) {
		//toggle value
		listner.setEnabled(!listner.getEnabled());
		//save
		listnerRepository.save(listner);
	    //subscribe unsubscribe topic
	    if(listner.getEnabled()) {
	    	kafkaUtils.startByListner(listner.getName());
	    	LOGGER.info("Subscribing listner" + listner.getName());
	    }else {
	    	kafkaUtils.stopByListner(listner.getName());
	    	LOGGER.info("Unsubscribing listner" + listner.getName());
	    }
	    return "redirect:/index.html";
	}
	
	@GetMapping("/producer/toggle/{id}")
	public String onOffProducer(@PathVariable("id") Producer producer, Model model) {
		//toggle value
		producer.setEnabled(!producer.getEnabled());
		//save
		producerRepository.save(producer);
	    return "redirect:/index.html";
	}

}
