package com.web.account.exception;

import com.web.pub.fw.exception.PubBaseException;

/**
 * 没有密码异常
 * 
 * @author lixln
 * 
 */
public class PubNotPasswordException extends PubBaseException {
	private static final long serialVersionUID = 7000442887820208229L;

	public PubNotPasswordException() {
		super("没有密码");
	}

	public PubNotPasswordException(PubBaseException e) {
		super("没有密码", e);
	}
}
