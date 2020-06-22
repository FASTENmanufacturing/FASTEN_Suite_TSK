package com.fasten.wp4.database.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(description = "The optimizator input data")
public class TacticalOptimization implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Enumerated(EnumType.STRING)
	private TacticalOptimizationType type;

	@Enumerated(EnumType.STRING)
	private TacticalOptimizationStatus status = TacticalOptimizationStatus.Invalid;

	private Integer maximumLocations; // p
	
	//private BigDecimal leadTimeLimit;// $max^{H}$ - maximum lead time (in hours) of a PC deliver to the

	private BigDecimal sramCapacity=new BigDecimal(8*3600); //seconds/day = 8h*3600

	// demand range
	private Date initialDate;

	private Date endDate;

	private Boolean usePrediction;

	private Integer horizon;

	@Enumerated(EnumType.STRING)
	private Granularity granularity;

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

	public TacticalOptimizationType getType() {
		return type;
	}

	public void setType(TacticalOptimizationType type) {
		this.type = type;
	}

	public TacticalOptimizationStatus getStatus() {
		return status;
	}

	public void setStatus(TacticalOptimizationStatus status) {
		this.status = status;
	}

	public Integer getMaximumLocations() {
		return maximumLocations;
	}

	public void setMaximumLocations(Integer maximumLocations) {
		this.maximumLocations = maximumLocations;
	}

//	public BigDecimal getLeadTimeLimit() {
//		return leadTimeLimit;
//	}
//
//	public void setLeadTimeLimit(BigDecimal leadTimeLimit) {
//		this.leadTimeLimit = leadTimeLimit;
//	}

	public BigDecimal getSramCapacity() {
		return sramCapacity;
	}

	public void setSramCapacity(BigDecimal sramCapacity) {
		this.sramCapacity = sramCapacity;
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

	public Boolean getUsePrediction() {
		return usePrediction;
	}

	public void setUsePrediction(Boolean usePrediction) {
		this.usePrediction = usePrediction;
	}

	public Integer getHorizon() {
		return horizon;
	}

	public void setHorizon(Integer horizon) {
		this.horizon = horizon;
	}

	public Granularity getGranularity() {
		return granularity;
	}

	public void setGranularity(Granularity granularity) {
		this.granularity = granularity;
	}
	
}
