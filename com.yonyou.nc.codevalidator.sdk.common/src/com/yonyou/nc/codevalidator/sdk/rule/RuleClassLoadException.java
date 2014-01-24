package com.yonyou.nc.codevalidator.sdk.rule;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

public class RuleClassLoadException extends RuleBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RuleClassLoadException(String msg) {
		super(msg);
	}

	public RuleClassLoadException(Throwable t) {
		super(t);
	}

	public RuleClassLoadException(String msg, Throwable t) {
		super(msg, t);
	}

}
