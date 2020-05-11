package com.fasten.wp4.bean;

import static com.fasten.wp4.util.Utils.addGrowlMessage;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.push.Push;
import javax.faces.push.PushContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;

@ApplicationScoped
@Named
public class AsyncNotifyMB {


	@Inject @Push
	private PushContext asyncChanel;
	

	public void sendPushMessage(String socketMessage) {
	    asyncChanel.send(socketMessage);
	}

	public void updateAsyncMessages() {
		String socketMessage = Faces.getRequestParameter("socketMessage");

		FacesContext context = FacesContext.getCurrentInstance();
    	String title = context.getApplication().evaluateExpressionGet(context, "#{event['"+socketMessage+".title']}",String.class);
    	String message = context.getApplication().evaluateExpressionGet(context, "#{event['"+socketMessage+".message']}",String.class);
    	
    	if(socketMessage.contains("error")) {
    		addGrowlMessage(title, message,FacesMessage.SEVERITY_ERROR);
    	}else {
    		addGrowlMessage(title, message);
    	}
	}
	
	
}
