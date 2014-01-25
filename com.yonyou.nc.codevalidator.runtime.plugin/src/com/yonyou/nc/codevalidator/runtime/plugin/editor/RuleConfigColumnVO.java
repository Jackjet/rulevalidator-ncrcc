package com.yonyou.nc.codevalidator.runtime.plugin.editor;

/**
 * 规则配置的列表VO
 * 
 * @author mazhqa
 * @since V2.4
 */
public final class RuleConfigColumnVO {

	private final String displayName;
	private final int columnWidth;
	private final IRuleConfigVOProperty ruleConfigVOProperty;

	public RuleConfigColumnVO(String displayName, int columnWidth, IRuleConfigVOProperty ruleConfigVOProperty) {
		super();
		this.displayName = displayName;
		this.columnWidth = columnWidth;
		this.ruleConfigVOProperty = ruleConfigVOProperty;
	}

	public String getDisplayName() {
		return displayName;
	}

	public int getColumnWidth() {
		return columnWidth;
	}
	
	public IRuleConfigVOProperty getRuleConfigVOProperty() {
		return ruleConfigVOProperty;
	}
	
}
