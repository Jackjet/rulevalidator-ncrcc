package com.yonyou.nc.codevalidator.rule.except;

/**
 * 用于规则配置产生的异常
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
