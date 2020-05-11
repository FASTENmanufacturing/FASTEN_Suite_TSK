package com.fasten.wp4.database.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import io.swagger.annotations.ApiModel;

@Entity
@ApiModel(description = "Details about SRAM. ")
public class SRAM implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String code;

	@Embedded
	private SRAMEnviromentalInfo enviromentalInfo;

	@Embedded
	private SRAMCapabilities capabilities;

	@Enumerated(EnumType.STRING)
	private SRAMProcessStatus processStatus;

	@Enumerated(EnumType.STRING)
	private SRAMStatus status;

	private Double progressCompletion;

	private Double printTime;

	private Double printTimeLeft;

	private String printTimeLeftOrigin;

	@ManyToOne
	@JoinColumn(name = "remote_station_id", referencedColumnName = "id")
//	@JsonManagedReference
	private RemoteStation remoteStation;

	public SRAM() {
		super();
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

	public Double getProgressCompletion() {
		return progressCompletion;
	}

	public void setProgressCompletion(Double progressCompletion) {
		this.progressCompletion = progressCompletion;
	}

	public Double getPrintTime() {
		return printTime;
	}

	public void setPrintTime(Double printTime) {
		this.printTime = printTime;
	}

	public Double getPrintTimeLeft() {
		return printTimeLeft;
	}

	public void setPrintTimeLeft(Double printTimeLeft) {
		this.printTimeLeft = printTimeLeft;
	}

	public String getPrintTimeLeftOrigin() {
		return printTimeLeftOrigin;
	}

	public void setPrintTimeLeftOrigin(String printTimeLeftOrigin) {
		this.printTimeLeftOrigin = printTimeLeftOrigin;
	}

	public RemoteStation getRemoteStation() {
		return remoteStation;
	}

	public void setRemoteStation(RemoteStation remoteStation) {
		this.remoteStation = remoteStation;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SRAM other = (SRAM) obj;
		return Objects.equals(id, other.id);
	}

}