package com.yonyou.nc.codevalidator.rule.executor;

public enum ValidatorEvent {

	/**
	 * 验证开始
	 */
	START("执行开始"),
	/**
	 * 验证结束
	 */
	END("执行结束");

	ValidatorEvent(String text) {
		this.text = text;
	}

	private String text;

	public String getText() {
		return text;
	}

}
