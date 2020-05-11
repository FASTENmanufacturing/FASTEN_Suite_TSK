package com.fasten.wp4;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableScheduling
public class IoTKafkaOAS {

	public static void main(String[] args) {
		SpringApplication.run(IoTKafkaOAS.class, args);
	}
	
	@Component
	class MyBean {

	    @Value("${spring.kafka.bootstrap-servers}")
	    private String prop;

	    public MyBean() {

	    }

	    @PostConstruct
	    public void init() {
	        System.out.println("================== " + prop + "================== ");
	    }
	}


}