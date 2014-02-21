package com.web.account.exception;

import com.web.pub.fw.exception.PubBaseException;

/**
 * 没有关联客户异常
 * 
 * @author lixln
 * 
 */
public class PubNotReferCustomerException extends PubBaseException {
	private static final long serialVersionUID = 7362983235536406203L;

	public PubNotReferCustomerException() {
		super("该用户没有关联客户");
	}

	public PubNotReferCustomerException(PubBaseException e) {
		super("该用户没有关联客户", e);
	}
}
