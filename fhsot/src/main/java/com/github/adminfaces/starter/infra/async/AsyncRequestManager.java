package com.github.adminfaces.starter.infra.async;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

@Named
@ApplicationScoped
public class AsyncRequestManager implements Serializable {
	
	private List<AsyncCall> asyncCalls;
	
	@PostConstruct
	public void init() {
		asyncCalls = new ArrayList<AsyncCall>();
	}

	@Produces
	public List<AsyncCall> getAsyncCalls() {
		return asyncCalls;
	}
	
}
