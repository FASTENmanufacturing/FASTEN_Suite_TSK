package com.fasten.wp4.database.model;

import java.io.Serializable;
import javax.persistence.*;


@Entity
@Table(name = "webhook", schema="webhook")
public class WebHook implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private String url;
	
	private String event;
	
	@Column(unique = true)
	private String consumerServiceName;

	public WebHook() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getConsumerServiceName() {
		return consumerServiceName;
	}

	public void setConsumerServiceName(String consumerServiceName) {
		this.consumerServiceName = consumerServiceName;
	}

}