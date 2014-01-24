package com.yonyou.nc.codevalidator.rule;

/**
 * ����ִ��״̬
 * 
 * @author mazhqa
 * @since V2.7
 */
public enum RuleExecuteStatus {

	SUCCESS("ִ�гɹ�", "/images/success.gif"), FAIL("ִ��ʧ��", "/images/fail.gif"), IGNORED("����",
			"/images/ignored.gif"), EXCEPTION("ִ���쳣", "/images/exception.png");

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
