package com.fasten.wp4.database.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

//import com.fasten.wp4.database.serializer.CustomDemandProjectedSerializer;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@Entity
@Table(name="demand_projection_studies", schema = "grafana_data")//catalog = "ufg_qa"
public class DemandProjectionStudy implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "study_id", referencedColumnName = "id")
	private Prediction study;
	
	@OneToMany(mappedBy = "demandProjectionStudy",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//	@JsonSerialize(using = CustomDemandProjectedSerializer.class)
	private List<DemandProjected> demandProjecteds;
	
	@OneToMany(mappedBy = "demandProjectionStudy",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//	@JsonSerialize(using = CustomDemandProjectedSerializer.class)
	private List<ErrorProjected> errors;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="prevision_date")
	private Date previsionDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="prevision_period_considered_start")
	private Date previsionPeriodConsideredStart;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="prevision_period_considered_end")
	private Date previsionPeriodConsideredEnd;
	
	@Temporal(TemporalType.DATE)
	@Column(name="real_demand_considered_start")
	private Date realDemandConsideredStart;

	@Temporal(TemporalType.DATE)
	@Column(name="real_demand_considered_end")
	private Date realDemandConsideredEnd;
	
	@Temporal(TemporalType.DATE)
	@Column(name="processed_demand_considered_start")
	private Date processedDemandConsideredStart;

	@Temporal(TemporalType.DATE)
	@Column(name="processed_demand_considered_end")
	private Date processedDemandConsideredEnd;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getPrevisionDate() {
		return previsionDate;
	}

	public void setPrevisionDate(Date previsionDate) {
		this.previsionDate = previsionDate;
	}

	public Date getPrevisionPeriodConsideredStart() {
		return previsionPeriodConsideredStart;
	}

	public void setPrevisionPeriodConsideredStart(Date previsionPeriodConsideredStart) {
		this.previsionPeriodConsideredStart = previsionPeriodConsideredStart;
	}

	public Date getPrevisionPeriodConsideredEnd() {
		return previsionPeriodConsideredEnd;
	}

	public void setPrevisionPeriodConsideredEnd(Date previsionPeriodConsideredEnd) {
		this.previsionPeriodConsideredEnd = previsionPeriodConsideredEnd;
	}

	public List<ErrorProjected> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorProjected> errors) {
		this.errors = errors;
	}

	public Prediction getStudy() {
		return study;
	}

	public void setStudy(Prediction study) {
		this.study = study;
	}

	public Date getRealDemandConsideredStart() {
		return realDemandConsideredStart;
	}

	public void setRealDemandConsideredStart(Date realDemandConsideredStart) {
		this.realDemandConsideredStart = realDemandConsideredStart;
	}

	public Date getRealDemandConsideredEnd() {
		return realDemandConsideredEnd;
	}

	public void setRealDemandConsideredEnd(Date realDemandConsideredEnd) {
		this.realDemandConsideredEnd = realDemandConsideredEnd;
	}
	
	public List<DemandProjected> getDemandProjecteds() {
		return demandProjecteds;
	}

	public void setDemandProjecteds(List<DemandProjected> demandProjecteds) {
		this.demandProjecteds = demandProjecteds;
	}
	
	public Date getProcessedDemandConsideredStart() {
		return processedDemandConsideredStart;
	}

	public void setProcessedDemandConsideredStart(Date processedDemandConsideredStart) {
		this.processedDemandConsideredStart = processedDemandConsideredStart;
	}

	public Date getProcessedDemandConsideredEnd() {
		return processedDemandConsideredEnd;
	}

	public void setProcessedDemandConsideredEnd(Date processedDemandConsideredEnd) {
		this.processedDemandConsideredEnd = processedDemandConsideredEnd;
	}

	public DemandProjected addDemandProjecteds(DemandProjected demandProjected) {
		if (getDemandProjecteds() == null) {
			setDemandProjecteds(new ArrayList<>());
		}
		getDemandProjecteds().add(demandProjected);
		demandProjected.setDemandProjectionStudy(this);
		return demandProjected;
	}
	
	public DemandProjected removeDemandProjecteds(DemandProjected demandProjected) {
		if (getDemandProjecteds() == null) {
			return demandProjected;
		}
		getDemandProjecteds().remove(demandProjected);
		demandProjected.setDemandProjectionStudy(null);
		return demandProjected;
	}
	
	public ErrorProjected addDemandProjecteds(ErrorProjected errorProjected) {
		if (getErrors() == null) {
			setErrors(new ArrayList<>());
		}
		getErrors().add(errorProjected);
		errorProjected.setDemandProjectionStudy(this);
		return errorProjected;
	}
	
	public ErrorProjected removeDemandProjecteds(ErrorProjected errorProjected) {
		if (getErrors() == null) {
			return errorProjected;
		}
		getErrors().remove(errorProjected);
		errorProjected.setDemandProjectionStudy(null);
		return errorProjected;
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
	        if (!(object instanceof DemandProjectionStudy)) {
	            return false;
	        }
	        DemandProjectionStudy other = (DemandProjectionStudy) object;
	        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	            return false;
	        }
	        return true;
	    }

		@Override
		public String toString() {
			return "DemandProjectionStudy [id=" + id + ", study=" + study + ", demandProjecteds=" + demandProjecteds
					+ ", errors=" + errors + ", previsionDate=" + previsionDate + ", previsionPeriodConsideredStart="
					+ previsionPeriodConsideredStart + ", previsionPeriodConsideredEnd=" + previsionPeriodConsideredEnd
					+ ", realDemandConsideredStart=" + realDemandConsideredStart + ", realDemandConsideredEnd="
					+ realDemandConsideredEnd + "]";
		}


}