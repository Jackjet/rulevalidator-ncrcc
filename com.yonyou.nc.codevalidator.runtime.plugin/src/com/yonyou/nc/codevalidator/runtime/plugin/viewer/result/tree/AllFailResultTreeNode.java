package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result.tree;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.RuleExecuteStatus;
import com.yonyou.nc.codevalidator.rule.vo.RuleExecuteLevel;

public class AllFailResultTreeNode extends AbstractFilteredResultTreeNode {

	@Override
	public String getDisplayText() {
		return "所有失败规则";
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	protected boolean filterResult(IRuleExecuteResult result) {
		return result.getRuleExecuteStatus() == RuleExecuteStatus.FAIL;
	}

	@Override
	protected List<IResultTreeNode> getChildrenNodes() {
		List<IResultTreeNode> result = new ArrayList<IResultTreeNode>();
		result.add(new AllShouldFixResultTreeNode());
		result.add(new AllSuccessResultTreeNode());
		for (RuleExecuteLevel ruleExecuteLevel : RuleExecuteLevel.values()) {
			result.add(new ExecuteLevelResultTreeNode(ruleExecuteLevel));
		}
		return result;
//		
//		return Arrays.asList(new IResultTreeNode[] { new AllShouldFixResultTreeNode(),
//				new AllSuggestedFixResultTreeNode() });
	}

	@Override
	public String getImageIcon() {
		return RuleExecuteStatus.FAIL.getIconName();
	}

}
