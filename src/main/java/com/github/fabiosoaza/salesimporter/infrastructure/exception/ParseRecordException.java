package com.github.fabiosoaza.salesimporter.infrastructure.exception;

public class ParseRecordException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ParseRecordException(String message) {
		super(message);
	}

	public ParseRecordException(String message, Throwable cause) {
		super(message, cause);
	}

}
