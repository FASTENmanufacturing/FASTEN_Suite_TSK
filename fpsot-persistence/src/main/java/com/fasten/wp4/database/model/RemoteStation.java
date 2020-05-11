package com.fasten.wp4.database.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@ApiModel(description = "Details about the RS - remote station. ")
public class RemoteStation implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String code;
	
	private String name;

	@Embedded
	private Address address;
	
	@Enumerated(EnumType.STRING)
	private RemoteStationPriority priority;
	
	private String unidade;

	private String frete;
	
//	@OneToMany(mappedBy = "remoteStation")
////	@JsonBackReference
//	@JsonIgnore
//	private Set<SRAM> srams= new HashSet<>();

	public RemoteStation() {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RemoteStationPriority getPriority() {
		return priority;
	}

	public void setPriority(RemoteStationPriority priority) {
		this.priority = priority;
	}
	
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public String getFrete() {
		return frete;
	}

	public void setFrete(String frete) {
		this.frete = frete;
	}

//	public Set<SRAM> getSrams() {
//		return srams;
//	}
//
//	public void setSrams(Set<SRAM> srams) {
//		this.srams = srams;
//	}
//	
//	public void addSram(SRAM sram) {
//		srams.add(sram);
//        sram.setRemoteStation(this);
//    }
// 
//    public void removeSram(SRAM sram) {
//    	srams.remove(sram);
//    	sram.setRemoteStation(null);
//    }

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
		RemoteStation other = (RemoteStation) obj;
		return Objects.equals(id, other.id);
	}

}
