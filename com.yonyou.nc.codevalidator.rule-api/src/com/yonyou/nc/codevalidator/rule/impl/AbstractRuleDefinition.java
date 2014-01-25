package com.yonyou.nc.codevalidator.rule.impl;

import com.yonyou.nc.codevalidator.rule.ExecutorContextHelperFactory;
import com.yonyou.nc.codevalidator.rule.IRuleDefinition;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.SystemRuntimeContext;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionsReader;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ���������ʵ�֣�����ʹ�ö��ֲ�ͬ��Դ�Ĳ���Ӹ���������
 * <p>
 * �����а�����ͳһ�Ĵ���ִ�м��������߼�
 * 
 * @author mazhqa
 * @since V1.0
 */
public abstract class AbstractRuleDefinition implements IRuleDefinition {

	@Override
	public String getIdentifier() {
		return this.getClass().getName();
	}

	@Override
	public String[] getDependentCreator() {
		return null;
	}

	public abstract IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException;

	/* (non-Javadoc)
	 * @see com.yonyou.nc.codevalidator.rule.IRuleDefinition#actualExecute(com.yonyou.nc.codevalidator.rule.IRuleExecuteContext)
	 */
	public final IRuleExecuteResult actualExecute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		SystemRuntimeContext systemRuntimeContext = ExecutorContextHelperFactory.getExecutorContextHelper().getCurrentRuntimeContext().getSystemRuntimeContext();
		ExecutePeriod currentExecutePeriod = systemRuntimeContext.getExecutePeriod();
		RuleDefinitionAnnotationVO ruleDefinitionVO = RuleDefinitionsReader.getInstance().getRuleDefinitionVO(getIdentifier());
		ExecutePeriod ruleExecutePeriod = ruleDefinitionVO.getExecutePeriod();
		if(ruleExecutePeriod.canExecuteInEnv(currentExecutePeriod)) {
			return execute(ruleExecContext);
		}
		return new IgnoreRuleExecuteResult(getIdentifier(), ruleExecutePeriod, currentExecutePeriod);
	}

}
