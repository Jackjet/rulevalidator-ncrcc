/*
 * 创建日期 2005-10-14
 *
 */
package com.yonyou.nc.codevalidator.dao;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * @author hey
 * 
 *         数据访问对象异常类
 */
public class DAOException extends RuleBaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6547640567505121180L;

	/**
	 * 
	 */
	public DAOException() {
		super();
	}

	/**
	 * @param s
	 */
	public DAOException(String s) {
		super(s);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DAOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param cause
	 */
	public DAOException(Throwable cause) {
		super(cause);
	}

}
