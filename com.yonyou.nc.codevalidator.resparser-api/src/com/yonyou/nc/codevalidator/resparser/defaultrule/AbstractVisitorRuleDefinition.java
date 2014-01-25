package com.yonyou.nc.codevalidator.resparser.defaultrule;

import com.yonyou.nc.codevalidator.resparser.IResourceManager;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFactory;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.impl.AbstractRuleDefinition;

public abstract class AbstractVisitorRuleDefinition extends AbstractRuleDefinition {

	@Override
	public final IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		IResourceManager resourceManager = ResourceManagerFactory.getInstance().getResourceManager();
		return visitorResource(ruleExecContext, resourceManager);
	}

	protected abstract IRuleExecuteResult visitorResource(IRuleExecuteContext ruleExecContext,
			IResourceManager resourceManager) throws RuleBaseException;

}
