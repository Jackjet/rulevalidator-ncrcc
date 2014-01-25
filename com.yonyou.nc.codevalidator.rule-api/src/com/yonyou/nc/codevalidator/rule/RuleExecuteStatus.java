package com.yonyou.nc.codevalidator.rule;

/**
 * 规则执行状态
 * 
 * @author mazhqa
 * @since V2.7
 */
public enum RuleExecuteStatus {

	SUCCESS("执行成功", "/images/success.gif"), FAIL("执行失败", "/images/fail.gif"), IGNORED("忽略",
			"/images/ignored.gif"), EXCEPTION("执行异常", "/images/exception.png");

	private String message;
	private String iconName;

	private RuleExecuteStatus(String message, String iconName) {
		this.message = message;
		this.iconName = iconName;
	}

	public String getMessage() {
		return message;
	}

	public String getIconName() {
		return iconName;
	}

}
