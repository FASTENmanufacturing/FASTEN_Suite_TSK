package com.fasten.wp4.iot.exception;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 7039855912080114863L;

	public NotFoundException(String exception) {
		super(exception);
	}

	public NotFoundException() {
		super("Resource not found");
	}
}