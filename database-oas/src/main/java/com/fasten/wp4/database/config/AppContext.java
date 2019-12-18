package com.fasten.wp4.database.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/*
 * Exposes AppContext to use in static classes
 * like in ConversorUtils
 */
@Component
public class AppContext implements ApplicationContextAware{
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AppContext.applicationContext = applicationContext;
    }

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
