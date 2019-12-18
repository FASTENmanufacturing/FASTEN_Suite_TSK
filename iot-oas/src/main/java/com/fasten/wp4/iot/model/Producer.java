package com.fasten.wp4.iot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Producer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "topic_id", referencedColumnName = "id")
	private Topic topic;

	private String name;

	private Boolean enabled;

	private Long interval;

	private Long initialDelay;

	private String expression;

	public Producer() {
	}
	
	public Producer(Topic topic, String name, Boolean enabled, Long interval, Long initialDelay, String expression) {
		super();
		this.topic = topic;
		this.name = name;
		this.enabled = enabled;
		this.interval = interval;
		this.initialDelay = initialDelay;
		this.expression = expression;
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

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public Long getInterval() {
		return interval;
	}

	public void setInterval(Long interval) {
		this.interval = interval;
	}

	public Long getInitialDelay() {
		return initialDelay;
	}

	public void setInitialDelay(Long initialDelay) {
		this.initialDelay = initialDelay;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

}
