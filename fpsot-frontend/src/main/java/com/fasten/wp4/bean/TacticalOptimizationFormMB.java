package com.fasten.wp4.bean;

import static com.fasten.wp4.util.ApiUtils.getIdFromLocationHeader;
import static com.fasten.wp4.util.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Faces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.SlideEndEvent;

import com.fasten.wp4.database.client.api.DemandControllerApi;
import com.fasten.wp4.database.client.api.TacticalOptimizationControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.TacticalOptimization;
import com.github.adminfaces.template.exception.BusinessException;

@Named
@ViewScoped
public class TacticalOptimizationFormMB implements Serializable {

	private Long id;

	private TacticalOptimization tacticalOptimization;

	@Inject
	private transient DemandControllerApi demandControllerApi;

    @Inject
	private transient TacticalOptimizationControllerApi tacticalOptimizationControllerApi;

	public void init() {
		if(Faces.isAjaxRequest()){
			return;
		}
		if (has(id)) {
        	try {
				tacticalOptimization = tacticalOptimizationControllerApi.retrieveTacticalOptimization(id);
			} catch (ApiException e) {
				throw new BusinessException("Could not retrive tactical optimization study");
			}
		} else {
			tacticalOptimization = new TacticalOptimization();
			tacticalOptimization.setStatus(TacticalOptimization.StatusEnum.INVALID);
		}
		calculateTotalCandidates();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TacticalOptimization getTacticalOptimization() {
		return tacticalOptimization;
	}
	public void setTacticalOptimization(TacticalOptimization tacticalOptimization) {
		this.tacticalOptimization = tacticalOptimization;
	}

	public void remove() throws IOException {
		if (has(tacticalOptimization) && has(tacticalOptimization.getId())) {
			try {
				tacticalOptimizationControllerApi.deleteTacticalOptimization(tacticalOptimization.getId());
			addDetailMessage("Tactical Optimization study removed successfully");
			Faces.getFlash().setKeepMessages(true);
			Faces.redirect("tactical-optimization-list.xhtml");
			} catch (ApiException e) {
				throw new BusinessException("Could not delete tactical optimization study");
			}
		}
	}
	
	public void save() {
		String msg;
		if (tacticalOptimization.getId() == null) {
			try {
				tacticalOptimization.setId(getIdFromLocationHeader(tacticalOptimizationControllerApi.createTacticalOptimizationWithHttpInfo(tacticalOptimization)));
			} catch (ApiException e) {
				throw new BusinessException("Could not create tactical optimization study");
			} catch (URISyntaxException e1) {
				try {
					tacticalOptimization = tacticalOptimizationControllerApi.retrieveTacticalOptimization(tacticalOptimization.getId());
				} catch (ApiException e) {
					addDetailMessage("Tactical Optimization study saved successfully");
					Faces.getFlash().setKeepMessages(true);
					Faces.redirect("tactical-optimization-list.xhtml");
				}
			}
			msg = "Tactical Optimization study created successfully";
		} else {
			try {
				tacticalOptimizationControllerApi.updateTacticalOptimization(tacticalOptimization.getId(),tacticalOptimization);
			} catch (ApiException e) {
				throw new BusinessException("Could not update tactical optimization study");
			}
			msg = "Tactical Optimization study updated successfully";
		}
		addDetailMessage(msg);
	}

	public void clear() {
		tacticalOptimization = new TacticalOptimization();
		id = null;
		candidates=null;
	}

	public boolean isNew() {
		return tacticalOptimization == null || tacticalOptimization.getId() == null;
	}

	public String getBreadcum() {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle admin = context.getApplication().evaluateExpressionGet(context, "#{adm}", ResourceBundle.class);
		String entityName = "Tactical Optimization";//admin.getString("tacticalOptimization");
		String newExpression = admin.getString("breadcum.new");
		if(tacticalOptimization.getId() == null) {
			return MessageFormat.format(newExpression, entityName);
		}else {
			return StringUtils.capitalize(entityName).concat(" "+tacticalOptimization.getId());
		}
	}

	public void onDateSelect(SelectEvent selectEvent) {

		calculateTotalCandidates();
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Total candidate locations: ", candidates.toString()));

	}
	
	public void calculateTotalCandidates() {

		if(has(tacticalOptimization.getInitialDate()) && has(tacticalOptimization.getEndDate())){
//			FacesContext context = FacesContext.getCurrentInstance();
//			ResourceBundle admin = context.getApplication().evaluateExpressionGet(context, "#{adm}", ResourceBundle.class);
//			String dateFormat = admin.getString("admin.dateFormat");
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			String endDate=format.format(tacticalOptimization.getEndDate());
			String startDate = format.format(tacticalOptimization.getInitialDate());
			try {
				candidates=0;
				candidates = demandControllerApi.retrieveCandidates(endDate,startDate);
			} catch (ApiException e) {
				throw new BusinessException("Could not retrive candidates");
			}finally {
			}
		}else {
			candidates=0;
		}
	}
	
	public void onSlideEnd(SlideEndEvent slideEndEvent) {
		this.tacticalOptimization.setDistanceWeight(new BigDecimal(100d).subtract(new BigDecimal(slideEndEvent.getValue())));
	}
	
	public void selectOneType() {
		if(has(tacticalOptimization.getType()) && tacticalOptimization.getType().equals(TacticalOptimization.TypeEnum.COST_BENEFIT)) {
			tacticalOptimization.leadTimeLimit(null);
		}
	}
	
	//wrapper para o boolean do client que possui erro de geração: Boolean com get "is..." 
	public boolean isClustered() {
		return (tacticalOptimization.isClustered()!=null)?tacticalOptimization.isClustered():false;
	}

	public void setClustered(boolean clustered) {
		tacticalOptimization.setClustered(clustered);
	}

	public void toogleCluster() {
		if(!tacticalOptimization.isClustered()) {
			tacticalOptimization.setMaximumLocations(null);
			tacticalOptimization.setDistanceWeight(null);
			tacticalOptimization.setTimeWeight(null);
		}
	}

	public void toogleUsePrediction() {
		if(!tacticalOptimization.isUsePrediction()) {
			tacticalOptimization.setHorizon(null);
			tacticalOptimization.setGranularity(null);
		}
	}
	
	public boolean isUsePrediction() {
		return (tacticalOptimization.isUsePrediction()!=null?tacticalOptimization.isUsePrediction():false);
	}

	public void setUsePrediction(boolean usePrediction) {
		tacticalOptimization.setUsePrediction(usePrediction);
	}
	
	private Integer candidates;

	public Integer getCandidates() {
		return candidates;
	}

	public void setCandidates(Integer candidates) {
		this.candidates = candidates;
	}
	
	private Integer slider;

	public Integer getSlider() {
		return slider;
	}

	public void setSlider(Integer slider) {
		this.slider = slider;
	}
}
