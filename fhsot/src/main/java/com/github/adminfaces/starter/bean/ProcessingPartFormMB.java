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

import com.fasten.wp4.database.client.api.PartControllerApi;
import com.fasten.wp4.database.client.api.ProcessingPartControllerApi;
import com.fasten.wp4.database.client.api.SramControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.Part;
import com.fasten.wp4.database.client.model.ProcessingPart;
import com.fasten.wp4.database.client.model.SRAM;
import com.github.adminfaces.template.exception.BusinessException;

@Named
@ViewScoped
public class ProcessingPartFormMB implements Serializable {

    private Long id;
    
    private ProcessingPart processingPart;
    
    List<Part> parts;
    
    List<SRAM> SRAMs;

    @Inject
	private transient ProcessingPartControllerApi processingPartControllerApi;
    
    @Inject
	private transient PartControllerApi partControllerApi;
    
    @Inject
	private transient SramControllerApi sramControllerApi;


    public void init() {
        if(Faces.isAjaxRequest()){
           return;
        }
        if (has(id)) {
            try {
				processingPart = processingPartControllerApi.retrieveProcessingPart(id);
			} catch (ApiException e) {
				throw new BusinessException("Could not retrive processingPart");
			}
            populateSelectPart();
            populateSelectSRAM();
        } else {
            processingPart = new ProcessingPart();
        }
        
    }

    public Long getId() {
    	return id;
    }

    public void setId(Long id) {
    	this.id = id;
    }


    public ProcessingPart getProcessingPart() {
    	return processingPart;
    }

    public void setProcessingPart(ProcessingPart processingPart) {
        this.processingPart = processingPart;
    }


    public void remove() throws IOException {
        if (has(processingPart) && has(processingPart.getId())) {
            try {
				processingPartControllerApi.deleteProcessingPart(processingPart.getId());
			} catch (ApiException e) {
				throw new BusinessException("Could not delete processingPart");
			}
            addDetailMessage("ProcessingPart " + processingPart.getPart().getName() + " removed successfully");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("processingPart-list.xhtml");
        }
    }

    public void save() {
        String msg;
        if (processingPart.getId() == null) {
            try {
				processingPartControllerApi.createProcessingPart(processingPart);
			} catch (ApiException e) {
				throw new BusinessException("Could not create processingPart");
			}
            msg = "ProcessingPart " + processingPart.getPart().getName() + " created successfully";
        } else {
            try {
				processingPartControllerApi.updateProcessingPart(processingPart.getId(), processingPart);
			} catch (ApiException e) {
				throw new BusinessException("Could not update processingPart");
			}
            msg = "ProcessingPart " + processingPart.getPart().getName() + " updated successfully";
        }
        addDetailMessage(msg);
    }

    public void clear() {
        processingPart = new ProcessingPart();
        id = null;
    }

    public boolean isNew() {
        return processingPart == null || processingPart.getId() == null;
    }
    
    public String getBreadcum() {
    	FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle admin = context.getApplication().evaluateExpressionGet(context, "#{adm}", ResourceBundle.class);
        String entityName = admin.getString("processingPart");
    	String newExpression = admin.getString("breadcum.new");
    	if(processingPart.getId() == null) {
    		return MessageFormat.format(newExpression, entityName);
    	}else {
    		return StringUtils.capitalize(entityName).concat(" "+ processingPart.getId());
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

	public void populateSelectSRAM() {
		try {
			SRAMs= sramControllerApi.retrieveAllSRAM();
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

	public List<SRAM> getSRAMs() {
		if(SRAMs==null) {
			SRAMs=new ArrayList<SRAM>();
			return SRAMs;
		}else if(SRAMs.isEmpty()) {
			populateSelectSRAM();
		}
		return SRAMs;
	} 
	
}
