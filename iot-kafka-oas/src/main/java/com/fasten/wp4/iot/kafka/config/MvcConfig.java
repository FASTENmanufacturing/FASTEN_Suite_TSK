package com.fasten.wp4.iot.kafka.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Autowired
	private MessageSource messageSource;

	@Override
	public void addViewControllers( ViewControllerRegistry registry) {
//		registry.addViewController("/").setViewName("redirect:/index.html");
//		registry.addViewController("/**/*.html");
	}

}