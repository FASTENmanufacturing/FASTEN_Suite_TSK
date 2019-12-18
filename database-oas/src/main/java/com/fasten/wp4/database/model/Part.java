package com.fasten.wp4.database.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Entity
@ApiModel(description = "Details about spare parts. ")
public class Part implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@ApiModelProperty(notes = "The part name.")
	private String name;
	
	@ApiModelProperty(notes = "The code (SKU or another one) of the part.")
	private String code;
	
	private Double width;
	
	private Double height;
	
	private Double depth;
	
	@Enumerated(EnumType.STRING)
	private PartPriority priority; 

	public Part() {
		super();
	}
	
	public Part(Long id, String name, String code, Double width, Double height, Double depth, PartPriority priority) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.priority = priority;
	}

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

	public Double getWidth() {
		return width;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public Double getDepth() {
		return depth;
	}

	public void setDepth(Double depth) {
		this.depth = depth;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public PartPriority getPriority() {
		return priority;
	}

	public void setPriority(PartPriority priority) {
		this.priority = priority;
	}
	
}
