/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.starter.infra.async;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Dispatcher;

public class AsyncCall implements Serializable {

    private String id;

    private Call call;
    private Date start;
    private Object study;
    
    private AsyncCallStatus status;
    private Object tag;
    private String host;
    private String name;
    
    private transient Dispatcher dispatcher;
    

    public AsyncCall() {
    	id = UUID.randomUUID().toString();
    }

	public String getId() {
		return id;
	}

	public Object getTag() {
		return tag;
	}

	public void setTag(Object tag) {
		this.tag = tag;
	}

	public Call getCall() {
		return call;
	}

	public void setCall(Call call) {
		this.call = call;
	}

	public Object getStudy() {
		return study;
	}

	public void setStudy(Object study) {
		this.study = study;
	}
	
	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public AsyncCallStatus getStatus() {
		return status;
	}

	public void setStatus(AsyncCallStatus status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Dispatcher getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}
	
	public boolean done() {
		if((new Date().getTime() - this.getStart().getTime()>30000) && (this.getStatus().equals(AsyncCallStatus.Canceled) || this.getStatus().equals(AsyncCallStatus.Executed))) {
			return true;
		}
		return false;
	}
	
}
