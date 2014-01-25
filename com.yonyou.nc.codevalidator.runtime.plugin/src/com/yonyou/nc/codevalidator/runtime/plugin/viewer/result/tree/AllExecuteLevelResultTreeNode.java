package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result.tree;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.vo.RuleExecuteLevel;

public class AllExecuteLevelResultTreeNode extends AbstractResultTreeNode {
	
	@Override
	public String getDisplayText() {
		return "所有执行级别";
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public List<IRuleExecuteResult> getRuleExecResultList() {
		return getAllExecResults();
	}

	@Override
	protected List<IResultTreeNode> getChildrenNodes() {
		List<IResultTreeNode> result = new ArrayList<IResultTreeNode>();
		for (RuleExecuteLevel ruleExecuteLevel : RuleExecuteLevel.values()) {
			result.add(new ExecuteLevelResultTreeNode(ruleExecuteLevel));
		}
		return result;
	}

	@Override
	public String getImageIcon() {
		return "/images/ruletype.gif";
	}
}
