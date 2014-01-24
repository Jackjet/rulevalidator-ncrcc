package com.yonyou.nc.codevalidator.export.excel.utils;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

public class ExcelOperateException extends RuleBaseException {

	private static final long serialVersionUID = -233440383247762246L;

	public ExcelOperateException() {

	}

	public ExcelOperateException(Throwable cause) {
		super(cause);
	}

	public ExcelOperateException(String message, Throwable cause) {
		super(message, cause);
	}

}
