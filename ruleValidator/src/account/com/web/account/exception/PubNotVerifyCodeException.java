package com.web.account.exception;

import com.web.pub.fw.exception.PubBaseException;

/**
 * 登录无验证码异常
 * 
 * @author lixln
 * 
 */
public class PubNotVerifyCodeException extends PubBaseException {
	private static final long serialVersionUID = 933326572614778517L;

	public PubNotVerifyCodeException() {
		// 尚未考虑国际化
		super("没有验证码");
	}

	public PubNotVerifyCodeException(PubBaseException e) {
		// 尚未考虑国际化
		super("没有验证码", e);
	}
}
