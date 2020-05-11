package com.fasten.wp4.resteasy;

import java.util.HashSet;
import java.util.Set;

//import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

//@ApplicationPath("rest")
public class ApplicationPath extends Application {

	@Override
    public Set<Class<?>> getClasses() {
        HashSet<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(FrontendNotifyController.class);
        return classes;
    }
	
}
