package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.EventObject;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;

public class AddRuleDefVOEvent extends EventObject {

	private static final long serialVersionUID = 250963721893434342L;

	private RuleDefinitionAnnotationVO[] ruleDefinitionVOs;

	public AddRuleDefVOEvent(Object source, RuleDefinitionAnnotationVO... ruleDefinitionVOs) {
		super(source);
		this.ruleDefinitionVOs = ruleDefinitionVOs;
	}

	protected RuleDefinitionAnnotationVO[] getRuleDefinitionVOs() {
		return ruleDefinitionVOs;
	}

}
