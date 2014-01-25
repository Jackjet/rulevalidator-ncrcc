package com.yonyou.nc.codevalidator.sdk.upm;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

public class UpmOperateException extends RuleBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UpmOperateException() {
		super();
	}

	public UpmOperateException(String message) {
		super(message);
	}

	public UpmOperateException(Throwable t) {
		super(t);
	}

	public UpmOperateException(String message, Throwable t) {
		super(message, t);
	}

}
