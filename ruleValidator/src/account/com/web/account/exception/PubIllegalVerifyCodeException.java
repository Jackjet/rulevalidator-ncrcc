package com.web.account.exception;

import com.web.pub.fw.exception.PubBaseException;

/**
 * 验证码错误异常
 * 
 * @author lixln
 * 
 */
public class PubIllegalVerifyCodeException extends PubBaseException {
	private static final long serialVersionUID = -2072951997096104148L;

	public PubIllegalVerifyCodeException() {
		// 尚未考虑国际化
		super("验证码错误");
	}

	public PubIllegalVerifyCodeException(PubBaseException e) {
		// 尚未考虑国际化
		super("验证码错误", e);
	}
}
