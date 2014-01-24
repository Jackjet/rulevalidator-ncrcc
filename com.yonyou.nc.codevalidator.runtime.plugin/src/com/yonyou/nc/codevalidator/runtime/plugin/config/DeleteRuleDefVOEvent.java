package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.EventObject;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;

@Deprecated
public class DeleteRuleDefVOEvent extends EventObject {

	private static final long serialVersionUID = 250963721893434342L;

	private List<RuleDefinitionAnnotationVO> ruleDefinitionVOs;

	public DeleteRuleDefVOEvent(Object source, List<RuleDefinitionAnnotationVO> ruleDefinitionVOs) {
		super(source);
		this.ruleDefinitionVOs = ruleDefinitionVOs;
	}

	protected List<RuleDefinitionAnnotationVO> getRuleDefinitionVOs() {
		return ruleDefinitionVOs;
	}

}
