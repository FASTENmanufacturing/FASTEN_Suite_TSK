package com.fasten.wp4.iot.kafka.request;

import java.io.Serializable;
import java.util.Date;

public class ProbabilityDistribuitionsRequest implements Serializable {

	private Date requestedTime = new Date();

	public Date getRequestedTime() {
		return requestedTime;
	}

	public void setRequestedTime(Date requestedTime) {
		this.requestedTime = requestedTime;
	}

}
