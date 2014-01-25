package com.yonyou.nc.codevalidator.rule.impl;

import com.yonyou.nc.codevalidator.rule.IGlobalRuleDefinition;

public abstract class AbstractGlobalRuleDefinition implements IGlobalRuleDefinition {
	
	@Override
	public String getIdentifier() {
		return getClass().getName();
	}

}
