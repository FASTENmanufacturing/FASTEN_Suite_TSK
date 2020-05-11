package com.fasten.wp4.bean;

import static com.fasten.wp4.util.Utils.addDetailMessage;
import static com.github.adminfaces.template.util.Assert.has;

import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Faces;

import com.fasten.wp4.database.client.api.CityControllerApi;
import com.fasten.wp4.database.client.api.OperationalOptimizationControllerApi;
import com.fasten.wp4.database.client.api.PartControllerApi;
import com.fasten.wp4.database.client.api.RemoteStationControllerApi;
import com.fasten.wp4.database.client.api.StateControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.City;
import com.fasten.wp4.database.client.model.OperationalOptimization;
import com.fasten.wp4.database.client.model.Part;
import com.fasten.wp4.database.client.model.State;
import com.fasten.wp4.fpsot.client.api.HsoControllerApi;
import com.github.adminfaces.template.exception.BusinessException;

@Named
@ViewScoped
public class OperationalOptimizationFormMB implements Serializable {

	private Serializable id;

	List<Part> parts;
	
	List<State> states;
	
	List<City> cities;

	private OperationalOptimization entity = new OperationalOptimization();

	@Inject
	private transient OperationalOptimizationControllerApi operationalOptimizationControllerApi;

	@Inject
	private transient PartControllerApi partControllerApi;

	@Inject
	private transient StateControllerApi stateControllerApi;
	
	@Inject
	private transient CityControllerApi cityControllerApi;
	
	@Inject
	private transient HsoControllerApi hsoControllerApi;

	public void init() {
		
		if(Faces.isAjaxRequest()){
			return;
		}
		
		entity.setQuantity(1);
		
		if (has(id) && !"".equals(id)) {
			try {
				entity = operationalOptimizationControllerApi.retrieveOperationalOptimization(Long.valueOf(id.toString()));
			} catch (ApiException e) {
				clear();
				throw new BusinessException(String.format("Could not retrive operational optimization with id %s, a new one will be initialized.", id));
			}
			populateSelectPart();
			populateSelectState();
			populateSelectCity();
		}
		
	}
	
	public void clear() {
		entity = new OperationalOptimization();
		id = null;
	}
	
	public boolean isNew() {
		return entity == null || entity.getId() == null;
	}

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
	}

	public OperationalOptimization getEntity() {
		return entity;
	}

	public void setEntity(OperationalOptimization entity) {
		this.entity = entity;
	}

	public void execute() {
		if (isNew()) {
			try {
				hsoControllerApi.executeOperationalOptimization(false,UUID.randomUUID().toString(),entity.getAddress().getCity().getName(), entity.getPart().getName(), entity.getQuantity()+"");
			} catch (com.fasten.wp4.fpsot.client.invoker.ApiException e) {
				throw new BusinessException("Could not create and execute operational optimization study");
			}
			addDetailMessage("Operational Optimization study created and executed successfully");
		} 
	}

	public void save() {
		if (isNew()) {
			try {
				operationalOptimizationControllerApi.createOperationalOptimization(entity);
			} catch (ApiException e) {
				throw new BusinessException("Could not create operational optimization study");
			}
			addDetailMessage("Operational Optimization study created successfully");
		} else {
			try {
				operationalOptimizationControllerApi.updateOperationalOptimization(entity.getId(),entity);
			} catch (ApiException e) {
				throw new BusinessException("Could not update operational optimization study");
			}
			addDetailMessage("Operational optimization study updated successfully");
		}
	}

	public void remove() throws IOException {
		if (has(entity) && has(entity.getId())) {
			try {
				operationalOptimizationControllerApi.deleteOperationalOptimization(entity.getId());
				addDetailMessage("Operational ptimization study removed successfully");
				Faces.getFlash().setKeepMessages(true);
				Faces.redirect("operational-optimization-list.xhtml");
			} catch (ApiException e) {
				throw new BusinessException("Could not delete operational optimization study");
			}
		}else {
			throw new BusinessException("Cannot delete operational optimization study without id.");
		}
	}

	public String getBreadcum() {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle admin = context.getApplication().evaluateExpressionGet(context, "#{adm}", ResourceBundle.class);
		String entityName = "Operational Optimization";//admin.getString("prediction");
		String newExpression = admin.getString("breadcum.new");
		if(entity.getId() == null) {
			return MessageFormat.format(newExpression, entityName);
		}else {
			return StringUtils.capitalize(entityName).concat(" "+entity.getId());
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

	public void populateSelectState() {
		try {
			states= stateControllerApi.retrieveAllState();
		} catch (ApiException e) {
			throw new BusinessException("Could not list states");
		}finally{
		}
	}
	
	public void populateSelectCity() {
		if(has(entity.getAddress().getState())) {
			try {
				cities= cityControllerApi.retrivedByState(entity.getAddress().getState().getId());
			} catch (ApiException e) {
				throw new BusinessException("Could not list cities by state");
			}finally{
			}
		}else {
			throw new BusinessException("Must select a state");
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
		}else if(cities.isEmpty() && has(entity.getAddress().getState())) {
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
