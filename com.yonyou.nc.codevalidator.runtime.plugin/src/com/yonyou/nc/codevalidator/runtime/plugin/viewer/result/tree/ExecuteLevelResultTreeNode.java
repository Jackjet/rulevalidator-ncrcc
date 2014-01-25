package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result.tree;

import java.util.List;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.RuleExecuteStatus;
import com.yonyou.nc.codevalidator.rule.vo.RuleExecuteLevel;

public class ExecuteLevelResultTreeNode extends AbstractFilteredResultTreeNode {

	private final RuleExecuteLevel ruleExecuteLevel;

	public ExecuteLevelResultTreeNode(RuleExecuteLevel ruleExecuteLevel) {
		super();
		this.ruleExecuteLevel = ruleExecuteLevel;
	}

	@Override
	public String getImageIcon() {
		return ruleExecuteLevel.getIconPath();
	}

	@Override
	public String getDisplayText() {
		return ruleExecuteLevel.getDisplayName();
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	protected boolean filterResult(IRuleExecuteResult result) {
		return result.getRuleExecuteStatus() == RuleExecuteStatus.FAIL
				&& result.getRuleExecuteContext().getRuleConfigContext().getRuleExecuteLevel().equals(ruleExecuteLevel);
	}

	@Override
	protected List<IResultTreeNode> getChildrenNodes() {
		return null;
	}

}
