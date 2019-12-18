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
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

import com.fasten.wp4.database.client.api.SramControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.SRAM;
import com.fasten.wp4.database.client.model.SRAMCapabilities;
import com.fasten.wp4.database.client.model.SRAMEnviromentalInfo;
import com.github.adminfaces.template.exception.BusinessException;

@Named
@ViewScoped
public class SRAMFormMB implements Serializable {

    private Long id;
    private SRAM sRAM;

    @Inject
	private transient SramControllerApi sRAMControllerApi;

    public void init() {
        if(Faces.isAjaxRequest()){
           return;
        }
        if (has(id)) {
            try {
				sRAM = sRAMControllerApi.retrieveSRAM(id);
			} catch (ApiException e) {
				throw new BusinessException("Could not retrive sRAM");
			}
        } else {
            sRAM = new SRAM();
            sRAM.setEnviromentalInfo(new SRAMEnviromentalInfo());
            sRAM.setCapabilities(new SRAMCapabilities());
        }
    }

    public Long getId() {
    	return id;
    }

    public void setId(Long id) {
    	this.id = id;
    }


    public SRAM getSRAM() {
    	return sRAM;
    }

    public void setSRAM(SRAM sRAM) {
        this.sRAM = sRAM;
    }


    public void remove() throws IOException {
        if (has(sRAM) && has(sRAM.getId())) {
            try {
				sRAMControllerApi.deleteSRAM(sRAM.getId());
			} catch (ApiException e) {
				throw new BusinessException("Could not delete sRAM");
			}
            addDetailMessage("SRAM " + sRAM.getCode() + " removed successfully");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("sRAM-list.xhtml");
        }
    }

    public void save() {
        String msg;
        if (sRAM.getId() == null) {
            try {
				sRAMControllerApi.createSRAM(sRAM);
			} catch (ApiException e) {
				throw new BusinessException("Could not create sRAM");
			}
            msg = "SRAM " + sRAM.getCode() + " created successfully";
        } else {
            try {
				sRAMControllerApi.updateSRAM(sRAM.getId(), sRAM);
			} catch (ApiException e) {
				throw new BusinessException("Could not update sRAM");
			}
            msg = "SRAM " + sRAM.getCode() + " updated successfully";
        }
        addDetailMessage(msg);
    }

    public void clear() {
        sRAM = new SRAM();
        id = null;
    }

    public boolean isNew() {
        return sRAM == null || sRAM.getId() == null;
    }
    
    public String getBreadcum() {
    	FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle admin = context.getApplication().evaluateExpressionGet(context, "#{adm}", ResourceBundle.class);
        String entityName = admin.getString("sram");
    	String newExpression = admin.getString("breadcum.new");
    	if(sRAM.getId() == null) {
    		return MessageFormat.format(newExpression, entityName);
    	}else {
    		return StringUtils.capitalize(entityName).concat(" "+ sRAM.getId());
    	}
    }

	public SRAM getsRAM() {
		return sRAM;
	}

	public SramControllerApi getsRAMControllerApi() {
		return sRAMControllerApi;
	}

}
