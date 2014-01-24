package com.yonyou.nc.codevalidator.sdk.aop;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

public class AopOperateException extends RuleBaseException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AopOperateException() {
		super();
	}

	public AopOperateException(String message) {
		super(message);
	}

	public AopOperateException(Throwable t) {
		super(t);
	}

	public AopOperateException(String message, Throwable t) {
		super(message, t);
	}

}
