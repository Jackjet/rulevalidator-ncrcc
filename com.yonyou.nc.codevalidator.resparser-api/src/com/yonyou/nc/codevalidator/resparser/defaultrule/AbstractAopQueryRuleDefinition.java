package com.yonyou.nc.codevalidator.resparser.defaultrule;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.AopResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.resource.AopResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.impl.AbstractRuleDefinition;

/**
 * 抽象的aop查询规则定义类，建议查询aop的规则都从该类中派生
 * @author mazhqa
 * @since V2.1
 */
public abstract class AbstractAopQueryRuleDefinition extends AbstractRuleDefinition {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		AopResourceQuery aopResourceQuery = getAopResourceQuery(ruleExecContext);
		List<AopResource> upmResources = ResourceManagerFacade.getResource(aopResourceQuery);
		return processAopRules(ruleExecContext, upmResources);
	}

	/**
	 * 得到aop资源查询，用户可覆盖此方法来达到修改查询参数的目的
	 * @param ruleExecuteContext
	 * @return
	 */
	protected AopResourceQuery getAopResourceQuery(IRuleExecuteContext ruleExecuteContext) throws RuleBaseException{
		AopResourceQuery aopResourceQuery = new AopResourceQuery();
		aopResourceQuery.setBusinessComponent(ruleExecuteContext.getBusinessComponent());
		return aopResourceQuery;
	}
	
	/**
	 * 处理得到的aop资源并得出处理结果
	 * @param ruleExecuteContext
	 * @param aopResources
	 * @return
	 */
	protected abstract IRuleExecuteResult processAopRules(IRuleExecuteContext ruleExecuteContext, List<AopResource> aopResources) throws RuleBaseException;
	
}
