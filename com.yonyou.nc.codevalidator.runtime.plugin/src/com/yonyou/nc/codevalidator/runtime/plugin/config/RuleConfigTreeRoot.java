package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;

/**
 * 规则配置的根节点，此时显示所有的规则
 * @author mazhqa
 * @since V2.6
 */
public class RuleConfigTreeRoot extends AbstractConfigTreeNode {

	@Override
	public String getDisplayText() {
		return "所有规则";
	}

	@Override
	public String getImageIcon() {
		return null;
	}

	@Override
	public List<IConfigTreeNode> actualGetChildrenNode() {
		List<IConfigTreeNode> result = new ArrayList<IConfigTreeNode>();
		result.add(new AllCategoryConditionTreeNode());
		result.add(new AllRepairLevelConditionTreeNode());
		result.add(new AllScopeConditionTreeNode());
		result.add(new AllExecutePeriodConditionTreeNode());
		result.add(new AllExecuteLayerConditionTreeNode());
		result.add(new SelectionCategoryRulesConditionTreeNode());
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
