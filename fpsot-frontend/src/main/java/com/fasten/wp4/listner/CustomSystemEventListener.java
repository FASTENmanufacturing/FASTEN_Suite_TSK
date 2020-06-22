package com.fasten.wp4.listner;

import javax.faces.application.Application;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasten.wp4.infra.client.DatabaseClient;
import com.fasten.wp4.infra.client.EmailClient;
import com.fasten.wp4.infra.client.IoTKafkaClient;
import com.fasten.wp4.infra.client.OpenRouteServiceGeocodeClient;
import com.fasten.wp4.infra.client.OptimizatorOperationalClient;
import com.fasten.wp4.infra.client.OptimizatorTacticalClient;
import com.fasten.wp4.infra.client.PredictionClient;
import com.fasten.wp4.infra.client.ProbabilityDistributionClient;
import com.fasten.wp4.probabilitydistribution.client.model.ProbabilityDistribuitionRequest;

public class CustomSystemEventListener implements SystemEventListener {
	
		private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	   @Override
	   public void processEvent(SystemEvent event) throws AbortProcessingException {
	      
	      if(event instanceof PostConstructApplicationEvent) {
	    	  LOGGER.info("Application Started with environmental variables:"+ "\n" +
	    			  "DATABASE_OAS_URL:"			+	DatabaseClient.DATABASE_OAS_URL 						+ "\n" +
	    			  "EMAIL_OAS_URL:"				+	EmailClient.EMAIL_OAS_URL 								+ "\n" +
	    			  "IOT_KAFKA_OAS_URL:"			+	IoTKafkaClient.IOT_KAFKA_OAS_URL					+ "\n" + 
	    			  "OPTIMIZATOR_OPERATIONAL_URL:"+	OptimizatorOperationalClient.OPTIMIZATOR_OPERATIONAL_URL+ "\n" + 
	    			  "OPTIMIZATOR_TACTICAL_URL:"	+	OptimizatorTacticalClient.OPTIMIZATOR_TACTICAL_URL		+ "\n" + 
	    			  "ORS_API_KEY:"				+	OpenRouteServiceGeocodeClient.ORS_API_KEY				+ "\n" + 
	    			  "PPA_URL:"					+	PredictionClient.PPA_URL								+ "\n" +  
	    			  "PROBABILITY_DISTRIBUTION_URL:"+	ProbabilityDistributionClient.PROBABILITY_DISTRIBUTION_URL+ "\n" +
	    			  "GRAFANA_URL:"+	System.getenv("GRAFANA_URL") + "\n"  
	    			  );
	      }
	   }

	   @Override
	   public boolean isListenerForSource(Object source) {
		   return (source instanceof Application);
	   }	
	}
