package com.fasten.wp4.database.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(description = "Tactical optimization result")
public class TacticalOptimizationResult implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private TacticalOptimization study;

	@OneToMany(mappedBy = "tacticalOptimizationResult",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SRAMsAllocated> printers;
	
	@OneToMany(mappedBy = "tacticalOptimizationResult",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Route> routes;

	@OneToMany(mappedBy = "tacticalOptimizationResult",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InternalSupply> internalSuppliers;

	@OneToMany(mappedBy = "tacticalOptimizationResult",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Production> productions;
	
	public TacticalOptimizationResult() {
		super();
	}

	public TacticalOptimizationResult(Long id, TacticalOptimization study, List<SRAMsAllocated> printers,
			List<Route> routes, List<InternalSupply> internalSuppliers, List<Production> productions,
			TacticalOptimizationResult tacticalOptimizationResult) {
		super();
		this.id = id;
		this.study = study;
		this.printers = printers;
		this.routes = routes;
		this.internalSuppliers = internalSuppliers;
		this.productions = productions;
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

	public List<InternalSupply> getInternalSuppliers() {
		return internalSuppliers;
	}

	public void setInternalSuppliers(List<InternalSupply> internalSuppliers) {
		this.internalSuppliers = internalSuppliers;
	}

	public List<Production> getProductions() {
		return productions;
	}

	public void setProductions(List<Production> productions) {
		this.productions = productions;
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
		return "TacticalOptimizationResult [id=" + id + ", study=" + study + ", printers=" + printers + ", routes="
				+ routes + ", internalSuppliers=" + internalSuppliers + ", productions=" + productions + "]";
	}

}
