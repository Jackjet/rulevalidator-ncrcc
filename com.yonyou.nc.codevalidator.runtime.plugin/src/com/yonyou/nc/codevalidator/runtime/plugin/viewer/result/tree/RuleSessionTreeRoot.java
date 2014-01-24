package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result.tree;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.SessionRuleExecuteResult;

public class RuleSessionTreeRoot extends AbstractResultTreeNode {

	public RuleSessionTreeRoot(SessionRuleExecuteResult sessionRuleExecuteResult) {
		this.sessionRuleExecuteResult = sessionRuleExecuteResult;
	}

	@Override
	public List<IResultTreeNode> getChildrenNodes() {
		List<IResultTreeNode> result = new ArrayList<IResultTreeNode>();
//		result.add(new AllGlobalResultTreeNode());
		result.add(new BusinessComponentListTreeNode(sessionRuleExecuteResult.getBusinessComponent()));
		result.add(new AllRuleTypeTreeNode());
		result.add(new AllSuccessResultTreeNode());
		result.add(new AllFailResultTreeNode());
		result.add(new AllExceptionResultTreeNode());
		result.add(new AllIgnoredResultTreeNode());
//		result.add(new AllExecuteLevelResultTreeNode());
		return result;
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
	public String getDisplayText() {
		return String.format("规则验证(%tT-%tT)", sessionRuleExecuteResult.getStartTime(),
				sessionRuleExecuteResult.getEndTime());
	}

	@Override
	public String getImageIcon() {
		return "/images/execute.gif";
	}

}
