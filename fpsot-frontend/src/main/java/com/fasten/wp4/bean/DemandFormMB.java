/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fasten.wp4.bean;

import static com.fasten.wp4.util.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;

import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

import com.fasten.wp4.database.client.api.DemandControllerApi;
import com.fasten.wp4.database.client.api.PartControllerApi;
import com.fasten.wp4.database.client.api.DistributionCenterControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.Demand;
import com.fasten.wp4.database.client.model.Part;
import com.fasten.wp4.database.client.model.DistributionCenter;
import com.github.adminfaces.template.exception.BusinessException;

@Named
@ViewScoped
public class DemandFormMB implements Serializable {

    private Long id;
    
    private Demand demand;
    
    List<Part> parts;

	List<DistributionCenter> distributionCenters;

    @Inject
	private transient DemandControllerApi demandControllerApi;
    
    @Inject
	private transient PartControllerApi partControllerApi;

	@Inject
	private transient DistributionCenterControllerApi distributionCenterControllerApi;

    public void init() {
        if(Faces.isAjaxRequest()){
           return;
        }
        if (has(id)) {
            try {
				demand = demandControllerApi.retrieveDemand(id);
			} catch (ApiException e) {
				throw new BusinessException("Could not retrive demand");
			}
            
            populateSelectPart();
    		populateSelectDistributionCenter();
    		
        } else {
            demand = new Demand();
        }
        
    }

    public Long getId() {
    	return id;
    }

    public void setId(Long id) {
    	this.id = id;
    }


    public Demand getDemand() {
    	return demand;
    }

    public void setDemand(Demand demand) {
        this.demand = demand;
    }


    public void remove() throws IOException {
        if (has(demand) && has(demand.getId())) {
            try {
				demandControllerApi.deleteDemand(demand.getId());
			} catch (ApiException e) {
				throw new BusinessException("Could not delete demand");
			}
            addDetailMessage("Demand " + demand.getCode() + " removed successfully");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("demand-list.xhtml");
        }
    }

    public void save() {
        String msg;
        if (demand.getId() == null) {
            try {
				demandControllerApi.createDemand(demand);
			} catch (ApiException e) {
				throw new BusinessException("Could not create demand");
			}
            msg = "Demand " + demand.getCode() + " created successfully";
        } else {
            try {
				demandControllerApi.updateDemand(demand.getId(), demand);
			} catch (ApiException e) {
				throw new BusinessException("Could not update demand");
			}
            msg = "Demand " + demand.getCode() + " updated successfully";
        }
        addDetailMessage(msg);
    }

    public void clear() {
        demand = new Demand();
        id = null;
    }

    public boolean isNew() {
        return demand == null || demand.getId() == null;
    }
    
    public String getBreadcum() {
    	FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle admin = context.getApplication().evaluateExpressionGet(context, "#{adm}", ResourceBundle.class);
        String entityName = admin.getString("demand");
    	String newExpression = admin.getString("breadcum.new");
    	if(demand.getId() == null) {
    		return MessageFormat.format(newExpression, entityName);
    	}else {
    		return StringUtils.capitalize(entityName).concat(" "+ demand.getId());
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

	public void populateSelectDistributionCenter() {
		try {
			distributionCenters= distributionCenterControllerApi.retrieveAllDistributionCenter();
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

	public List<DistributionCenter> getDistributionCenters() {
		if(distributionCenters==null) {
			distributionCenters=new ArrayList<DistributionCenter>();
			return distributionCenters;
		}else if(distributionCenters.isEmpty()) {
			populateSelectDistributionCenter();
		}
		return distributionCenters;
	}

	public void setDistributionCenters(List<DistributionCenter> distributionCenters) {
		this.distributionCenters = distributionCenters;
	}
	
}
