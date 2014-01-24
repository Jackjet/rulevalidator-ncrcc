package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.Collections;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;

public class SingleExecuteLayerConditionTreeNode extends AbstractSingleConditionTreeNode {
	
	private final ExecuteLayer executeLayer;
	
	public SingleExecuteLayerConditionTreeNode(ExecuteLayer executeLayer){
		this.executeLayer = executeLayer;
	}

	@Override
	public String getDisplayText() {
		return executeLayer.name();
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
		return executeLayer.equals(ruleDefinitionVO.getExecuteLayer());
	}

}
