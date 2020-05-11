package com.fasten.wp4.database.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@ApiModel(description = "The forecasting study input data")
public class Prediction implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@Enumerated(EnumType.STRING)
	private PredictionModelSelection modelSelection;
	
	@ElementCollection
	@Enumerated(EnumType.STRING)
	private List<PredictionModel> models;
	
	@Enumerated(EnumType.STRING)
	private Granularity granularity;

	private Integer horizon;
	
	@ApiModelProperty(notes = "The part")
	@ManyToOne
	private Part part;
	
	@ApiModelProperty(notes = "The RS")
	@ManyToOne
	private RemoteStation remoteStation;
	
	// demand range
	private Date initialDate;
	
	private Date endDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PredictionModel> getModels() {
		return models;
	}

	public void setModels(List<PredictionModel> models) {
		this.models = models;
	}

	public Granularity getGranularity() {
		return granularity;
	}

	public void setGranularity(Granularity granularity) {
		this.granularity = granularity;
	}

	public Integer getHorizon() {
		return horizon;
	}

	public void setHorizon(Integer horizon) {
		this.horizon = horizon;
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public RemoteStation getRemoteStation() {
		return remoteStation;
	}

	public void setRemoteStation(RemoteStation remoteStation) {
		this.remoteStation = remoteStation;
	}

	public Date getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public PredictionModelSelection getModelSelection() {
		return modelSelection;
	}

	public void setModelSelection(PredictionModelSelection modelSelection) {
		this.modelSelection = modelSelection;
	}

	@Override
	public String toString() {
		return "Prediction [id=" + id + ", name=" + name + ", modelSelection=" + modelSelection + ", models=" + models
				+ ", granularity=" + granularity + ", horizon=" + horizon + ", part=" + part + ", remoteStation="
				+ remoteStation + ", initialDate=" + initialDate + ", endDate=" + endDate + "]";
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
		Prediction other = (Prediction) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}

