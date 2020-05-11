package com.fasten.wp4;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class DatabaseOAS {

	public static void main(String[] args) {
		SpringApplication.run(DatabaseOAS.class, args);
	}
}

@Component
class MyBean {

    @Value("${spring.datasource.url}")
    private String prop;

    public MyBean() {

    }

    @PostConstruct
    public void init() {
        System.out.println("================== " + prop + "================== ");
    }
}