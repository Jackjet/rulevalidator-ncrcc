package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result.tree;

import java.util.List;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.SessionRuleExecuteResult;

public abstract class AbstractResultTreeNode implements IResultTreeNode {

	protected SessionRuleExecuteResult sessionRuleExecuteResult;

	@Override
	public void setSessionRuleExecuteResult(SessionRuleExecuteResult sessionRuleExecResult) {
		this.sessionRuleExecuteResult = sessionRuleExecResult;
	}

	@Override
	public SessionRuleExecuteResult getSessionRuleExecuteResult() {
		return sessionRuleExecuteResult;
	}

	@Override
	public final List<IResultTreeNode> actualGetChildrenNode() {
		List<IResultTreeNode> childrenNodeImpl = getChildrenNodes();
		if (childrenNodeImpl != null && childrenNodeImpl.size() > 0) {
			for (IResultTreeNode treeNode : childrenNodeImpl) {
				treeNode.setSessionRuleExecuteResult(sessionRuleExecuteResult);
			}
		}
		return childrenNodeImpl;
	}

	protected abstract List<IResultTreeNode> getChildrenNodes();

	/**
	 * Get all results from session.
	 * 
	 * @return
	 */
	protected List<IRuleExecuteResult> getAllExecResults() {
//		Map<BusinessComponent, List<IRuleExecuteResult>> busiComp2ResultMap = sessionRuleExecuteResult
//				.getBusiComp2ResultMap();
//		List<IRuleExecuteResult> result = new ArrayList<IRuleExecuteResult>();
//		for (Map.Entry<BusinessComponent, List<IRuleExecuteResult>> busiCompResultMapEntry : busiComp2ResultMap
//				.entrySet()) {
//			result.addAll(busiCompResultMapEntry.getValue());
//		}
		return sessionRuleExecuteResult.getRuleExecuteResults();
	}

}
