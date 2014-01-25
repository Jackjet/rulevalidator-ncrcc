package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;

public abstract class AbstractFilteredResultTreeNode extends AbstractResultTreeNode {

	@Override
	public final List<IRuleExecuteResult> getRuleExecResultList() {
//		Map<BusinessComponent, List<IRuleExecuteResult>> busiComp2ResultMap = sessionRuleExecuteResult
//				.getBusiComp2ResultMap();
//		List<IRuleExecuteResult> globalExecuteResultList = sessionRuleExecuteResult.getGlobalExecuteResultList();
		List<IRuleExecuteResult> ruleExecuteResults = sessionRuleExecuteResult.getRuleExecuteResults();
		List<IRuleExecuteResult> result = new ArrayList<IRuleExecuteResult>();
		
		for (IRuleExecuteResult executeResult : ruleExecuteResults) {
			if(filterResult(executeResult)) {
				result.add(executeResult);
			}
		}
//		
//		for (List<IRuleExecuteResult> resultList : busiComp2ResultMap.values()) {
//			for (IRuleExecuteResult executeResult : resultList) {
//				if (filterResult(executeResult)) {
//					result.add(executeResult);
//				}
//			}
//		}
		
		Collections.sort(result, new RuleIdentifierComparator());
		return result;
	}

	protected abstract boolean filterResult(IRuleExecuteResult result);

	
	public static class RuleIdentifierComparator implements Comparator<IRuleExecuteResult> {
		
		@Override
		public int compare(IRuleExecuteResult o1, IRuleExecuteResult o2) {
			return o1.getRuleDefinitionIdentifier().compareTo(o2.getRuleDefinitionIdentifier());
		}
	};
}

