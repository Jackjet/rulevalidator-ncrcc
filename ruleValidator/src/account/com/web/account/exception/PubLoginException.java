package com.web.account.exception;

import com.web.pub.fw.exception.PubBaseException;

/**
 * µÇÂ¼Òì³£
 * 
 * @author lixln
 * 
 */
public class PubLoginException extends PubBaseException {
	private static final long serialVersionUID = 7250400025270312113L;

	public PubLoginException() {
		super("µÇÂ¼´íÎó");
	}

	public PubLoginException(PubBaseException e) {
		super("µÇÂ¼´íÎó", e);
	}
}
