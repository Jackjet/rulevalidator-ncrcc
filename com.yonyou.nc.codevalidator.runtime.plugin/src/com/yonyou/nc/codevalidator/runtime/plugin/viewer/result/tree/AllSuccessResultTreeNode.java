package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result.tree;

import java.util.List;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.RuleExecuteStatus;

public class AllSuccessResultTreeNode extends AbstractFilteredResultTreeNode {

	@Override
	public String getDisplayText() {
		return "所有成功规则";
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	protected boolean filterResult(IRuleExecuteResult result) {
		return result.getRuleExecuteStatus() == RuleExecuteStatus.SUCCESS;
	}

	@Override
	protected List<IResultTreeNode> getChildrenNodes() {
		return null;
	}

	@Override
	public String getImageIcon() {
		return RuleExecuteStatus.SUCCESS.getIconName();
	}

}
