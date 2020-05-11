package com.fasten.wp4.database;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.util.ResourceUtils;

@Configuration
@ComponentScan
@PropertySource(ResourceUtils.CLASSPATH_URL_PREFIX + "persistence.properties")
public class PersistenceAutoConfiguration {
	
}
