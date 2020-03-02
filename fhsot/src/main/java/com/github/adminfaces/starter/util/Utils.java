package com.github.adminfaces.starter.util;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;

import org.omnifaces.util.Messages;

@Named
@ApplicationScoped
@SuppressWarnings("serial")
public class Utils implements Serializable {

    public static void addDetailMessage(String message) {
        addDetailMessage(message, null);
    }

    public static void addGrowlMessage(String title, String message) {
        addGrowlMessage(title, message, null);
    }

    
    public static void addGrowlMessage(String title, String message, FacesMessage.Severity severity) {
    	FacesMessage facesMessage = Messages.create("").detail(message).get();
    	facesMessage.setSummary(title);
        if (severity != null && severity != FacesMessage.SEVERITY_INFO) {
            facesMessage.setSeverity(severity);
        }
    	Messages.add("asyncNotify", facesMessage);
	}

	public static void addDetailMessage(String message, FacesMessage.Severity severity) {
        FacesMessage facesMessage = Messages.create("").detail(message).get();
        if (severity != null && severity != FacesMessage.SEVERITY_INFO) {
            facesMessage.setSeverity(severity);
            Messages.add("messages", facesMessage);
        }else {
        	Messages.add("info-messages", facesMessage);
        }
    }
}
