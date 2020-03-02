package com.fasten.wp4.optimizator.tactical.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(description = "Operational optimization result")
public class OperationalOptimizationResult implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private OperationalOptimization study;

	@ManyToOne
	private RemoteStation remoteStation;

	private BigDecimal queueTime;

	private BigDecimal timeProduction;

	private BigDecimal timeTravel;

	private BigDecimal totalTime;

	public OperationalOptimizationResult() {
		super();
	}

	public OperationalOptimizationResult(Long id, OperationalOptimization study, RemoteStation remoteStation,
			BigDecimal queueTime, BigDecimal timeProduction, BigDecimal timeTravel, BigDecimal totalTime) {
		super();
		this.id = id;
		this.study = study;
		this.remoteStation = remoteStation;
		this.queueTime = queueTime;
		this.timeProduction = timeProduction;
		this.timeTravel = timeTravel;
		this.totalTime = totalTime;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public OperationalOptimization getStudy() {
		return study;
	}

	public void setStudy(OperationalOptimization study) {
		this.study = study;
	}

	public RemoteStation getRemoteStation() {
		return remoteStation;
	}

	public void setRemoteStation(RemoteStation remoteStation) {
		this.remoteStation = remoteStation;
	}

	public BigDecimal getQueueTime() {
		return queueTime;
	}

	public void setQueueTime(BigDecimal queueTime) {
		this.queueTime = queueTime;
	}

	public BigDecimal getTimeProduction() {
		return timeProduction;
	}

	public void setTimeProduction(BigDecimal timeProduction) {
		this.timeProduction = timeProduction;
	}

	public BigDecimal getTimeTravel() {
		return timeTravel;
	}

	public void setTimeTravel(BigDecimal timeTravel) {
		this.timeTravel = timeTravel;
	}

	public BigDecimal getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(BigDecimal totalTime) {
		this.totalTime = totalTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OperationalOptimizationResult other = (OperationalOptimizationResult) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OperationalOptimizationResult [id=" + id + ", study=" + study + ", remoteStation=" + remoteStation
				+ ", queueTime=" + queueTime + ", timeProduction=" + timeProduction + ", timeTravel=" + timeTravel
				+ ", totalTime=" + totalTime + "]";
	}

}
