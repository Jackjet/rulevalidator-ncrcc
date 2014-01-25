package com.yonyou.nc.codevalidator.rule.except;

public class RuleBaseRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 4413240035495738706L;

	public RuleBaseRuntimeException() {
		super();
	}

	public RuleBaseRuntimeException(String msg) {
		super(msg);
	}

	public RuleBaseRuntimeException(Throwable throwable) {
		super(throwable);
	}

	public RuleBaseRuntimeException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

}
