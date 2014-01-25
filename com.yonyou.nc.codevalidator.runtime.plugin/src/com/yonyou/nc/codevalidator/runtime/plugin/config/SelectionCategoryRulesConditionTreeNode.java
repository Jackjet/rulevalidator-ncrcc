package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;

public class SelectionCategoryRulesConditionTreeNode extends AbstractConfigTreeNode {

	@Override
	public String getDisplayText() {
		return "根据选择情况分类";
	}

	@Override
	public String getImageIcon() {
		return null;
	}

	@Override
	public List<IConfigTreeNode> actualGetChildrenNode() {
		List<IConfigTreeNode> result = new ArrayList<IConfigTreeNode>();
		result.add(new CheckedRulesConditonTreeNode());
		result.add(new SelectedRulesConditionTreeNode());
		result.add(new UncheckedRulesConditionTreeNode());
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
