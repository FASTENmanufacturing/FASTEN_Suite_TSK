package com.fasten.wp4.database.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@ApiModel(description = "Operational optimization study")
public class OperationalOptimization implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty(notes = "The State")
	@ManyToOne
	private State state;
	
	@ApiModelProperty(notes = "The city")
	@ManyToOne
	private City city;

	@ApiModelProperty(notes = "The part")
	@ManyToOne
	private Part part;

	@ApiModelProperty(notes = "The quantitiy of parts")
	private Integer quantity;
	
	public OperationalOptimization() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
}
