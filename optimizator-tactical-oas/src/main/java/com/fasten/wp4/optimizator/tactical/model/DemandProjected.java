package com.fasten.wp4.optimizator.tactical.model;

import java.io.Serializable;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="demand_projected", schema = "grafana_data")//catalog = "ufg_qa"
public class DemandProjected implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name="demand_type")
	private DemandType demandType;
	
	@Enumerated(EnumType.STRING)
	@Column(name="demand_subtype")
	private PredictionModel demandSubtype;

	@ManyToOne
	@JoinColumn(name = "part_id", referencedColumnName = "id")
	private Part part;

	@Temporal(TemporalType.DATE)
	@Column(name="projected_order_date")
	private Date projectedOrderDate;

	private Integer quantity;

	@ManyToOne
	@JoinColumn(name = "remote_station_id", referencedColumnName = "id")
	private RemoteStation remoteStation;

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

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public Date getProjectedOrderDate() {
		return projectedOrderDate;
	}

	public void setProjectedOrderDate(Date projectedOrderDate) {
		this.projectedOrderDate = projectedOrderDate;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public RemoteStation getRemoteStation() {
		return remoteStation;
	}

	public void setRemoteStation(RemoteStation remoteStation) {
		this.remoteStation = remoteStation;
	}

	public DemandProjectionStudy getDemandProjectionStudy() {
		return demandProjectionStudy;
	}

	public void setDemandProjectionStudy(DemandProjectionStudy demandProjectionStudy) {
		this.demandProjectionStudy = demandProjectionStudy;
	}
	
	public DemandType getDemandType() {
		return demandType;
	}

	public void setDemandType(DemandType demandType) {
		this.demandType = demandType;
	}

	public PredictionModel getDemandSubtype() {
		return demandSubtype;
	}

	public void setDemandSubtype(PredictionModel demandSubtype) {
		this.demandSubtype = demandSubtype;
	}

	@Override
	    public int hashCode() {
	        int hash = 0;
	        hash += (id != null ? id.hashCode() : 0);
	        return hash;
	    }

	    @Override
	    public boolean equals(Object object) {
	        // TODO: Warning - this method won't work in the case the id fields are not set
	        if (!(object instanceof DemandProjected)) {
	            return false;
	        }
	        DemandProjected other = (DemandProjected) object;
	        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	            return false;
	        }
	        return true;
	    }

		@Override
		public String toString() {
			return "DemandProjected [id=" + id + ", demandType=" + demandType + ", demandSubtype=" + demandSubtype
					+ ", part=" + part + ", projectedOrderDate=" + projectedOrderDate + ", quantity=" + quantity
					+ ", remoteStation=" + remoteStation + ", demandProjectionStudy=" + demandProjectionStudy + "]";
		}

}