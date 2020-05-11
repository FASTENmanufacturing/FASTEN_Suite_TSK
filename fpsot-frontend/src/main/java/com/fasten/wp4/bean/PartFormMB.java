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
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

import com.fasten.wp4.database.client.api.PartControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.Part;
import com.github.adminfaces.template.exception.BusinessException;

@Named
@ViewScoped
public class PartFormMB implements Serializable {

    private Long id;
    private Part part;

    @Inject
	private transient PartControllerApi partControllerApi;

    public void init() {
        if(Faces.isAjaxRequest()){
           return;
        }
        if (has(id)) {
            try {
				part = partControllerApi.retrievePart(id);
			} catch (ApiException e) {
				throw new BusinessException("Could not retrive part");
			}
        } else {
            part = new Part();
        }
    }

    public Long getId() {
    	return id;
    }

    public void setId(Long id) {
    	this.id = id;
    }


    public Part getPart() {
    	return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }


    public void remove() throws IOException {
        if (has(part) && has(part.getId())) {
            try {
				partControllerApi.deletePart(part.getId());
			} catch (ApiException e) {
				throw new BusinessException("Could not delete part");
			}
            addDetailMessage("Part " + part.getName() + " removed successfully");
            Faces.getFlash().setKeepMessages(true);
            Faces.redirect("part-list.xhtml");
        }
    }

    public void save() {
        String msg;
        if (part.getId() == null) {
            try {
				partControllerApi.createPart(part);
			} catch (ApiException e) {
				throw new BusinessException("Could not create part");
			}
            msg = "Part " + part.getName() + " created successfully";
        } else {
            try {
				partControllerApi.updatePart(part.getId(), part);
			} catch (ApiException e) {
				throw new BusinessException("Could not update part");
			}
            msg = "Part " + part.getName() + " updated successfully";
        }
        addDetailMessage(msg);
    }

    public void clear() {
        part = new Part();
        id = null;
    }

    public boolean isNew() {
        return part == null || part.getId() == null;
    }
    
    public String getBreadcum() {
    	FacesContext context = FacesContext.getCurrentInstance();
        ResourceBundle admin = context.getApplication().evaluateExpressionGet(context, "#{adm}", ResourceBundle.class);
        String entityName = admin.getString("part");
    	String newExpression = admin.getString("breadcum.new");
    	if(part.getId() == null) {
    		return MessageFormat.format(newExpression, entityName);
    	}else {
    		return StringUtils.capitalize(entityName).concat(" "+ part.getId());
    	}
    }

}
