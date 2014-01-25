package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.Collections;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;

public class CheckedRulesConditonTreeNode extends AbstractConfigTreeNode {

	@Override
	public String getDisplayText() {
		return "已选择规则";
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
	public List<RuleDefinitionAnnotationVO> filterRuleDefinitions(RuleDefinitionFilterContext filterContext) {
		return filterContext.getCheckedRuleDefinitionVoList();
	}

}
