package com.yonyou.nc.codevalidator.rule.except;

public class RuleBaseException extends Exception {
	private static final long serialVersionUID = -2529426944846583177L;

	private String hint;

	public RuleBaseException() {
		super();
	}

	public RuleBaseException(String s) {
		super(s);
	}

	public RuleBaseException(Throwable cause) {
		super(cause);
	}

	public RuleBaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public java.lang.String getHint() {
		return hint;
	}

	public void setHint(java.lang.String newHint) {
		hint = newHint;
	}
}
