package com.fasten.wp4.fpsot.listner;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.fasten.wp4.database.client.api.WebHookControllerApi;
import com.fasten.wp4.database.client.invoker.ApiException;
import com.fasten.wp4.database.client.model.WebHook;
import com.fasten.wp4.fpsot.event.WebHookEvent;
import com.fasten.wp4.fpsot.retryable.WebHookNotify;

@Component
public class WebHookEventListner {

	private static final Logger logger = LoggerFactory.getLogger(WebHookEventListner.class);

	@Autowired
	WebHookControllerApi webHookControllerApi;
	
	@Autowired
	WebHookNotify webHookNotify;

	@EventListener
	public void handleEvent(WebHookEvent webHookEvent) {

		List<WebHook> webHooksByEvent = new ArrayList<WebHook>();

		try {
			webHooksByEvent = webHookControllerApi.retrieveWebHooksByEvent(webHookEvent.getEvent());
		} catch (ApiException e) {
			if(e.getCode()!=404) {
				e.printStackTrace();
			}
		}

		for (WebHook webHook : webHooksByEvent) {
			webHookNotify.notifyConsumerService(webHook.getUrl(), webHookEvent.toJson());
		}
	}


}
