package com.web.pub.exception.business;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import com.web.pub.exception.message.ExceptionMessage;
import com.web.pub.fw.exception.PubBaseException;

/**
 * 
 * 
 * @author:sunshine
 * @date:2014-1-7 ÏÂÎç4:49:18
 */
public class PubBusinessException extends RuntimeException {

	private static final long serialVersionUID = -2044343593998763991L;

	public PubBusinessException() {

	}

	public PubBusinessException(String message) {
		ExceptionMessage exceptionMessage = new ExceptionMessage(message, null);
		this.exceptionMessage = exceptionMessage;
	}

	public PubBusinessException(String message, String detail) {
		ExceptionMessage exceptionMessage = new ExceptionMessage(message,
				detail);
		this.exceptionMessage = exceptionMessage;
	}

	public PubBusinessException(String message, PubBaseException ex) {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		PrintStream pstream = new PrintStream(byteOut);
		ex.printStackTrace(pstream);
		pstream.close();
		ExceptionMessage exceptionMessage = new ExceptionMessage(message,
				new String(byteOut.toByteArray()));
		this.exceptionMessage = exceptionMessage;
	}

	public PubBusinessException(ExceptionMessage exceptionMessage) {
		super();
		this.exceptionMessage = exceptionMessage;
	}

	protected ExceptionMessage exceptionMessage;

	public ExceptionMessage getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(ExceptionMessage exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

}
