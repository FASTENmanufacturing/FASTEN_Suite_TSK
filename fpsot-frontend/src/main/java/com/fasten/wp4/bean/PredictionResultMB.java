package com.fasten.wp4.bean;

import static com.github.adminfaces.template.util.Assert.has;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Faces;
import org.primefaces.PrimeFaces;

import com.fasten.wp4.database.client.api.DemandControllerApi;
import com.fasten.wp4.database.client.api.DemandProjectionStudyControllerApi;
import com.fasten.wp4.database.client.api.PredictionControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.DemandProjectionStudy;
import com.fasten.wp4.database.client.model.ErrorProjected;
import com.fasten.wp4.database.client.model.Prediction;
import com.fasten.wp4.database.client.model.ErrorProjected.ErrorMethodEnum;
import com.fasten.wp4.database.client.model.ErrorProjected.ErrorTypeEnum;
import com.github.adminfaces.template.exception.BusinessException;

@Named
@ViewScoped
public class PredictionResultMB implements Serializable {
	
	public static String GRAFANA_URL =  System.getenv("GRAFANA_URL");

	private Serializable id;

	private Prediction prediction = new Prediction();
	
	private String url = "";

	@Inject
	private transient DemandControllerApi demandControllerApi;
	
	@Inject
	private transient DemandProjectionStudyControllerApi demandProjectionStudyControllerApi;

	@Inject
	private transient PredictionControllerApi predictionControllerApi;

	public void init() {
		
		if(Faces.isAjaxRequest()){
			return;
		}
		
		if (has(id) && !"".equals(id)) {
			try {
				prediction = predictionControllerApi.retrievePrediction(Long.valueOf(id.toString()));
				setIframeUrl(prediction);
			} catch (ApiException e) {
				clear();
				throw new BusinessException(String.format("Could not retrive prediction study with id %s, a new one will be initialized.", id));
			}
		}
	}
	
	public void clear() {
		prediction = new Prediction();
		id = null;
		url = "";
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
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
	
	public void setIframeUrl(Prediction prediction) {
		DemandProjectionStudy demandProjectionStudy = null;
		try{
			
			demandProjectionStudy = demandProjectionStudyControllerApi.retrieveByStudy(prediction.getId());
			String url = GRAFANA_URL+"/d-solo/NWnEalFWk/%s?"
					+ "orgId=%s"
					+ "&from=%s"
					+ "&to=%s"
					+ "&refresh=%s"
					+ "&var-dashboard_part_id=%s"
					+ "&var-dashboard_remote_station_id=%s"
					+ "&var-dashboard_study_id=%s"
					+ "&panelId=%s"
					+ "&var-dashboard_demand_subtype=%s"
//					+ "&fullscreen"
					;
            
			String dashboardPath = "tsk-demand-analysis";
			String orgId = "1";
            String from = demandProjectionStudy.getRealDemandConsideredStart().getTime()+"";
            String to = demandProjectionStudy.getPrevisionPeriodConsideredEnd().getTime()+"";
            String refresh = "5s";
            String panelId = "43";
            String var_dashboard_part_id = (prediction.getPart()!=null && prediction.getPart().getId()!=null)?prediction.getPart().getId().toString():"All";
            String var_dashboard_remote_station_id= (prediction.getDistributionCenter()!=null && prediction.getDistributionCenter().getId()!=null)?prediction.getDistributionCenter().getId().toString():"All";
            String  var_dashboard_study_id= demandProjectionStudy.getStudy().getId().toString();
            String  var_dashboard_demand_subtype= "All";
            
            String fullURL = String.format(url, dashboardPath, orgId, from, to, refresh, var_dashboard_part_id, var_dashboard_remote_station_id, var_dashboard_study_id, panelId,var_dashboard_demand_subtype);

            setUrl(fullURL);
		}catch(ApiException e) {
			throw  new BusinessException("Could not show results");
		}
	}
	
	public void viewResult(Prediction prediction) {
		DemandProjectionStudy demandProjectionStudy = null;
		try{
			
			demandProjectionStudy = demandProjectionStudyControllerApi.retrieveByStudy(prediction.getId());
			String url = GRAFANA_URL+"/d/NWnEalFWk/%s?"
					+ "orgId=%s"
					+ "&from=%s"
					+ "&to=%s"
					+ "&refresh=%s"
					+ "&var-dashboard_part_id=%s"
					+ "&var-dashboard_remote_station_id=%s"
					+ "&var-dashboard_study_id=%s"
					+ "&panelId=%s"
					+ "&var-dashboard_demand_subtype=%s"
//					+ "&fullscreen"
					;
            
			String dashboardPath = "tsk-demand-analysis";
			String orgId = "1";
            String from = demandProjectionStudy.getRealDemandConsideredStart().getTime()+"";
            String to = demandProjectionStudy.getPrevisionPeriodConsideredEnd().getTime()+"";
            String refresh = "5s";
            String panelId = "43";
            String var_dashboard_part_id = (prediction.getPart()!=null && prediction.getPart().getId()!=null)?prediction.getPart().getId().toString():"All";
            String var_dashboard_remote_station_id= (prediction.getDistributionCenter()!=null && prediction.getDistributionCenter().getId()!=null)?prediction.getDistributionCenter().getId().toString():"All";
            String  var_dashboard_study_id= demandProjectionStudy.getStudy().getId().toString();
            String  var_dashboard_demand_subtype= "All";
            
            String fullURL = String.format(url, dashboardPath, orgId, from, to, refresh, var_dashboard_part_id, var_dashboard_remote_station_id, var_dashboard_study_id, panelId,var_dashboard_demand_subtype);
            
			PrimeFaces.current().executeScript("window.open('"+fullURL+"', '_newtab')");
		}catch(ApiException e) {
			throw  new BusinessException("Could not show results");
		}
	}
	
	public  String getAutomaticChosen(Prediction c) {
		try{
			DemandProjectionStudy result = demandProjectionStudyControllerApi.retrieveByStudy(c.getId());
			return result.getDemandProjecteds().get(0).getDemandSubtype().getValue();
		}catch(ApiException e) {
			throw  new BusinessException("Could not get prediction results");
		}
	}
	
	public  Date getRealDemandConsideredStart(Prediction c) {
		try{
			DemandProjectionStudy result = demandProjectionStudyControllerApi.retrieveByStudy(c.getId());
			return result.getRealDemandConsideredStart();
		}catch(ApiException e) {
			throw  new BusinessException("Could not get prediction results");
		}
	}
	
	public  Date getRealDemandConsideredEnd(Prediction c) {
		try{
			DemandProjectionStudy result = demandProjectionStudyControllerApi.retrieveByStudy(c.getId());
			return result.getRealDemandConsideredEnd();
		}catch(ApiException e) {
			throw  new BusinessException("Could not get prediction results");
		}
	}
	
	public Integer getPastObservations(Prediction c) {
		try {
			DemandProjectionStudy result = demandProjectionStudyControllerApi.retrieveByStudy(c.getId());
			String start = null;
			String end = null;
			Long partId = null;
			Long distributionCenterId=null;
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			start = format.format(result.getRealDemandConsideredStart());
			end=format.format(result.getRealDemandConsideredEnd());
			if(has(c.getPart()) && has(c.getPart().getId())){
				partId=c.getPart().getId();
			}
			if(has(c.getDistributionCenter()) && has(c.getDistributionCenter().getId())){
				distributionCenterId=c.getDistributionCenter().getId();
			}

			return demandControllerApi.retrieveByPredictionParams(distributionCenterId,end, partId, start);
		} catch (ApiException e) {
			throw new BusinessException("Could not get quantity of observations for this period.");
		}
	}

	public List<ErrorProjected>  getMase(Prediction c) {
		try{
			DemandProjectionStudy result = demandProjectionStudyControllerApi.retrieveByStudy(c.getId());
			List<ErrorProjected> errors = result.getErrors().stream().filter(error-> ErrorMethodEnum.MASE.equals(error.getErrorMethod())).filter(error->ErrorTypeEnum.TEST.equals(error.getErrorType())).collect(Collectors.toList());
			errors.sort(Comparator.comparing(ErrorProjected::getValue));
			return errors;
		}catch(ApiException e) {
			throw  new BusinessException("Could not get prediction results");
		}
	}

}
