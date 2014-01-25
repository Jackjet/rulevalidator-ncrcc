package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;
import com.yonyou.nc.codevalidator.rule.annotation.ScopeEnum;

public class AllScopeConditionTreeNode extends AbstractConfigTreeNode {

	@Override
	public String getDisplayText() {
		return "所有应用范围";
	}

	@Override
	public String getImageIcon() {
		return null;
	}

	@Override
	public List<IConfigTreeNode> actualGetChildrenNode() {
		List<IConfigTreeNode> result = new ArrayList<IConfigTreeNode>();
		for (ScopeEnum scope : ScopeEnum.values()) {
			result.add(new SingleScopeConditionTreeNode(scope));
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
