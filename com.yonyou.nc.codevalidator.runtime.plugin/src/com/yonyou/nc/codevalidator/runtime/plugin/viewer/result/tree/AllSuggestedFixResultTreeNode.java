package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result.tree;

import java.util.List;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.RuleExecuteStatus;
import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionsReader;

public class AllSuggestedFixResultTreeNode extends AbstractFilteredResultTreeNode {

	@Override
	public String getImageIcon() {
		return "/images/warning.gif";
	}

	@Override
	public String getDisplayText() {
		return "½¨ÒéÐÞ¸Ä";
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
		return ruleDefinitionVO.getRepairLevel() != null
				&& ruleDefinitionVO.getRepairLevel() == RepairLevel.SUGGESTREPAIR;
	}

	@Override
	protected List<IResultTreeNode> getChildrenNodes() {
		return null;
	}

}
