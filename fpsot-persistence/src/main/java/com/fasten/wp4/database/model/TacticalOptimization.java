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
	private BigDecimal distanceWeight;
	private BigDecimal timeWeight;

	// location-allocation
	private BigDecimal productionCenterCost;// FC_R annual fixed cost of an IF
	private BigDecimal sramCost;// annual fixed cost of holding a 3D printer
	private BigDecimal sramCapacity;// annual capacity of a 3D printer (in hours) = 8h * 22 days * 12
									// months
	private BigDecimal capitalCost; // %$cc$ - percentage of the average stock applied to calculate
	private BigDecimal stockoutCost; // %$sc$ - stockout cost, the cost of having a stockout of a part
										// on an IF during a review time; %sc
	private Integer maximumSrams; // $max^{P}$ - maximum of printers in an IF;

	private BigDecimal leadTimeLimit; // $max^{H}$ - maximum lead time (in hours) of a PC deliver to the

	// demand range
	private Date initialDate;
	private Date endDate;
	
	private Boolean clustered;
	
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

	public Integer getMaximumLocations() {
		return maximumLocations;
	}

	public void setMaximumLocations(Integer maximumLocations) {
		this.maximumLocations = maximumLocations;
	}

	public BigDecimal getDistanceWeight() {
		return distanceWeight;
	}

	public void setDistanceWeight(BigDecimal distanceWeight) {
		this.distanceWeight = distanceWeight;
	}

	public BigDecimal getTimeWeight() {
		return timeWeight;
	}

	public void setTimeWeight(BigDecimal timeWeight) {
		this.timeWeight = timeWeight;
	}

	public BigDecimal getProductionCenterCost() {
		return productionCenterCost;
	}

	public void setProductionCenterCost(BigDecimal productionCenterCost) {
		this.productionCenterCost = productionCenterCost;
	}

	public BigDecimal getSramCost() {
		return sramCost;
	}

	public void setSramCost(BigDecimal sramCost) {
		this.sramCost = sramCost;
	}

	public BigDecimal getSramCapacity() {
		return sramCapacity;
	}

	public void setSramCapacity(BigDecimal sramCapacity) {
		this.sramCapacity = sramCapacity;
	}

	public BigDecimal getCapitalCost() {
		return capitalCost;
	}

	public void setCapitalCost(BigDecimal capitalCost) {
		this.capitalCost = capitalCost;
	}

	public BigDecimal getStockoutCost() {
		return stockoutCost;
	}

	public void setStockoutCost(BigDecimal stockoutCost) {
		this.stockoutCost = stockoutCost;
	}

	public Integer getMaximumSrams() {
		return maximumSrams;
	}

	public void setMaximumSrams(Integer maximumSrams) {
		this.maximumSrams = maximumSrams;
	}

	public BigDecimal getLeadTimeLimit() {
		return leadTimeLimit;
	}

	public void setLeadTimeLimit(BigDecimal leadTimeLimit) {
		this.leadTimeLimit = leadTimeLimit;
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

	public Boolean getClustered() {
		return clustered;
	}

	public void setClustered(Boolean clustered) {
		this.clustered = clustered;
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
