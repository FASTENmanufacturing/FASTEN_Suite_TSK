package com.fasten.wp4.bean;

import static com.fasten.wp4.util.LazyDataModelUtils.toApiFilters;
import static com.fasten.wp4.util.LazyDataModelUtils.toApiPage;
import static com.fasten.wp4.util.LazyDataModelUtils.toApiSize;
import static com.fasten.wp4.util.LazyDataModelUtils.toApiSort;
import static com.fasten.wp4.util.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.primefaces.PrimeFaces;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

import com.fasten.wp4.database.client.api.OperationalOptimizationControllerApi;
import com.fasten.wp4.database.client.api.OperationalOptimizationResultControllerApi;
import com.fasten.wp4.database.client.api.PartControllerApi;
import com.fasten.wp4.database.client.api.SramControllerApi;
import com.fasten.wp4.database.client.api.StateControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.OperationalOptimization;
import com.fasten.wp4.database.client.model.OperationalOptimizationResult;
import com.fasten.wp4.database.client.model.PageOfOperationalOptimization;
import com.fasten.wp4.fpsot.client.api.HsoControllerApi;
import com.fasten.wp4.infra.async.AsyncCall;
import com.fasten.wp4.infra.async.AsyncRequestUtils;
import com.fasten.wp4.optimizator.operational.client.api.DefaultApi;
import com.fasten.wp4.optimizator.operational.client.invoker.ApiCallback;
import com.fasten.wp4.optimizator.operational.client.model.AllocationResult;
import com.fasten.wp4.optimizator.operational.client.model.AllocationResultOptimizationResult;
import com.github.adminfaces.template.exception.BusinessException;
import com.squareup.okhttp.Call;

@Named
@ViewScoped
public class OperationalOptimizationListMB implements Serializable {

	@Inject
	private transient OperationalOptimizationControllerApi operationalOptimizationControllerApi;

	@Inject
	private transient PartControllerApi partControllerApi;

	@Inject
	private transient StateControllerApi stateControllerApi;
	
	@Inject
	private transient SramControllerApi sramControllerApi;
	
	@Inject
	private transient OperationalOptimizationResultControllerApi operationalOptimizationResultControllerApi;
	
	@Inject
	private transient DefaultApi defaultApi;
	
	@Inject
	private transient HsoControllerApi hsoControllerApi;
	
	@Inject
	List<AsyncCall> asyncCalls;
	
	@Inject
	AsyncNotifyMB asyncNotifyMB;

	Long id;

	List<String> parts;

	List<String> states;
	
	LazyDataModel<OperationalOptimization> entities;

	// datatable filteredValue attribute (column filters)
	List<OperationalOptimization> filteredValue;

	List<OperationalOptimization> selecteds = new ArrayList<OperationalOptimization>();
	
	private HorizontalBarChartModel horizontalBarModel;

	public void init() {
		if(Faces.isAjaxRequest()){
			return;
		}
		populateSelectPart();
		populateSelectState();
	}

	@PostConstruct
	public void initDataModel() {

		entities = new LazyDataModel<OperationalOptimization>() {

			@Override
			public List<OperationalOptimization> load(int first, int pageSize, List<SortMeta> multiSortMeta, Map<String, Object> filters) {

				if(has(id))
					filters.put("id",id+"");

				List<OperationalOptimization> list = Arrays.asList();
				try {
					PageOfOperationalOptimization page = operationalOptimizationControllerApi.retrieveOperationalOptimizationFilteredAndPaged(toApiFilters(filters), toApiPage(first, pageSize), toApiSize(pageSize), toApiSort( multiSortMeta));
					list=page.getContent();
					setRowCount(page.getTotalElements().intValue());
				} catch (ApiException e) {
					addDetailMessage("Could not retrive list", FacesMessage.SEVERITY_ERROR);
				}
				return list;
			}
			
			@Override
		    public OperationalOptimization getRowData(String key) {
				return this.getWrappedData().parallelStream().filter(entity -> key.equals(entity.getId().toString())).findAny().orElse(null);
		    }
		};
	}

	public List<String> getParts() {
		return parts;
	}

	public List<String> getStates() {
		return states;
	}
	
	public void populateSelectPart() {
		try {
			parts= partControllerApi.retrieveAllDistinctByName();
		} catch (ApiException e) {
			throw new BusinessException("Could not retrive part list");
		}
	}

	public void populateSelectState() {
		try {
			states= stateControllerApi.retrieveAllState().stream().map(s->s.getStateAcronym()).collect(Collectors.toList());
		} catch (ApiException e) {
			throw new BusinessException("Could not list states");
		}finally{
		}
	}
	
	public void delete() {
		int numOperationalOptimization = 0;
		for (OperationalOptimization selected : selecteds) {
			numOperationalOptimization++;
			try {
				operationalOptimizationControllerApi.deleteOperationalOptimization(selected.getId());
			} catch (ApiException e) {
				throw  new BusinessException("OperationalOptimization not found with id " + id);
			} finally {}
		}
		selecteds.clear();
		addDetailMessage(numOperationalOptimization + " study(ies) deleted successfully!");
	}
	
	public boolean hasResult() {
		if(selecteds.size()!=1) {
			return false;
		}else {
			OperationalOptimization selected = selecteds.get(0);
			try{
				return operationalOptimizationResultControllerApi.existsByOperationalOptimization(selected.getId());
			}catch(ApiException e) {
				throw  new BusinessException("Could not verify if this study has any results");
			}
		}
	}
	
	String resultsJson;
	
	public String getResultsJson() {
		return resultsJson;
	}
	
	List<OperationalOptimizationResult> results;
	
	public List<OperationalOptimizationResult> getResults() {
		return results;
	}

	public void viewResult(OperationalOptimization operationalOptimization) {
		try{
			results = operationalOptimizationResultControllerApi.retrieveByOperationalOptimization(operationalOptimization.getId());
			resultsJson = operationalOptimizationResultControllerApi.getApiClient().getJSON().serialize(results);
			PrimeFaces.current().executeScript("PF('results').show();");
		}catch(ApiException e) {
			throw  new BusinessException("Could not show results");
		}
	}
	
	//before open
	public void handleLoadContent(AjaxBehaviorEvent event) {
		createHorizontalBarModel(results);
	}
	
	public void handleOpen(AjaxBehaviorEvent event) {
		PrimeFaces.current().executeScript("renderMap("+resultsJson+");");
		createHorizontalBarModel(results);
    }
	
	public void handleClose(AjaxBehaviorEvent event) {
		PrimeFaces.current().executeScript("clearMap();");
	}
	
	
	private void createHorizontalBarModel(List<OperationalOptimizationResult> results) {
        
		horizontalBarModel = new HorizontalBarChartModel();
 
        ChartSeries queueTime = new ChartSeries();
        queueTime.setLabel("Queue");
        ChartSeries timeProduction = new ChartSeries();
        timeProduction.setLabel("Production");
        ChartSeries timeTravel = new ChartSeries();
        timeTravel.setLabel("Travel");

        Collections.reverse(results);
        for (OperationalOptimizationResult operationalOptimizationResult : results) {
        	queueTime.set(operationalOptimizationResult.getSram().getCode(), operationalOptimizationResult.getQueueTime());
        	timeProduction.set(operationalOptimizationResult.getSram().getCode(), operationalOptimizationResult.getTimeProduction());
        	timeTravel.set(operationalOptimizationResult.getSram().getCode(), operationalOptimizationResult.getTimeTravel());
		}
        Collections.reverse(results);
 
        horizontalBarModel.addSeries(queueTime);
        horizontalBarModel.addSeries(timeProduction);
        horizontalBarModel.addSeries(timeTravel);
 
        horizontalBarModel.setTitle("Stacked times");
        horizontalBarModel.setLegendPosition("e");
        horizontalBarModel.setStacked(true);
 
        Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
        xAxis.setLabel("Total time");
        xAxis.setMin(0);
//        xAxis.setMax(200);
 
        Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
        yAxis.setLabel("Ranked locations");
    }
	
	public void execute(OperationalOptimization entity) throws ApiException {
		try {
			
			//hsoControllerApi.executeOperationalOptimization(origin, part, quantity);
			
			String requestId = UUID.randomUUID().toString();
			Call c = defaultApi.allocatePostAsync(requestId, entity.getAddress().getCity().getLongitude().toString(),entity.getAddress().getCity().getLatitude().toString(), entity.getQuantity().toString(), entity.getPart().getName(),new ApiCallback<AllocationResult>() {
				
				@Override
				public void onSuccess(AllocationResult allocatePost, int statusCode, Map<String, List<String>> responseHeaders) {
					try {
						List<OperationalOptimizationResult> results = new ArrayList<OperationalOptimizationResult>();
						for (AllocationResultOptimizationResult allocation : allocatePost.getOptimizationResult()) {
							OperationalOptimizationResult operationalOptimizationResult = new OperationalOptimizationResult();
							operationalOptimizationResult.setQueueTime(allocation.getQueueTime());
							operationalOptimizationResult.setTimeProduction(allocation.getTimeProduction());
							operationalOptimizationResult.setTimeTravel(allocation.getTimeTravel());
							operationalOptimizationResult.setTotalTime(allocation.getTotalTime());
							operationalOptimizationResult.setStudy(entity);
							operationalOptimizationResult.setSram(sramControllerApi.retrieveSRAMByCode(allocation.getSRAM()));
							results.add(operationalOptimizationResult);
						}
						operationalOptimizationResultControllerApi.createOperationalOptimizationResults(results);
						
						asyncNotifyMB.sendPushMessage("operational_optimization_execution");
					} catch (Exception e) {
						throw  new BusinessException("Could not save operational optimization study results");
					}
				}
				
				@Override
				public void onFailure(com.fasten.wp4.optimizator.operational.client.invoker.ApiException e, int statusCode, Map<String, List<String>> responseHeaders) {
					throw  new BusinessException("Could not execute operational optimization study");
				}
				
				@Override
				public void onDownloadProgress(long bytesRead, long contentLength, boolean done) {}
				@Override
				public void onUploadProgress(long bytesWritten, long contentLength, boolean done) {}
			});
			
			AsyncCall asyncCall = AsyncRequestUtils.getAsyncCallFrom(defaultApi.getApiClient().getHttpClient().getDispatcher(), c,requestId);
			asyncCall.setStudy(entity);
			asyncCalls.add(asyncCall);
			
		} catch (com.fasten.wp4.optimizator.operational.client.invoker.ApiException e) {
			e.printStackTrace();
			throw  new BusinessException("Could not execute operational optimization study");
		} 
		
		

		selecteds.clear();
		addDetailMessage("Execution of operational optimization study requested successfully!");
	}

	public HorizontalBarChartModel getHorizontalBarModel() {
        return horizontalBarModel;
    }


	public List<OperationalOptimization> getFilteredValue() {
		return filteredValue;
	}

	public void setFilteredValue(List<OperationalOptimization> filteredValue) {
		this.filteredValue = filteredValue;
	}

	public List<OperationalOptimization> getSelecteds() {
		return selecteds;
	}

	public void setSelecteds(List<OperationalOptimization> selecteds) {
		this.selecteds = selecteds;
	}

	public LazyDataModel<OperationalOptimization> getEntities() {
		return entities;
	}

	public void setEntities(LazyDataModel<OperationalOptimization> entities) {
		this.entities = entities;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
