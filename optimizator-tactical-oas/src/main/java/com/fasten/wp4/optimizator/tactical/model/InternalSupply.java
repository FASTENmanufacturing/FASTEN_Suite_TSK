package com.fasten.wp4.optimizator.tactical.model;

import java.io.Serializable;

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
@ApiModel(description = "Result route output")
public class InternalSupply implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty(notes = "The remote station producing the part")
	@ManyToOne
	private RemoteStation remoteStationOrigin;

	@ApiModelProperty(notes = "The remote station receiving the part")
	@ManyToOne
	private RemoteStation remoteStationDestination;

	@ApiModelProperty(notes = "The part")
	@ManyToOne
	private Part part;
	
	@ManyToOne
	@JoinColumn(name = "tactical_optimization_result_id", referencedColumnName = "id")
	@JsonIgnore
	private TacticalOptimizationResult tacticalOptimizationResult;

	public InternalSupply() {
		super();
	}

	public InternalSupply(Long id, RemoteStation remoteStationOrigin, RemoteStation remoteStationDestination, Part part,
			TacticalOptimizationResult tacticalOptimizationResult) {
		super();
		this.id = id;
		this.remoteStationOrigin = remoteStationOrigin;
		this.remoteStationDestination = remoteStationDestination;
		this.part = part;
		this.tacticalOptimizationResult = tacticalOptimizationResult;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RemoteStation getRemoteStationOrigin() {
		return remoteStationOrigin;
	}

	public void setRemoteStationOrigin(RemoteStation remoteStationOrigin) {
		this.remoteStationOrigin = remoteStationOrigin;
	}

	public RemoteStation getRemoteStationDestination() {
		return remoteStationDestination;
	}

	public void setRemoteStationDestination(RemoteStation remoteStationDestination) {
		this.remoteStationDestination = remoteStationDestination;
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public TacticalOptimizationResult getTacticalOptimizationResult() {
		return tacticalOptimizationResult;
	}

	public void setTacticalOptimizationResult(TacticalOptimizationResult tacticalOptimizationResult) {
		this.tacticalOptimizationResult = tacticalOptimizationResult;
	}

	@Override
	public String toString() {
		return "InternalSupply [id=" + id + ", remoteStationOrigin=" + remoteStationOrigin
				+ ", remoteStationDestination=" + remoteStationDestination + ", part=" + part
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
		InternalSupply other = (InternalSupply) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}