package com.fasten.wp4.bean;

import static com.github.adminfaces.template.util.Assert.has;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Faces;
import org.primefaces.PrimeFaces;

import com.fasten.wp4.database.client.api.RemoteStationControllerApi;
import com.fasten.wp4.database.client.api.TacticalOptimizationResultControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.RemoteStation;
import com.fasten.wp4.database.client.model.TacticalOptimizationResult;
import com.github.adminfaces.template.exception.BusinessException;

@Named
@ViewScoped
public class TacticalOptimizationResultMB implements Serializable {

	private Long study;

	private TacticalOptimizationResult tacticalOptimizationResult;

    @Inject
	private transient TacticalOptimizationResultControllerApi tacticalOptimizationResultControllerApi;
    
    @Inject
	private transient RemoteStationControllerApi remoteStationControllerApi;

	public void init() {
		if(Faces.isAjaxRequest()){
			return;
		}
		if (has(study)) {
        	try {
        		tacticalOptimizationResult = tacticalOptimizationResultControllerApi.retrieveByTacticalOptimization(study);
        		List<RemoteStation> remotes = tacticalOptimizationResult.getPrinters().stream().map(allocatedSRAM -> allocatedSRAM.getRemoteStation()).collect(Collectors.toList());
        		PrimeFaces.current().executeScript("renderMap("+remoteStationControllerApi.getApiClient().getJSON().serialize(remotes)+");");
			} catch (ApiException e) {
				throw new BusinessException("Could not retrive tactical optimization result");
			}
		}
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
		return tacticalOptimizationResult.getRoutes().stream().mapToDouble(d -> d.getLeadTime()).max().orElse(0);
	}
	
	public int getMaxPrintersByRemote() {
		return new BigDecimal(tacticalOptimizationResult.getPrinters().stream().mapToInt(d -> d.getNumberOfSRAMs()).max().orElse(0)).setScale(0,RoundingMode.HALF_UP).intValue();
	}

	public double getExpensiveRoute() {
		return new BigDecimal(tacticalOptimizationResult.getRoutes().stream().mapToDouble(d -> d.getCost().doubleValue()).max().orElse(0)).setScale(2,RoundingMode.HALF_UP).doubleValue();
	}
	
	
}