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
 * �����aop��ѯ�������࣬�����ѯaop�Ĺ��򶼴Ӹ���������
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
	 * �õ�aop��Դ��ѯ���û��ɸ��Ǵ˷������ﵽ�޸Ĳ�ѯ������Ŀ��
	 * @param ruleExecuteContext
	 * @return
	 */
	protected AopResourceQuery getAopResourceQuery(IRuleExecuteContext ruleExecuteContext) throws RuleBaseException{
		AopResourceQuery aopResourceQuery = new AopResourceQuery();
		aopResourceQuery.setBusinessComponent(ruleExecuteContext.getBusinessComponent());
		return aopResourceQuery;
	}
	
	/**
	 * ����õ���aop��Դ���ó�������
	 * @param ruleExecuteContext
	 * @param aopResources
	 * @return
	 */
	protected abstract IRuleExecuteResult processAopRules(IRuleExecuteContext ruleExecuteContext, List<AopResource> aopResources) throws RuleBaseException;
	
}
