package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;

public class AllExecuteLayerConditionTreeNode extends AbstractConfigTreeNode {

	@Override
	public String getDisplayText() {
		return "所有执行层次";
	}

	@Override
	public String getImageIcon() {
		return null;
	}

	@Override
	public List<IConfigTreeNode> actualGetChildrenNode() {
		List<IConfigTreeNode> result = new ArrayList<IConfigTreeNode>();
		for (ExecuteLayer executeLayer : ExecuteLayer.values()) {
			result.add(new SingleExecuteLayerConditionTreeNode(executeLayer));
		}
		return result;
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public List<RuleDefinitionAnnotationVO> filterRuleDefinitions(RuleDefinitionFilterContext filterContext) {
		return filterContext.getRuleDefinitionVoList();
	}

}
