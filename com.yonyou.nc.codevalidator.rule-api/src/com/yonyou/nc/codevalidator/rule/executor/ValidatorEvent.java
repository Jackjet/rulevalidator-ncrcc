package com.yonyou.nc.codevalidator.rule.executor;

public enum ValidatorEvent {

	/**
	 * ��֤��ʼ
	 */
	START("ִ�п�ʼ"),
	/**
	 * ��֤����
	 */
	END("ִ�н���");

	ValidatorEvent(String text) {
		this.text = text;
	}

	private String text;

	public String getText() {
		return text;
	}

}
