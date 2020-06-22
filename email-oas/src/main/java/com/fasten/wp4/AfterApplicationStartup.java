package com.fasten.wp4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

@Component
public class AfterApplicationStartup {

	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	@EventListener(ApplicationReadyEvent.class)
	public void doAfterStartup(){

		SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();
		
	}

}
