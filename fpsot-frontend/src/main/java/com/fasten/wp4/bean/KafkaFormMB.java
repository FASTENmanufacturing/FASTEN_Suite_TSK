/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fasten.wp4.bean;

import static com.fasten.wp4.util.Utils.addDetailMessage;

import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;

import com.fasten.wp4.iot.kafka.client.api.KafkaControllerApi;
import com.fasten.wp4.iot.kafka.client.invoker.ApiException;
import com.fasten.wp4.iot.kafka.client.model.HistoricalDemandRequest;
import com.fasten.wp4.iot.kafka.client.model.HistoricalTimesRequest;
import com.fasten.wp4.iot.kafka.client.model.ProbabilityDistribuitionsRequest;
import com.github.adminfaces.template.exception.BusinessException;

@Named
@ViewScoped
public class KafkaFormMB implements Serializable {
	
	@Inject
	private transient KafkaControllerApi kafkaControllerApi;
	
	private HistoricalDemandRequest historicalDemandRequest;
	
	private HistoricalTimesRequest historicalTimesRequest;
	
	private ProbabilityDistribuitionsRequest probabilityDistribuitionsRequest;

	public HistoricalDemandRequest getHistoricalDemandRequest() {
		return historicalDemandRequest;
	}

	public HistoricalTimesRequest getHistoricalTimesRequest() {
		return historicalTimesRequest;
	}
	
	public ProbabilityDistribuitionsRequest getProbabilityDistribuitionsRequest() {
		return probabilityDistribuitionsRequest;
	}

	public void setProbabilityDistribuitionsRequest(ProbabilityDistribuitionsRequest probabilityDistribuitionsRequest) {
		this.probabilityDistribuitionsRequest = probabilityDistribuitionsRequest;
	}

	public void setHistoricalTimesRequest(HistoricalTimesRequest historicalTimesRequest) {
		this.historicalTimesRequest = historicalTimesRequest;
	}

	public void setHistoricalDemandRequest(HistoricalDemandRequest historicalDemandRequest) {
		this.historicalDemandRequest = historicalDemandRequest;
	}

	public void init() {
		if (Faces.isAjaxRequest()) {
			return;
		}
		clear();
	}

	public void clear() {
		historicalDemandRequest = new HistoricalDemandRequest();
		historicalTimesRequest = new HistoricalTimesRequest();
		probabilityDistribuitionsRequest = new ProbabilityDistribuitionsRequest();
	}
	
	public void getHistoricalDemand() {
		
		try {
			kafkaControllerApi.getHistoricalDemand(historicalDemandRequest);
		} catch (ApiException e) {
			throw new BusinessException("Could not request a historical demand sync");
		}
		
		FacesContext context = FacesContext.getCurrentInstance();
		String msg = context.getApplication().evaluateExpressionGet(context, "#{adm['kafka.requested']}",String.class);
		addDetailMessage(msg);
		clear();
	}
	
	public void getHistoricalTimes() {
		
		try {
			kafkaControllerApi.getHistoricalTimes(historicalTimesRequest);
		} catch (ApiException e) {
			throw new BusinessException("Could not request a historical times sync");
		}
		
		FacesContext context = FacesContext.getCurrentInstance();
		String msg = context.getApplication().evaluateExpressionGet(context, "#{adm['kafka.requested']}",String.class);
		addDetailMessage(msg);
		clear();
	}
	
	public void getProbabilityDistribuitions() {
		
		try {
			kafkaControllerApi.getProbabilityDistribuitions(probabilityDistribuitionsRequest);
		} catch (ApiException e) {
			throw new BusinessException("Could not request a probability distribuition sync");
		}
		
		FacesContext context = FacesContext.getCurrentInstance();
		String msg = context.getApplication().evaluateExpressionGet(context, "#{adm['kafka.requested']}",String.class);
		addDetailMessage(msg);
		clear();
	}
	
	

}
