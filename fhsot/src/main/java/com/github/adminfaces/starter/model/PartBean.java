package com.github.adminfaces.starter.model;

import java.io.Serializable;

public class PartBean implements Serializable, Comparable<PartBean>{
	
	private Long id;
	private String name;
	private Double width;
	private Double height;
	private Double depth;

	public PartBean() {
		super();
	}
	
	public PartBean(Long id, String name, Double width, Double height, Double depth) {
		super();
		this.id = id;
		this.name = name;
		this.width = width;
		this.height = height;
		this.depth = depth;
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
	
	@Override
	public int compareTo(PartBean o) {
		return this.id.compareTo(o.getId());
	}
	
}
