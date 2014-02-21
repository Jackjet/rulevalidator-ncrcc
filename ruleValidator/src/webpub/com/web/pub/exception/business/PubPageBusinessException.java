package com.web.pub.exception.business;

import com.web.pub.exception.message.ExceptionMessage;
import com.web.pub.fw.exception.PubBaseException;

/**
 * 
 * 
 * @author:sunshine
 * @date:2014-1-7 ÏÂÎç4:49:26
 */
public class PubPageBusinessException extends PubBusinessException {

	private static final long serialVersionUID = 4760443523616568248L;

	public PubPageBusinessException() {
		super();
	}

	public PubPageBusinessException(String message) {
		super(message);
	}

	public PubPageBusinessException(String message, String detail) {
		super(message, detail);
	}

	public PubPageBusinessException(String message, PubBaseException detail) {
		super(message, detail);
	}

	public PubPageBusinessException(ExceptionMessage exceptionMessage) {
		super(exceptionMessage);
	}

}
