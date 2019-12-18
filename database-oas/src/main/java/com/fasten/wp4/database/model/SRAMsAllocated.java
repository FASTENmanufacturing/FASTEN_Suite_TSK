package com.fasten.wp4.database.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@ApiModel(description = "The output aumont of SRAMs for each remote station")
public class SRAMsAllocated implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty(notes = "The remote station producing")
	@ManyToOne
	private RemoteStation remoteStation;

	@ApiModelProperty(notes = "SRAMs allocated into a Remote Station")
	@Column(name="number_of_SRAMs")
	private Integer numberOfSRAMs;
	
	@ManyToOne
	@JoinColumn(name = "tactical_optimization_result_id", referencedColumnName = "id")
	@JsonIgnore
	private TacticalOptimizationResult tacticalOptimizationResult;
	
	
	public SRAMsAllocated() {
	}

	public SRAMsAllocated(Long id, RemoteStation remoteStation, Integer numberOfSRAMs,
			TacticalOptimizationResult tacticalOptimizationResult) {
		super();
		this.id = id;
		this.remoteStation = remoteStation;
		this.numberOfSRAMs = numberOfSRAMs;
		this.tacticalOptimizationResult = tacticalOptimizationResult;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RemoteStation getRemoteStation() {
		return remoteStation;
	}

	public void setRemoteStation(RemoteStation remoteStation) {
		this.remoteStation = remoteStation;
	}

	public Integer getNumberOfSRAMs() {
		return numberOfSRAMs;
	}

	public void setNumberOfSRAMs(Integer numberOfSRAMs) {
		this.numberOfSRAMs = numberOfSRAMs;
	}

	public TacticalOptimizationResult getTacticalOptimizationResult() {
		return tacticalOptimizationResult;
	}

	public void setTacticalOptimizationResult(TacticalOptimizationResult tacticalOptimizationResult) {
		this.tacticalOptimizationResult = tacticalOptimizationResult;
	}
	
	@Override
	public String toString() {
		return "SRAMsAllocated [id=" + id + ", remoteStation=" + remoteStation + ", numberOfSRAMs=" + numberOfSRAMs
				+ ", tacticalOptimizationResult=" + tacticalOptimizationResult + "]";
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
		SRAMsAllocated other = (SRAMsAllocated) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
