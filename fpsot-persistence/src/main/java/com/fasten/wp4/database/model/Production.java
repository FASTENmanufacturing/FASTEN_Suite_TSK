//package com.fasten.wp4.database.model;
//
//import java.io.Serializable;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//
//@Entity
//@ApiModel(description = "Result route output")
//public class Production implements Serializable{
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//	@ApiModelProperty(notes = "The remote station producing the part")
//	private String remoteStation;
//
//	@ApiModelProperty(notes = "The part")
//	private String part;
//	
//	@ManyToOne
//	@JoinColumn(name = "tactical_optimization_result_id", referencedColumnName = "id")
//	@JsonIgnore
//	private TacticalOptimizationResult tacticalOptimizationResult;
//
//	public Production() {
//		super();
//	}
//
//	public Production(Long id, String remoteStation, String part,
//			TacticalOptimizationResult tacticalOptimizationResult) {
//		super();
//		this.id = id;
//		this.remoteStation = remoteStation;
//		this.part = part;
//		this.tacticalOptimizationResult = tacticalOptimizationResult;
//	}
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public String getRemoteStation() {
//		return remoteStation;
//	}
//
//	public void setRemoteStation(String remoteStation) {
//		this.remoteStation = remoteStation;
//	}
//
//	public String getPart() {
//		return part;
//	}
//
//	public void setPart(String part) {
//		this.part = part;
//	}
//
//	public TacticalOptimizationResult getTacticalOptimizationResult() {
//		return tacticalOptimizationResult;
//	}
//
//	public void setTacticalOptimizationResult(TacticalOptimizationResult tacticalOptimizationResult) {
//		this.tacticalOptimizationResult = tacticalOptimizationResult;
//	}
//
//	@Override
//	public String toString() {
//		return "Production [id=" + id + ", remoteStation=" + remoteStation + ", part=" + part
//				+ ", tacticalOptimizationResult=" + tacticalOptimizationResult + "]";
//	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Production other = (Production) obj;
//		if (id == null) {
//			if (other.id != null)
//				return false;
//		} else if (!id.equals(other.id))
//			return false;
//		return true;
//	}
//	
//}