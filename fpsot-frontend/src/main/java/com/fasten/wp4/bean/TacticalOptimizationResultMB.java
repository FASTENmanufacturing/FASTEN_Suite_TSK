package com.fasten.wp4.bean;

import static com.github.adminfaces.template.util.Assert.has;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Comparator;
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

import com.fasten.wp4.database.client.api.DistributionCenterControllerApi;
import com.fasten.wp4.database.client.api.RemoteStationControllerApi;
import com.fasten.wp4.database.client.api.TacticalOptimizationControllerApi;
import com.fasten.wp4.database.client.api.TacticalOptimizationResultControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.DistributionCenter;
import com.fasten.wp4.database.client.model.Route;
import com.fasten.wp4.database.client.model.TacticalOptimization;
import com.fasten.wp4.database.client.model.TacticalOptimization.TypeEnum;
import com.fasten.wp4.database.client.model.TacticalOptimizationResult;
import com.github.adminfaces.template.exception.BusinessException;

@Named
@ViewScoped
public class TacticalOptimizationResultMB implements Serializable {

	private Long study;
	
	private TacticalOptimization tacticalOptimization;
	
	private List<TacticalOptimizationResult> tacticalOptimizationResults;

    @Inject
	private transient TacticalOptimizationControllerApi tacticalOptimizationControllerApi;

	private TacticalOptimizationResult tacticalOptimizationResult;

    @Inject
	private transient TacticalOptimizationResultControllerApi tacticalOptimizationResultControllerApi;
    
    @Inject
	private transient RemoteStationControllerApi remoteStationControllerApi;

    @Inject
    private transient DistributionCenterControllerApi distributionCenterControllerApi;

	public void init() {
		if(Faces.isAjaxRequest()){
			return;
		}
		if (has(study)) {
        	try {

        		tacticalOptimization = tacticalOptimizationControllerApi.retrieveTacticalOptimization(study);
        		
        		if(tacticalOptimization.getType().equals(TypeEnum.COST_BENEFIT)) {
        			tacticalOptimizationResult =  tacticalOptimizationResultControllerApi.retrieveOneByTacticalOptimization(study);
        		}else if(tacticalOptimization.getType().equals(TypeEnum.NUMBER_OF_FACILITES)) {
        			tacticalOptimizationResult =  tacticalOptimizationResultControllerApi.retrieveOneByTacticalOptimization(study);
        		}else if(tacticalOptimization.getType().equals(TypeEnum.ANALYSIS)) {
        			tacticalOptimizationResults = tacticalOptimizationResultControllerApi.retrieveByTacticalOptimization(study);
        			if(tacticalOptimizationResult==null) {
        				tacticalOptimizationResult =  tacticalOptimizationResults.stream().filter(r->r.isCostBenefit()!=null&&r.isCostBenefit()).findAny().orElse(null);
        			}
        		}
        		
        		updateMap();
			} catch (ApiException e) {
				throw new BusinessException("Could not retrive tactical optimization result");
			}
		}
	}
	
	public void updateMap() {
		List<DistributionCenter> remotes = tacticalOptimizationResult.getPrinters().stream().map(allocatedSRAM -> {
			try {
				return distributionCenterControllerApi.retrieveDistributionCenterByName(allocatedSRAM.getRemoteStation());
			} catch (ApiException e) {
				return null;
			}}).collect(Collectors.toList());
		PrimeFaces.current().executeScript("renderMap("+distributionCenterControllerApi.getApiClient().getJSON().serialize(remotes)+");");
	}

	public Long getStudy() {
		return study;
	}

	public void setStudy(Long study) {
		this.study = study;
	}

	public TacticalOptimizationResult getTacticalOptimizationResult() {
		return tacticalOptimizationResult;
	}

	public void setTacticalOptimizationResult(TacticalOptimizationResult tacticalOptimizationResult) {
		this.tacticalOptimizationResult = tacticalOptimizationResult;
	}
	
	public List<TacticalOptimizationResult> getTacticalOptimizationResults() {
		return tacticalOptimizationResults;
	}

	public void setTacticalOptimizationResults(List<TacticalOptimizationResult> tacticalOptimizationResults) {
		this.tacticalOptimizationResults = tacticalOptimizationResults;
	}
	
	public TacticalOptimization getTacticalOptimization() {
		return tacticalOptimization;
	}

	public void setTacticalOptimization(TacticalOptimization tacticalOptimization) {
		this.tacticalOptimization = tacticalOptimization;
	}

	public String getBreadcum() {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle admin = context.getApplication().evaluateExpressionGet(context, "#{adm}", ResourceBundle.class);
		String entityName = "Tactical Optimization Result";//admin.getString("tacticalOptimization");
		String newExpression = admin.getString("breadcum.new");
		if(tacticalOptimizationResult.getId() == null) {
			return MessageFormat.format(newExpression, entityName);
		}else {
			return StringUtils.capitalize(entityName).concat(" "+tacticalOptimizationResult.getId());
		}
	}
	

	public int getTotalRemoteStation() {
		return tacticalOptimizationResult.getPrinters().size();
	}
	
	public double getMaxLeadTime() {
		return (tacticalOptimizationResult.getRoutes().stream().mapToDouble(d -> d.getTimeOfTravel()).max().orElse(0));
	}
	
	public int getMaxPrintersByRemote() {
		return new BigDecimal(tacticalOptimizationResult.getPrinters().stream().mapToInt(d -> d.getNumberOfSRAMs()).max().orElse(0)).setScale(0,RoundingMode.HALF_UP).intValue();
	}
	
	public List<Route> getRoutesSorted(){
		Collections.sort(tacticalOptimizationResult.getRoutes(), Comparator.comparing(Route::getRemoteStation)
			            .thenComparing(Route::getDistributionCenter)
			            .thenComparing(Route::getPart));
		return tacticalOptimizationResult.getRoutes();
	}
	
	public int getTotalRemoteStation(TacticalOptimizationResult tacticalOptimizationResult) {
		return tacticalOptimizationResult.getPrinters().size();
	}
	
	public double getMaxLeadTime(TacticalOptimizationResult tacticalOptimizationResult) {
		return (tacticalOptimizationResult.getRoutes().stream().mapToDouble(d -> d.getTimeOfTravel()).max().orElse(0));
	}
	
	public int getMaxPrintersByRemote(TacticalOptimizationResult tacticalOptimizationResult) {
		return new BigDecimal(tacticalOptimizationResult.getPrinters().stream().mapToInt(d -> d.getNumberOfSRAMs()).max().orElse(0)).setScale(0,RoundingMode.HALF_UP).intValue();
	}

	public double getSramCapacity() {
		return tacticalOptimization.getSramCapacity().doubleValue();
	}
	
	public double getTotalProcessingTime() {
		return (tacticalOptimizationResult.getRoutes().stream().mapToDouble(d -> d.getTotalProcessingTime()).sum());
	}
	
	public int getHorizon() {
		return Math.toIntExact(ChronoUnit.DAYS.between(Instant.ofEpochMilli(tacticalOptimization.getInitialDate().getTime()), Instant.ofEpochMilli(tacticalOptimization.getEndDate().getTime())));
	}
	
}
