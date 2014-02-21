package com.web.exception.system;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.web.pub.exception.message.ExceptionMessage;

public class EcpSystemException extends RuntimeException {

	protected final Log logger = LogFactory.getLog(getClass());

	private static final long serialVersionUID = -132411326354318110L;

	public EcpSystemException(Throwable exception) {
		super(exception);
	}

	protected ExceptionMessage exceptionMessage;

	public ExceptionMessage getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(ExceptionMessage exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
}
