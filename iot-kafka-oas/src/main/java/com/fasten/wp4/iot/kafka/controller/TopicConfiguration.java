package com.fasten.wp4.iot.kafka.controller;

import org.springframework.stereotype.Component;

@Component
public class TopicConfiguration {

	private boolean enableGetProbabilityDistribuitions = false;
	
	private boolean enabledReturnHistoricalDemand = false;

	public TopicConfiguration() {
		super();
	}

	public boolean isEnableGetProbabilityDistribuitions() {
		return enableGetProbabilityDistribuitions;
	}

	public void setEnableGetProbabilityDistribuitions(boolean enableGetProbabilityDistribuitions) {
		this.enableGetProbabilityDistribuitions = enableGetProbabilityDistribuitions;
	}

	public boolean isEnabledReturnHistoricalDemand() {
		return enabledReturnHistoricalDemand;
	}

	public void setEnabledReturnHistoricalDemand(boolean enabledReturnHistoricalDemand) {
		this.enabledReturnHistoricalDemand = enabledReturnHistoricalDemand;
	}

}
