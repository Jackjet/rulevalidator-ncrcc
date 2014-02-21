package com.web.account.exception;

import com.web.pub.fw.exception.PubBaseException;

/**
 * 没有用户名异常
 * 
 * @author lixln
 * 
 */
public class PubNotUserIdException extends PubBaseException {
	private static final long serialVersionUID = 1532840905506326789L;

	public PubNotUserIdException() {
		super("没有用户名");
	}

	public PubNotUserIdException(PubBaseException e) {
		super("没有用户名", e);
	}
}
