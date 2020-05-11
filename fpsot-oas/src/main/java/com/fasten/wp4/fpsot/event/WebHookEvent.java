package com.fasten.wp4.fpsot.event;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.GsonBuilder;

public class WebHookEvent implements Serializable {

	public static final String OPERATIONAL_OPTIMIZATION_FINISHED = "operational_optimization_finished";

	private String event;
	private Map<String, Object> payload;

	public WebHookEvent(String name, Map<String, Object> payload) {
		this.event = name;
		this.payload = payload;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Map<String, Object> getPayload() {
		return payload;
	}

	public void setPayload(Map<String, Object> payload) {
		this.payload = payload;
	}

	public String toJson() {
		Map<String, Object> jsonObject = new HashMap<String, Object>();
		jsonObject.put("event", this.getEvent());
		jsonObject.put("payload", this.getPayload());
		return new GsonBuilder().create().toJson(jsonObject);
	}

}
