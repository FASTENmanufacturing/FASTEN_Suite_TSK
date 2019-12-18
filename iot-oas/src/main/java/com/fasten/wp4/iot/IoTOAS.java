package com.fasten.wp4.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class IoTOAS {

	public static void main(String[] args) {
		SpringApplication.run(IoTOAS.class, args);
	}


}