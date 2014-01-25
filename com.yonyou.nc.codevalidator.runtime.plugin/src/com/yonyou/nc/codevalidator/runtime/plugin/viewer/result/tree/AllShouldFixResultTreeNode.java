package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result.tree;

import java.util.List;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.RuleExecuteStatus;
import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionsReader;

public class AllShouldFixResultTreeNode extends AbstractFilteredResultTreeNode {

	@Override
	public String getImageIcon() {
		return "/images/mustfix.gif";
	}

	@Override
	public String getDisplayText() {
		return "±ØÐëÐÞ¸Ä";
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	protected boolean filterResult(IRuleExecuteResult result) {
		if (result.getRuleExecuteStatus() != RuleExecuteStatus.FAIL) {
			return false;
		}
		String ruleDefinitionIdentifier = result.getRuleDefinitionIdentifier();
		RuleDefinitionAnnotationVO ruleDefinitionVO = RuleDefinitionsReader.getInstance().getRuleDefinitionVO(
				ruleDefinitionIdentifier);
		return ruleDefinitionVO.getRepairLevel() != null && ruleDefinitionVO.getRepairLevel() == RepairLevel.MUSTREPAIR;
	}

	@Override
	protected List<IResultTreeNode> getChildrenNodes() {
		return null;
	}

}
