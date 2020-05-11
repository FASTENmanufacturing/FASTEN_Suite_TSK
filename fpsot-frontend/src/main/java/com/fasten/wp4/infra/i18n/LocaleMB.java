package com.fasten.wp4.infra.i18n;

import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

@Named
@SessionScoped
public class LocaleMB implements Serializable {
	
	private final Locale ENGLISH = Locale.ENGLISH;
	private final Locale PORTUGUESE = new Locale("pt", "BR");
	private Locale locale;
	
	@PostConstruct
    public void init() {
        locale = FacesContext.getCurrentInstance().getApplication().getDefaultLocale();
    }

	public Locale getLocale() {
		return (locale);
	}

	public void setEnglish(ActionEvent event) {
		locale = ENGLISH;
		updateViewLocale();
	}

	public void setPortuguese(ActionEvent event) {
		locale = PORTUGUESE;
		updateViewLocale();
	}

	public void updateViewLocale() {
		try {
			FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
			FacesContext.getCurrentInstance().getExternalContext().redirect("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}