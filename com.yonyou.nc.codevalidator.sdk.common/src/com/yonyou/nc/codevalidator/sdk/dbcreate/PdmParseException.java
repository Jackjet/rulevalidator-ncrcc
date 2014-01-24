package com.yonyou.nc.codevalidator.sdk.dbcreate;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * pdm解析验证时产生的异常
 * @author mazhqa
 * @since V2.2
 */
public class PdmParseException extends RuleBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PdmParseException(String message){
		super(message);
	}

}
