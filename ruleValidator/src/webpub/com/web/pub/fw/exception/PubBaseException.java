package com.web.pub.fw.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PubBaseException extends Exception {

	protected static final Log logger = LogFactory
			.getLog(PubBaseException.class);

	private String detailMessage;

	/**
     * 
     */
	private static final long serialVersionUID = -2249288681399839579L;

	public PubBaseException() {
		super();
	}

	public PubBaseException(String message, Throwable cause) {
		this(cause);
		this.detailMessage = message;
	}

	public PubBaseException(String message) {
		this.detailMessage = message;
	}

	public PubBaseException(Throwable cause) {
		// super(cause);
		this.detailMessage = cause.getMessage();
		this.setStackTrace(cause.getStackTrace());
	}

	public PubBaseException(Exception e) {
		this(e.getMessage(), e);
	}

	@Override
	public String getMessage() {
		return detailMessage;
	}

}
