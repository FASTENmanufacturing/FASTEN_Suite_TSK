package com.fasten.wp4.database.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@ApiModel(description = "Details about SRAM. ")
public class SRAM implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String code;

	@ApiModelProperty(notes = "Annual capacity in hours)")
	private Integer capacity;

	@ApiModelProperty(notes = "Annual fixed cost (currency)")
	private BigDecimal fixedCost;

	@Embedded
	private SRAMEnviromentalInfo enviromentalInfo;
	
	@Embedded
	private SRAMCapabilities capabilities;

	@Enumerated(EnumType.STRING)
	private SRAMProcessStatus processStatus;

	@Enumerated(EnumType.STRING)
	private SRAMStatus status;

	public SRAM() {
		super();
	}

	public SRAM(Long id, String code, Integer capacity, BigDecimal fixedCost, SRAMEnviromentalInfo enviromentalInfo,
			SRAMCapabilities capabilities, SRAMProcessStatus processStatus, SRAMStatus status) {
		super();
		this.id = id;
		this.code = code;
		this.capacity = capacity;
		this.fixedCost = fixedCost;
		this.enviromentalInfo = enviromentalInfo;
		this.capabilities = capabilities;
		this.processStatus = processStatus;
		this.status = status;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public BigDecimal getFixedCost() {
		return fixedCost;
	}

	public void setFixedCost(BigDecimal fixedCost) {
		this.fixedCost = fixedCost;
	}

	public SRAMEnviromentalInfo getEnviromentalInfo() {
		return enviromentalInfo;
	}

	public void setEnviromentalInfo(SRAMEnviromentalInfo enviromentalInfo) {
		this.enviromentalInfo = enviromentalInfo;
	}

	public SRAMProcessStatus getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(SRAMProcessStatus processStatus) {
		this.processStatus = processStatus;
	}

	public SRAMStatus getStatus() {
		return status;
	}

	public void setStatus(SRAMStatus status) {
		this.status = status;
	}

	public SRAMCapabilities getCapabilities() {
		return capabilities;
	}

	public void setCapabilities(SRAMCapabilities capabilities) {
		this.capabilities = capabilities;
	}
	

}