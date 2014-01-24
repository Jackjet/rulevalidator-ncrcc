package com.yonyou.nc.codevalidator.rule.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ����ִ�еĵȼ�������Sonar�н�����ֳɲ�ͬ�ļ���ִ��
 * @author mazhqa
 * @since V2.8
 */
public enum RuleExecuteLevel {

	Blocker("Blocker"), Critical("Critical"), Major("Major"), Minor("Minor"), Info("Info");
	
	private String displayName;
	
	private RuleExecuteLevel(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getIconPath() {
		return String.format("/images/executelevel/%s.png", displayName.toUpperCase());
	}
	
	/**
	 * �õ����е�ִ�й���ȼ��ַ�����ʾ
	 * @return
	 */
	public static List<String> getAllExecuteLevels(){
		List<String> result = new ArrayList<String>(5);
		for (RuleExecuteLevel ruleExecuteLevel : RuleExecuteLevel.values()) {
			result.add(ruleExecuteLevel.getDisplayName());
		}
		return result;
	}
	
	public static List<RuleExecuteLevel> getRuleExecuteLevels(){
		return Arrays.asList(RuleExecuteLevel.values());
	}
	
	/**
	 * ���ַ����л�ö�Ӧ�Ĺ���ִ�еȼ�
	 * @param ruleExecuteLevelString
	 * @return
	 */
	public static RuleExecuteLevel getRuleExecuteLevel(String ruleExecuteLevelString) {
		for (RuleExecuteLevel ruleExecuteLevel : RuleExecuteLevel.values()) {
			if(ruleExecuteLevel.getDisplayName().equals(ruleExecuteLevelString)) {
				return ruleExecuteLevel;
			}
		}
		return RuleExecuteLevel.Critical;
	}

}
