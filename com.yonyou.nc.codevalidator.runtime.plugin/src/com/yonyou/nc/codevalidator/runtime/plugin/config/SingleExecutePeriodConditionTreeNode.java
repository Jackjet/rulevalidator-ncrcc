package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.Collections;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;

public class SingleExecutePeriodConditionTreeNode extends AbstractSingleConditionTreeNode {
	
	private final ExecutePeriod executePeriod;
	

	public SingleExecutePeriodConditionTreeNode(ExecutePeriod executePeriod) {
		this.executePeriod = executePeriod;
	}

	@Override
	public String getDisplayText() {
		return executePeriod.getName();
	}

	@Override
	public String getImageIcon() {
		return null;
	}

	@Override
	public List<IConfigTreeNode> actualGetChildrenNode() {
		return Collections.emptyList();
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	protected boolean fulfillCondition(RuleDefinitionAnnotationVO ruleDefinitionVO) {
		return executePeriod.equals(ruleDefinitionVO.getExecutePeriod());
	}

}
