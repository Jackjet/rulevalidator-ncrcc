package com.web.account.exception;

import com.web.pub.fw.exception.PubBaseException;

/**
 * ÖØ¸´µÇÂ¼Òì³£
 * 
 * @author lixln
 * 
 */
public class PubRepeatLoginException extends PubBaseException {
	private static final long serialVersionUID = 5196036887390401151L;

	public PubRepeatLoginException() {
		super("ÖØ¸´µÇÂ¼");
	}

	public PubRepeatLoginException(PubBaseException e) {
		super("ÖØ¸´µÇÂ¼", e);
	}
}
