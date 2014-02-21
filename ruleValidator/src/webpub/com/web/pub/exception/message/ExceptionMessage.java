package com.web.pub.exception.message;

import java.io.Serializable;

/**
 * 
 * 
 * @author:sunshine
 * @date:2014-1-7 ÏÂÎç4:49:32
 */
public class ExceptionMessage {
	private String message;
	private String detail;
	private Serializable userObject;

	public ExceptionMessage(String message, String detail) {
		super();
		this.message = message;
		this.detail = detail;
	}

	public ExceptionMessage(Serializable userObject) {
		super();
		this.userObject = userObject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Serializable getUserObject() {
		return userObject;
	}

	public void setUserObject(Serializable userObject) {
		this.userObject = userObject;
	}

}
