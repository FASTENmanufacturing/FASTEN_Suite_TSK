package com.github.adminfaces.starter.bean;

import static com.github.adminfaces.starter.util.ApiUtils.getIdFromLocationHeader;
import static com.github.adminfaces.starter.util.LazyDataModelUtils.toApiFilters;
import static com.github.adminfaces.starter.util.LazyDataModelUtils.toApiPage;
import static com.github.adminfaces.starter.util.LazyDataModelUtils.toApiSize;
import static com.github.adminfaces.starter.util.LazyDataModelUtils.toApiSort;
import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.primefaces.PrimeFaces;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.fasten.wp4.database.client.api.DemandControllerApi;
import com.fasten.wp4.database.client.api.DemandProjectionStudyControllerApi;
import com.fasten.wp4.database.client.api.PartControllerApi;
import com.fasten.wp4.database.client.api.PredictionControllerApi;
import com.fasten.wp4.database.client.api.RemoteStationControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.Demand;
import com.fasten.wp4.database.client.model.DemandProjectionStudy;
import com.fasten.wp4.database.client.model.PageOfPrediction;
import com.fasten.wp4.database.client.model.Prediction;
import com.fasten.wp4.predictive.client.api.ForecastApi;
import com.fasten.wp4.predictive.client.invoker.ApiCallback;
import com.fasten.wp4.predictive.client.model.ForecastingStudy;
import com.fasten.wp4.predictive.client.model.StudyResults;
import com.github.adminfaces.starter.infra.async.AsyncCall;
import com.github.adminfaces.starter.infra.async.AsyncRequestUtils;
import com.github.adminfaces.starter.util.PredictionApiUtils;
import com.github.adminfaces.template.exception.BusinessException;
import com.squareup.okhttp.Call;

@Named
@ViewScoped
public class PredictionListMB implements Serializable {

	@Inject
	private transient PredictionControllerApi predictionControllerApi;

	@Inject
	private transient PartControllerApi partControllerApi;

	@Inject
	private transient RemoteStationControllerApi remoteStationControllerApi;

	@Inject
	private transient ForecastApi forecastApi;

	@Inject
	private transient DemandControllerApi demandControllerApi;
	
	@Inject
	private transient DemandProjectionStudyControllerApi demandProjectionStudyControllerApi;
	
	@Inject
	AsyncNotifyMB asyncNotifyMB;
	
	Long id;

	List<String> parts;

	List<String> remoteStations;

	LazyDataModel<Prediction> predictions;

	// datatable filteredValue attribute (column filters)
	List<Prediction> filteredValue;

	List<Prediction> selecteds;
	
	@Inject
	List<AsyncCall> asyncCalls;

	public void init() {
		if(Faces.isAjaxRequest()){
			return;
		}
		selecteds = new ArrayList<Prediction>();

		try {
			parts= partControllerApi.retrieveAllDistinctByName();
			remoteStations= remoteStationControllerApi.retrieveAllByName();
		} catch (ApiException e) {
			throw new BusinessException("Could not retrive list");
		}finally{
		}
	}

	@PostConstruct
	public void initDataModel() {

		predictions = new LazyDataModel<Prediction>() {

			@Override
			public List<Prediction> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {

				if(has(id))
					filters.put("id",id+"");

				List<Prediction> list = Arrays.asList();
				try {
					PageOfPrediction page = predictionControllerApi.retrievePredictionFilteredAndPaged(toApiFilters(filters), toApiPage(first, pageSize), toApiSize(pageSize), toApiSort( multiSortMeta));
					list=page.getContent();
					setRowCount(page.getTotalElements().intValue());
				} catch (ApiException e) {
					addDetailMessage("Could not retrive list", FacesMessage.SEVERITY_ERROR);
				}
				return list;
			}
			
			//select from list
			@Override
		    public Prediction getRowData(String key) {
				return this.getWrappedData().parallelStream().filter(entity -> key.equals(entity.getId().toString())).findAny().orElse(null);
		    }

			//select from server
//			@Override
//			public Prediction getRowData(String key) {
//				try {
//					Prediction prediction = predictionControllerApi.retrievePrediction(new Long(key));
//					selecteds.add(prediction);
//					return prediction;
//				} catch (ApiException e) {
//					throw  new BusinessException("Not found with id " + id);
//				}finally{
//				}
//			}
		};
	}

	public List<String> getParts() {
		return parts;
	}


	public List<String> getRemoteStations() {
		return remoteStations;
	}

	public void delete() {
		int numPrediction = 0;
		for (Prediction selected : selecteds) {
			numPrediction++;
			try {
				predictionControllerApi.deletePrediction(selected.getId());
			} catch (ApiException e) {
				throw  new BusinessException("Prediction not found with id " + id);
			} finally {}
		}
		selecteds.clear();
		addDetailMessage(numPrediction + " prediction(s) deleted successfully!");
	}
	
	public boolean hasResult() {
		if(selecteds.size()!=1) {
			return false;
		}else {
			Prediction selected = selecteds.get(0);
			try{
				return demandProjectionStudyControllerApi.existsByPrediction(selected.getId());
			}catch(ApiException e) {
				throw  new BusinessException("Could not verify if this prediction has any results");
			}
		}
	}
	
	public void viewResult(Prediction prediction) {
		DemandProjectionStudy demandProjectionStudy = null;
		try{
			demandProjectionStudy = demandProjectionStudyControllerApi.retrieveByStudy(prediction.getId());
			String url = "http://150.162.6.64:3000/d/NWnEalFWk/%s?"
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
            String panelId = "16";
            String var_dashboard_part_id = (prediction.getPart()!=null && prediction.getPart().getId()!=null)?prediction.getPart().getId().toString():"All";
            String var_dashboard_remote_station_id= (prediction.getRemoteStation()!=null && prediction.getRemoteStation().getId()!=null)?prediction.getRemoteStation().getId().toString():"All";
            String  var_dashboard_study_id= demandProjectionStudy.getStudy().getId().toString();
            String  var_dashboard_demand_subtype= "All";
            
            String fullURL = String.format(url, dashboardPath, orgId, from, to, refresh, var_dashboard_part_id, var_dashboard_remote_station_id, var_dashboard_study_id, panelId,var_dashboard_demand_subtype);
            
			PrimeFaces.current().executeScript("window.open('"+fullURL+"', '_newtab')");
		}catch(ApiException e) {
			throw  new BusinessException("Could not show results");
		}
	}
	
	public void execute(Prediction prediction) {
		try {
			List<Demand> realDemand = demandControllerApi.retrieveByPrediction(prediction.getId().toString());
			ForecastingStudy forecastingStudy = PredictionApiUtils.convertInput(prediction, realDemand);
			String requestId = UUID.randomUUID().toString();
			
			Call c = forecastApi.postMainClassAsync(/*TODO requestId,*/forecastingStudy, new ApiCallback<StudyResults>() {
				@Override
				public void onFailure(com.fasten.wp4.predictive.client.invoker.ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
					 throw  new BusinessException("Could not execute prediction study");
				}
				@Override
				public void onSuccess(StudyResults result, int statusCode, Map<String, List<String>> responseHeaders) {
					try {
					DemandProjectionStudy demandProjectionStudy = PredictionApiUtils.convertOutput(prediction, realDemand, result);
					demandProjectionStudy.setId(getIdFromLocationHeader(demandProjectionStudyControllerApi.createDemandProjectionStudyWithHttpInfo(demandProjectionStudy)));
					asyncNotifyMB.sendPushMessage("prediction_execution");
					} catch (Exception e) {
						throw  new BusinessException("Could not save prediction study results");
					}
				}

				@Override
				public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {}

				@Override
				public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {}
			});
			AsyncCall asyncCall = AsyncRequestUtils.getAsyncCallFrom(forecastApi.getApiClient().getHttpClient().getDispatcher(), c,requestId);
			asyncCall.setStudy(prediction);
			asyncCalls.add(asyncCall);
			 
			selecteds.clear();
			addDetailMessage("Execution of prediction study requested successfully!");
		} catch (Exception e) {
			e.printStackTrace();
			throw  new BusinessException("Could not obtaind data to execute the prediction study");
		}
	}


	public List<Prediction> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<Prediction> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public List<Prediction> getSelecteds() {
		return selecteds;
	}

	public void setSelecteds(List<Prediction> selecteds) {
		this.selecteds = selecteds;
	}

	public LazyDataModel<Prediction> getPredictions() {
		return predictions;
	}

	public void setPredictions(LazyDataModel<Prediction> predictions) {
		this.predictions = predictions;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
