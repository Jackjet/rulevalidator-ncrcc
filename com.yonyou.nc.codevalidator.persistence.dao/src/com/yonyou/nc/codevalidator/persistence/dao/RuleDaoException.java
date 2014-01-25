package com.yonyou.nc.codevalidator.persistence.dao;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * �������ݷ��ʶ������쳣
 * 
 * @author mazhqa
 * @since V2.7
 */
public class RuleDaoException extends RuleBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RuleDaoException(String message) {
		super(message);
	}

	public RuleDaoException(Throwable t) {
		super(t);
	}

	public RuleDaoException(String message, Throwable t) {
		super(message, t);
	}

}
