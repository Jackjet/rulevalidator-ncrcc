package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;

public class AllRepairLevelConditionTreeNode extends AbstractConfigTreeNode{

	@Override
	public String getDisplayText() {
		return "所有修复类型";
	}

	@Override
	public String getImageIcon() {
		return null;
	}

	@Override
	public List<IConfigTreeNode> actualGetChildrenNode() {
		List<IConfigTreeNode> result = new ArrayList<IConfigTreeNode>();
		for (RepairLevel repairLevel : RepairLevel.values()) {
			result.add(new SingleRepairLevelConditionTreeNode(repairLevel));
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
