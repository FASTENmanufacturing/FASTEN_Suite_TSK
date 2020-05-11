package com.fasten.wp4.bean;

import static com.fasten.wp4.util.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;

import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Faces;

import com.fasten.wp4.database.client.api.DemandControllerApi;
import com.fasten.wp4.database.client.api.PartControllerApi;
import com.fasten.wp4.database.client.api.PredictionControllerApi;
import com.fasten.wp4.database.client.api.RemoteStationControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.Part;
import com.fasten.wp4.database.client.model.Prediction;
import com.fasten.wp4.database.client.model.RemoteStation;
import com.github.adminfaces.template.exception.BusinessException;

@Named
@ViewScoped
public class PredictionFormMB implements Serializable {

	private Serializable id;

	List<Part> parts;

	List<RemoteStation> remoteStations;

	private Prediction prediction = new Prediction();

	@Inject
	private transient DemandControllerApi demandControllerApi;

	@Inject
	private transient PredictionControllerApi predictionControllerApi;

	@Inject
	private transient PartControllerApi partControllerApi;

	@Inject
	private transient RemoteStationControllerApi remoteStationControllerApi;
	
	private Integer pastObservations=null;

	public void init() {
		
		if(Faces.isAjaxRequest()){
			return;
		}
		
		if (has(id) && !"".equals(id)) {
			try {
				prediction = predictionControllerApi.retrievePrediction(Long.valueOf(id.toString()));
			} catch (ApiException e) {
				clear();
				throw new BusinessException(String.format("Could not retrive prediction study with id %s, a new one will be initialized.", id));
			}
			populateSelectPart();
			populateSelectRemoteStation();
		}
		
		selectPredictionType();
	}
	
	public void clear() {
		prediction = new Prediction();
		id = null;
		pastObservations = null;
	}
	
	public boolean isNew() {
		return prediction == null || prediction.getId() == null;
	}

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
	}

	public Prediction getPrediction() {
		return prediction;
	}
	public void setPrediction(Prediction prediction) {
		this.prediction = prediction;
	}
	
	public void save() {
		if (isNew()) {
			try {
				predictionControllerApi.createPrediction(prediction);
			} catch (ApiException e) {
				throw new BusinessException("Could not create prediction study");
			}
			addDetailMessage("Prediction study created successfully");
		} else {
			try {
				predictionControllerApi.updatePrediction(prediction.getId(),prediction);
			} catch (ApiException e) {
				throw new BusinessException("Could not update prediction study");
			}
			addDetailMessage("Prediction study updated successfully");
		}
	}

	public void remove() throws IOException {
		if (has(prediction) && has(prediction.getId())) {
			try {
				predictionControllerApi.deletePrediction(prediction.getId());
				addDetailMessage("Prediction study removed successfully");
				Faces.getFlash().setKeepMessages(true);
				Faces.redirect("prediction-list.xhtml");
			} catch (ApiException e) {
				throw new BusinessException("Could not delete prediction study");
			}
		}else {
			throw new BusinessException("Cannot delete prediction study without id.");
		}
	}

	public String getBreadcum() {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle admin = context.getApplication().evaluateExpressionGet(context, "#{adm}", ResourceBundle.class);
		String entityName = "Prediction";//admin.getString("prediction");
		String newExpression = admin.getString("breadcum.new");
		if(prediction.getId() == null) {
			return MessageFormat.format(newExpression, entityName);
		}else {
			return StringUtils.capitalize(entityName).concat(" "+prediction.getId());
		}
	}

	public void populateSelectPart() {
		try {
			parts= partControllerApi.retrieveAllPart();
		} catch (ApiException e) {
			throw new BusinessException("Could not retrive list");
		}finally{
		}
	}

	public void populateSelectRemoteStation() {
		try {
			remoteStations= remoteStationControllerApi.retrieveAllRemoteStation();
		} catch (ApiException e) {
			throw new BusinessException("Could not retrive list");
		}finally{
		}
	}

	public List<Part> getParts() {
		if(parts==null) {
			parts=new ArrayList<Part>();
			return parts;
		}else if(parts.isEmpty()){
			populateSelectPart();
		}
		return parts;
	}

	public void setParts(List<Part> parts) {
		this.parts = parts;
	}

	public List<RemoteStation> getRemoteStations() {
		if(remoteStations==null) {
			remoteStations=new ArrayList<RemoteStation>();
			return remoteStations;
		}else if(remoteStations.isEmpty()) {
			populateSelectRemoteStation();
		}
		return remoteStations;
	}

	public void setRemoteStations(List<RemoteStation> remoteStations) {
		this.remoteStations = remoteStations;
	}

	private String predictionType;

	public String getPredictionType() {
		return predictionType;
	}

	public void setPredictionType(String predictionType) {
		this.predictionType = predictionType;
	}
	
	public Integer getPastObservations() {
		return pastObservations;
	}

	public void setPastObservations(Integer pastObservations) {
		this.pastObservations = pastObservations;
	}

	public void updatePastObservations() {
		try {
			String start = null;
			String end = null;
			Long partId = null;
			Long remoteStationId=null;
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			if(has(prediction.getInitialDate())){
				start = format.format(prediction.getInitialDate());
			}
			if(has(prediction.getEndDate())){
				end=format.format(prediction.getEndDate());
			}
			if(has(prediction.getPart()) && has(prediction.getPart().getId())){
				partId=prediction.getPart().getId();
			}
			if(has(prediction.getRemoteStation()) && has(prediction.getRemoteStation().getId())){
				remoteStationId=prediction.getRemoteStation().getId();
			}

			pastObservations = demandControllerApi.retrieveByPredictionParams(end, partId, remoteStationId, start);
		} catch (ApiException e) {
			throw new BusinessException("Could not get quantity of observations for this period.");
		}
	}

	public void onRadioSelect() {
		if(predictionType.contentEquals("auto")) {
			prediction.setEndDate(null);
			prediction.setInitialDate(null);
		}
	}

	public void selectPredictionType() {
		if(has(prediction) && has(prediction.getInitialDate()) && has(prediction.getEndDate()) ) {
			predictionType="filter";
		}else {
			predictionType="auto";
		}
		
	}

}
