package com.yonyou.nc.codevalidator.resparser;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

public class ResourceParserException extends RuleBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceParserException(String msg) {
		super(msg);
	}

	public ResourceParserException(Throwable t) {
		super(t);
	}

	public ResourceParserException(String msg, Throwable t) {
		super(msg, t);
	}

}
