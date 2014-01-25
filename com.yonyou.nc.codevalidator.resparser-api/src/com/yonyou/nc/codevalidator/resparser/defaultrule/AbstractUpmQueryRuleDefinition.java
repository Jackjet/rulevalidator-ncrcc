package com.yonyou.nc.codevalidator.resparser.defaultrule;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.UpmResourceQuery;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.impl.AbstractRuleDefinition;

/**
 * 抽象的upm查询规则定义类，建议查询upm的规则都从该类中派生
 * @author mazhqa
 * @since V1.0
 */
public abstract class AbstractUpmQueryRuleDefinition extends AbstractRuleDefinition {

	@Override
	public final IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		UpmResourceQuery upmResourceQuery = getUpmResourceQuery(ruleExecContext);
		List<UpmResource> upmResources = ResourceManagerFacade.getResource(upmResourceQuery);
		return processUpmRules(ruleExecContext, upmResources);
	}

	/**
	 * Build resource query from context.
	 * @param ruleExecContext
	 * @return
	 * @throws ResourceParserException
	 */
	protected UpmResourceQuery getUpmResourceQuery(IRuleExecuteContext ruleExecContext) throws ResourceParserException {
		UpmResourceQuery query = new UpmResourceQuery();
		query.setBusinessComponent(ruleExecContext.getBusinessComponent());
		return query;
	}

	/**
	 * Process Java Resources, Return execute result.
	 * 
	 * @param resources
	 * @return
	 */
	protected abstract IRuleExecuteResult processUpmRules(IRuleExecuteContext ruleExecContext,
			List<UpmResource> resources) throws RuleBaseException;

}
