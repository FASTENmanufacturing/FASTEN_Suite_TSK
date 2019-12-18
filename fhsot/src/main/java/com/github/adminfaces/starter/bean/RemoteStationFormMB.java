/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.bean;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
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
import com.fasten.wp4.database.client.api.RemoteStationControllerApi;
import com.fasten.wp4.database.client.api.StateControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.City;
import com.fasten.wp4.database.client.model.RemoteStation;
import com.fasten.wp4.database.client.model.State;
import com.github.adminfaces.template.exception.BusinessException;

@Named
@ViewScoped
public class RemoteStationFormMB implements Serializable {

    private Long id;
    private RemoteStation remoteStation;
    
    List<State> states;
	
	List<City> cities;

    @Inject
	private transient RemoteStationControllerApi remoteStationControllerApi;
    
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
				remoteStation = remoteStationControllerApi.retrieveRemoteStation(id);
			} catch (ApiException e) {
				throw new BusinessException("Could not retrive remoteStation");
			}
            populateSelectState();
			populateSelectCity();
        } else {
            remoteStation = new RemoteStation();
        }
    }

    public Long getId() {
    	return id;
    }

    public void setId(Long id) {
    	this.id = id;
    }


    public RemoteStation getRemoteStation() {
    	return remoteStation;
    }

    public void setRemoteStation(RemoteStation remoteStation) {
        this.remoteStation = remoteStation;
    }


    public void remove() throws IOException {
        if (has(remoteStation) && has(remoteStation.getId())) {
            try {
				remoteStationControllerApi.deleteRemoteStation(remoteStation.getId());
			} catch (ApiException e) {
				throw new BusinessException("Could not delete remoteStation");
			}
            addDetailMessage("RemoteStation " + remoteStation.getCode() + " removed successfully");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("remoteStation-list.xhtml");
        }
    }

    public void save() {
        String msg;
        if (remoteStation.getId() == null) {
            try {
				remoteStationControllerApi.createRemoteStation(remoteStation);
			} catch (ApiException e) {
				throw new BusinessException("Could not create remoteStation");
			}
            msg = "RemoteStation " + remoteStation.getCode() + " created successfully";
        } else {
            try {
				remoteStationControllerApi.updateRemoteStation(remoteStation.getId(), remoteStation);
			} catch (ApiException e) {
				throw new BusinessException("Could not update remoteStation");
			}
            msg = "RemoteStation " + remoteStation.getCode() + " updated successfully";
        }
        addDetailMessage(msg);
    }

    public void clear() {
        remoteStation = new RemoteStation();
        id = null;
    }

    public boolean isNew() {
        return remoteStation == null || remoteStation.getId() == null;
    }
    
    public String getBreadcum() {
    	FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle admin = context.getApplication().evaluateExpressionGet(context, "#{adm}", ResourceBundle.class);
        String remoteStationName = admin.getString("remoteStation");
    	String newExpression = admin.getString("breadcum.new");
    	if(remoteStation.getId() == null) {
    		return MessageFormat.format(newExpression, remoteStationName);
    	}else {
    		return StringUtils.capitalize(remoteStationName).concat(" "+ remoteStation.getId());
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
		if(has(remoteStation.getState())) {
			try {
				cities= cityControllerApi.retrivedByState(remoteStation.getState().getId());
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
		}else if(cities.isEmpty() && has(remoteStation.getState())) {
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
