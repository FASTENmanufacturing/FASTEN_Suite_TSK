package com.github.adminfaces.starter.bean;

import static com.github.adminfaces.starter.util.Utils.addGrowlMessage;

import javax.enterprise.context.ApplicationScoped;
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
    	
    	addGrowlMessage(title, message);
	}
	
	
}
