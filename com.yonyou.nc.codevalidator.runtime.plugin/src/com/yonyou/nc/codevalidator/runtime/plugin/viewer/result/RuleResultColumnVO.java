package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result;

/**
 * ����ִ�н���б�VO���������ñ�������
 * 
 * @author mazhqa
 * @since V2.8
 */
public class RuleResultColumnVO {

	private final String displayName;
	private final int columnWidth;
	private final IRuleResultVOProperty ruleResultVoProperty;

	public RuleResultColumnVO(String displayName, int columnWidth, IRuleResultVOProperty ruleResultVoProperty) {
		this.displayName = displayName;
		this.columnWidth = columnWidth;
		this.ruleResultVoProperty = ruleResultVoProperty;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getColumnWidth() {
		return columnWidth;
	}

	public IRuleResultVOProperty getRuleResultVoProperty() {
		return ruleResultVoProperty;
	}

}
