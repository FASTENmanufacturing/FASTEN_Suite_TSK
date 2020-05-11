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

import com.fasten.wp4.database.client.api.DeliveryControllerApi;
import com.fasten.wp4.database.client.api.RemoteStationControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.Delivery;
import com.fasten.wp4.database.client.model.RemoteStation;
import com.github.adminfaces.template.exception.BusinessException;

@Named
@ViewScoped
public class DeliveryFormMB implements Serializable {

    private Long id;
    
    private Delivery delivery;
    
	List<RemoteStation> remoteStations;

    @Inject
	private transient DeliveryControllerApi deliveryControllerApi;
    
	@Inject
	private transient RemoteStationControllerApi remoteStationControllerApi;

    public void init() {
        if(Faces.isAjaxRequest()){
           return;
        }
        if (has(id)) {
            try {
				delivery = deliveryControllerApi.retrieveDelivery(id);
			} catch (ApiException e) {
				throw new BusinessException("Could not retrive delivery");
			}
            
            populateSelectRemoteStation();
            
        } else {
            delivery = new Delivery();
        }
        
    }

    public Long getId() {
    	return id;
    }

    public void setId(Long id) {
    	this.id = id;
    }


    public Delivery getDelivery() {
    	return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }


    public void remove() throws IOException {
        if (has(delivery) && has(delivery.getId())) {
            try {
				deliveryControllerApi.deleteDelivery(delivery.getId());
			} catch (ApiException e) {
				throw new BusinessException("Could not delete delivery");
			}
            addDetailMessage("Delivery " + delivery.getOriginCep() + " removed successfully");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("delivery-list.xhtml");
        }
    }

    public void save() {
        String msg;
        if (delivery.getId() == null) {
            try {
				deliveryControllerApi.createDelivery(delivery);
			} catch (ApiException e) {
				throw new BusinessException("Could not create delivery");
			}
            msg = "Delivery " + delivery.getOriginCep() + " created successfully";
        } else {
            try {
				deliveryControllerApi.updateDelivery(delivery.getId(), delivery);
			} catch (ApiException e) {
				throw new BusinessException("Could not update delivery");
			}
            msg = "Delivery " + delivery.getOriginCep() + " updated successfully";
        }
        addDetailMessage(msg);
    }

    public void clear() {
        delivery = new Delivery();
        id = null;
    }

    public boolean isNew() {
        return delivery == null || delivery.getId() == null;
    }
    
    public String getBreadcum() {
    	FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle admin = context.getApplication().evaluateExpressionGet(context, "#{adm}", ResourceBundle.class);
        String entityName = admin.getString("delivery");
    	String newExpression = admin.getString("breadcum.new");
    	if(delivery.getId() == null) {
    		return MessageFormat.format(newExpression, entityName);
    	}else {
    		return StringUtils.capitalize(entityName).concat(" "+ delivery.getId());
    	}
    } 

	public void populateSelectRemoteStation() {
		try {
			remoteStations= remoteStationControllerApi.retrieveAllRemoteStation();
		} catch (ApiException e) {
			throw new BusinessException("Could not retrive list");
		}finally{
		}
	}

	public List<RemoteStation> getRemoteStations() {
		if(remoteStations==null) {
			remoteStations=new ArrayList<RemoteStation>();
			return remoteStations;
		}else if(remoteStations.isEmpty()) {
			populateSelectRemoteStation();
		}
		return remoteStations;
	}

	public void setRemoteStations(List<RemoteStation> remoteStations) {
		this.remoteStations = remoteStations;
	}
	
}
