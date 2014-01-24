package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;

public class UncheckedRulesConditionTreeNode extends AbstractConfigTreeNode {

	@Override
	public String getDisplayText() {
		return "Œ¥—°‘ÒπÊ‘Ú";
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
		List<RuleDefinitionAnnotationVO> ruleDefinitionVoList = filterContext.getRuleDefinitionVoList();
		List<RuleDefinitionAnnotationVO> checkedRuleDefinitionVoList = filterContext.getCheckedRuleDefinitionVoList();
		List<RuleDefinitionAnnotationVO> selectedRuleDefinitionList = filterContext.getSelectedRuleDefinitionList();
		// Collect
		List<RuleDefinitionAnnotationVO> result = new ArrayList<RuleDefinitionAnnotationVO>();
		for (RuleDefinitionAnnotationVO ruleDefinitionVO : ruleDefinitionVoList) {
			if (!checkedRuleDefinitionVoList.contains(ruleDefinitionVO)
					&& !selectedRuleDefinitionList.contains(ruleDefinitionVO)) {
				result.add(ruleDefinitionVO);
			}
		}
		return result;
	}

}
