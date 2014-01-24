package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result.tree;

import java.util.List;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.RuleExecuteStatus;

public class AllExceptionResultTreeNode extends AbstractFilteredResultTreeNode {

	@Override
	public String getImageIcon() {
		return RuleExecuteStatus.EXCEPTION.getIconName();
	}

	@Override
	public String getDisplayText() {
		return "所有异常规则";
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	protected boolean filterResult(IRuleExecuteResult result) {
		return result.getRuleExecuteStatus() == RuleExecuteStatus.EXCEPTION;
	}

	@Override
	protected List<IResultTreeNode> getChildrenNodes() {
		return null;
	}

}
