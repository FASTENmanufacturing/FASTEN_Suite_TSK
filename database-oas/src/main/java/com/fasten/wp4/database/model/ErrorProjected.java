package com.fasten.wp4.database.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="error_projected", schema = "grafana_data")//catalog = "ufg_qa"
public class ErrorProjected {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name="error_type")
	private ErrorType errorType;
	
	@Enumerated(EnumType.STRING)
	@Column(name="error_subtype")
	private PredictionModel errorSubtype;
	
	@Enumerated(EnumType.STRING)
	@Column(name="error_method")
	private ErrorMethod errorMethod;

	private BigDecimal value;

	@ManyToOne
	@JoinColumn(name = "demand_projection_study_id", referencedColumnName = "id")
	@JsonIgnore
	private DemandProjectionStudy demandProjectionStudy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ErrorType getErrorType() {
		return errorType;
	}

	public void setErrorType(ErrorType errorType) {
		this.errorType = errorType;
	}

	public PredictionModel getErrorSubtype() {
		return errorSubtype;
	}

	public void setErrorSubtype(PredictionModel errorSubtype) {
		this.errorSubtype = errorSubtype;
	}

	public ErrorMethod getErrorMethod() {
		return errorMethod;
	}

	public void setErrorMethod(ErrorMethod errorMethod) {
		this.errorMethod = errorMethod;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public DemandProjectionStudy getDemandProjectionStudy() {
		return demandProjectionStudy;
	}

	public void setDemandProjectionStudy(DemandProjectionStudy demandProjectionStudy) {
		this.demandProjectionStudy = demandProjectionStudy;
	}

	@Override
	public String toString() {
		return "ErrorProjected [id=" + id + ", errorType=" + errorType + ", errorSubtype=" + errorSubtype
				+ ", errorMethod=" + errorMethod + ", value=" + value + ", demandProjectionStudy="
				+ demandProjectionStudy + "]";
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
		ErrorProjected other = (ErrorProjected) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}
