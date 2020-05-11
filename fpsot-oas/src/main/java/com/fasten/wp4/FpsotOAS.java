package com.fasten.wp4;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableRetry
public class FpsotOAS {

	public static void main(String[] args) {
		SpringApplication.run(FpsotOAS.class, args);
	}
}

@Component
class MyBean {

    @Value("${fastengateway-client.url}")
    private String prop;

    public MyBean() {

    }

    @PostConstruct
    public void init() {
        System.out.println("================== " + prop + "================== ");
    }
}