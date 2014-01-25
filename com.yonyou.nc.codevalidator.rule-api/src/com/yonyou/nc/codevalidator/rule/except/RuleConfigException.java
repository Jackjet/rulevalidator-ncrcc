package com.yonyou.nc.codevalidator.rule.except;

/**
 * ���ڹ������ò������쳣
 * @author mazhqa
 * @since V2.7
 */
public class RuleConfigException extends RuleBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RuleConfigException(String s) {
		super(s);
	}

	public RuleConfigException(Throwable cause) {
		super(cause);
	}

	public RuleConfigException(String message, Throwable cause) {
		super(message, cause);
	}

}
