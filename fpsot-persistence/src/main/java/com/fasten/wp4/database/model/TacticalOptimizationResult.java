package com.fasten.wp4.database.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(description = "Tactical optimization result")
public class TacticalOptimizationResult implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private Double objectivePercent;
	
	private Double pPercent;
	
	private Boolean costBenefit;
	
	private Boolean unfeasible;

	@ManyToOne
	private TacticalOptimization study;

	@OneToMany(mappedBy = "tacticalOptimizationResult",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SRAMsAllocated> printers;
	
	@OneToMany(mappedBy = "tacticalOptimizationResult",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Route> routes;

	public TacticalOptimizationResult() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TacticalOptimization getStudy() {
		return study;
	}

	public void setStudy(TacticalOptimization study) {
		this.study = study;
	}

	public List<SRAMsAllocated> getPrinters() {
		return printers;
	}

	public void setPrinters(List<SRAMsAllocated> printers) {
		this.printers = printers;
	}

	public List<Route> getRoutes() {
		return routes;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}
	
	public Double getObjectivePercent() {
		return objectivePercent;
	}

	public void setObjectivePercent(Double objectivePercent) {
		this.objectivePercent = objectivePercent;
	}

	public Double getpPercent() {
		return pPercent;
	}

	public void setpPercent(Double pPercent) {
		this.pPercent = pPercent;
	}
	
	public Boolean getCostBenefit() {
		return costBenefit;
	}

	public void setCostBenefit(Boolean costBenefit) {
		this.costBenefit = costBenefit;
	}
	
	public Boolean getUnfeasible() {
		return unfeasible;
	}

	public void setUnfeasible(Boolean unfeasible) {
		this.unfeasible = unfeasible;
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
		TacticalOptimizationResult other = (TacticalOptimizationResult) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TacticalOptimizationResult [id=" + id + ", objectivePercent=" + objectivePercent + ", pPercent="
				+ pPercent + ", costBenefit=" + costBenefit + ", unfeasible=" + unfeasible + ", study=" + study
				+ ", printers=" + printers + ", routes=" + routes + "]";
	}


}
