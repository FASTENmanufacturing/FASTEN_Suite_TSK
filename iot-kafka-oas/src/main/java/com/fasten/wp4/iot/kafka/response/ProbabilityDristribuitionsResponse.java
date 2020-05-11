package com.fasten.wp4.iot.kafka.response;

import java.io.Serializable;
import java.util.List;

import com.fasten.wp4.database.client.model.ProcessingPart;


public class ProbabilityDristribuitionsResponse implements Serializable {

	private List<ProcessingPart> processingParts;

	public List<ProcessingPart> getProcessingParts() {
		return processingParts;
	}

	public void setProcessingParts(List<ProcessingPart> processingParts) {
		this.processingParts = processingParts;
	}

}
