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

import com.fasten.wp4.database.client.api.CityControllerApi;
import com.fasten.wp4.database.client.api.DistributionCenterControllerApi;
import com.fasten.wp4.database.client.api.StateControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.City;
import com.fasten.wp4.database.client.model.DistributionCenter;
import com.fasten.wp4.database.client.model.State;
import com.github.adminfaces.template.exception.BusinessException;

@Named
@ViewScoped
public class DistributionCenterFormMB implements Serializable {

    private Long id;
    private DistributionCenter distributionCenter;
    
    List<State> states;
	
	List<City> cities;

    @Inject
	private transient DistributionCenterControllerApi distributionCenterControllerApi;
    
    @Inject
	private transient StateControllerApi stateControllerApi;
	
	@Inject
	private transient CityControllerApi cityControllerApi;

    public void init() {
        if(Faces.isAjaxRequest()){
           return;
        }
        if (has(id)) {
            try {
				distributionCenter = distributionCenterControllerApi.retrieveDistributionCenter(id);
			} catch (ApiException e) {
				throw new BusinessException("Could not retrive distributionCenter");
			}
            populateSelectState();
			populateSelectCity();
        } else {
            distributionCenter = new DistributionCenter();
        }
    }

    public Long getId() {
    	return id;
    }

    public void setId(Long id) {
    	this.id = id;
    }


    public DistributionCenter getDistributionCenter() {
    	return distributionCenter;
    }

    public void setDistributionCenter(DistributionCenter distributionCenter) {
        this.distributionCenter = distributionCenter;
    }


    public void remove() throws IOException {
        if (has(distributionCenter) && has(distributionCenter.getId())) {
            try {
				distributionCenterControllerApi.deleteDistributionCenter(distributionCenter.getId());
			} catch (ApiException e) {
				throw new BusinessException("Could not delete distribution center");
			}
            addDetailMessage("DistributionCenter " + distributionCenter.getCode() + " removed successfully");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("distributionCenter-list.xhtml");
        }
    }

    public void save() {
        String msg;
        if (distributionCenter.getId() == null) {
            try {
				distributionCenterControllerApi.createDistributionCenter(distributionCenter);
			} catch (ApiException e) {
				throw new BusinessException("Could not create distributionCenter");
			}
            msg = "Distribution Center " + distributionCenter.getCode() + " created successfully";
        } else {
            try {
				distributionCenterControllerApi.updateDistributionCenter(distributionCenter.getId(), distributionCenter);
			} catch (ApiException e) {
				throw new BusinessException("Could not update distributionCenter");
			}
            msg = "Distribution Center " + distributionCenter.getCode() + " updated successfully";
        }
        addDetailMessage(msg);
    }

    public void clear() {
        distributionCenter = new DistributionCenter();
        id = null;
    }

    public boolean isNew() {
        return distributionCenter == null || distributionCenter.getId() == null;
    }
    
    public String getBreadcum() {
    	FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle admin = context.getApplication().evaluateExpressionGet(context, "#{adm}", ResourceBundle.class);
        String distributionCenterName = admin.getString("distributionCenter");
    	String newExpression = admin.getString("breadcum.new");
    	if(distributionCenter.getId() == null) {
    		return MessageFormat.format(newExpression, distributionCenterName);
    	}else {
    		return StringUtils.capitalize(distributionCenterName).concat(" "+ distributionCenter.getId());
    	}
    }
    
    public void populateSelectState() {
		try {
			states= stateControllerApi.retrieveAllState();
		} catch (ApiException e) {
			throw new BusinessException("Could not list states");
		}finally{
		}
	}
	
	public void populateSelectCity() {
		if(has(distributionCenter.getAddress().getState())) {
			try {
				cities= cityControllerApi.retrivedByState(distributionCenter.getAddress().getState().getId());
			} catch (ApiException e) {
				throw new BusinessException("Could not list cities by state");
			}finally{
			}
		}else {
			throw new BusinessException("Must select a state");
		}
	}
	
	public List<State> getStates() {
		if(states==null) {
			states=new ArrayList<State>();
			return states;
		}else if(states.isEmpty()) {
			populateSelectState();
		}
		return states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}

	public List<City> getCities() {
		if(cities==null) {
			cities=new ArrayList<City>();
			return cities;
		}else if(cities.isEmpty() && has(distributionCenter.getAddress().getState())) {
			populateSelectCity();
		}
		return cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}
	
	public void onStateSelect() {
		populateSelectCity();
	}

}
