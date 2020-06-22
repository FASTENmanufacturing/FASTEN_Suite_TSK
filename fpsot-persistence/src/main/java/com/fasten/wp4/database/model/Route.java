package com.fasten.wp4.database.model;

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
public class Route implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty(notes = "The remote station producing the part")
	private String remoteStation;

	@ApiModelProperty(notes = "The distribution center receiving the part")
	private String distributionCenter;

	@ApiModelProperty(notes = "The part")
	private String part;

	@ApiModelProperty(notes = "The quantitiy of travels")
	private Integer timesTraveled;

	@ApiModelProperty(notes = "Time of a travel")
	private Double timeOfTravel;

	@ApiModelProperty(notes = "Time of all travel")
	private Double totalTravelTime;
	
	@ApiModelProperty(notes = "The quantitiy of parts")
	private Integer quantityOfParts;

	@ApiModelProperty(notes = "Time to produce one part")
	private Double totalProcessingTime;

	@ApiModelProperty(notes = "Time to produce all parts")
	private Double processingTime;

	@ManyToOne
	@JoinColumn(name = "tactical_optimization_result_id", referencedColumnName = "id")
	@JsonIgnore
	private TacticalOptimizationResult tacticalOptimizationResult;

	public Route() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRemoteStation() {
		return remoteStation;
	}

	public void setRemoteStation(String remoteStation) {
		this.remoteStation = remoteStation;
	}

	public String getDistributionCenter() {
		return distributionCenter;
	}

	public void setDistributionCenter(String distributionCenter) {
		this.distributionCenter = distributionCenter;
	}

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		this.part = part;
	}
	
	public Integer getTimesTraveled() {
		return timesTraveled;
	}

	public void setTimesTraveled(Integer timesTraveled) {
		this.timesTraveled = timesTraveled;
	}

	public Double getTimeOfTravel() {
		return timeOfTravel;
	}

	public void setTimeOfTravel(Double timeOfTravel) {
		this.timeOfTravel = timeOfTravel;
	}

	public Double getTotalTravelTime() {
		return totalTravelTime;
	}

	public void setTotalTravelTime(Double totalTravelTime) {
		this.totalTravelTime = totalTravelTime;
	}

	public Integer getQuantityOfParts() {
		return quantityOfParts;
	}

	public void setQuantityOfParts(Integer quantityOfParts) {
		this.quantityOfParts = quantityOfParts;
	}

	public Double getTotalProcessingTime() {
		return totalProcessingTime;
	}

	public void setTotalProcessingTime(Double totalProcessingTime) {
		this.totalProcessingTime = totalProcessingTime;
	}

	public Double getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(Double processingTime) {
		this.processingTime = processingTime;
	}

	public TacticalOptimizationResult getTacticalOptimizationResult() {
		return tacticalOptimizationResult;
	}

	public void setTacticalOptimizationResult(TacticalOptimizationResult tacticalOptimizationResult) {
		this.tacticalOptimizationResult = tacticalOptimizationResult;
	}

	@Override
	public String toString() {
		return "Route [id=" + id + ", remoteStation=" + remoteStation + ", distributionCenter=" + distributionCenter
				+ ", part=" + part + ", timesTraveled=" + timesTraveled + ", timeOfTravel=" + timeOfTravel
				+ ", totalTravelTime=" + totalTravelTime + ", quantityOfParts=" + quantityOfParts
				+ ", totalProcessingTime=" + totalProcessingTime + ", processingTime=" + processingTime
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
		Route other = (Route) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
