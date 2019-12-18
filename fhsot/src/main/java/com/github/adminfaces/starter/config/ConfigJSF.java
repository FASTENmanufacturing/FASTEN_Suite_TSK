package com.github.adminfaces.starter.config;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.FacesConfig;
import javax.faces.annotation.FacesConfig.Version;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@ApplicationScoped
@FacesConfig(version=Version.JSF_2_3)
public class ConfigJSF {
//	https://github.com/javaserverfaces/mojarra
	@Inject 
	FacesContext facescontext;
}
