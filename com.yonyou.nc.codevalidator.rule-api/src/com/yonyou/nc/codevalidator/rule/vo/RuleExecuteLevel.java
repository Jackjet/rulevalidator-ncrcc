package com.yonyou.nc.codevalidator.rule.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 规则执行的等级，类似Sonar中将规则分成不同的级别执行
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
	 * 得到所有的执行规则等级字符串显示
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
	 * 从字符串中获得对应的规则执行等级
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
