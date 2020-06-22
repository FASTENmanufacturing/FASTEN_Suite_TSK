package com.fasten.wp4.bean;

import static com.fasten.wp4.util.ApiUtils.getIdFromLocationHeader;
import static com.fasten.wp4.util.LazyDataModelUtils.toApiFilters;
import static com.fasten.wp4.util.LazyDataModelUtils.toApiPage;
import static com.fasten.wp4.util.LazyDataModelUtils.toApiSize;
import static com.fasten.wp4.util.LazyDataModelUtils.toApiSort;
import static com.fasten.wp4.util.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
import com.fasten.wp4.database.client.api.DistributionCenterControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.Demand;
import com.fasten.wp4.database.client.model.DemandProjected.DemandSubtypeEnum;
import com.fasten.wp4.database.client.model.DemandProjectionStudy;
import com.fasten.wp4.database.client.model.ErrorProjected;
import com.fasten.wp4.database.client.model.PageOfPrediction;
import com.fasten.wp4.database.client.model.Prediction;
import com.fasten.wp4.database.client.model.ErrorProjected.ErrorMethodEnum;
import com.fasten.wp4.database.client.model.ErrorProjected.ErrorTypeEnum;
import com.fasten.wp4.infra.async.AsyncCall;
import com.fasten.wp4.infra.async.AsyncRequestUtils;
import com.fasten.wp4.predictive.client.api.ForecastApi;
import com.fasten.wp4.predictive.client.invoker.ApiCallback;
import com.fasten.wp4.predictive.client.model.ForecastingStudy;
import com.fasten.wp4.predictive.client.model.StudyResults;
import com.fasten.wp4.util.PredictionApiUtils;
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
	private transient DistributionCenterControllerApi distributionCenterControllerApi;

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

	List<String> distributionCenters;

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
			distributionCenters= distributionCenterControllerApi.retrieveAllDistributionCenterByName();
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


	public List<String> getDistributionCenters() {
		return distributionCenters;
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
	
	public boolean getResult(Prediction c) {
		try{
			return demandProjectionStudyControllerApi.existsByPrediction(c.getId());
		}catch(ApiException e) {
			throw  new BusinessException("Could not verify if this prediction has any results");
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
	

	public void viewResultIframe(Prediction prediction) {
		Faces.redirect("prediction-result.xhtml?id="+prediction.getId());
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
