package com.yonyou.nc.codevalidator.rule.executor;

import com.yonyou.nc.codevalidator.rule.RuntimeContext;
import com.yonyou.nc.codevalidator.rule.SessionRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ����ִ����֤�Ľӿڷ���
 * @author mazhqa
 * @since V2.7
 */
public interface IRuleValidatorExecutor {
	
	/**
	 * ִ�й�����֤ ִ��, ����ʱ����֪ͨ����
	 * @param runtimeContext
	 * @param validatorListener
	 * @return
	 * @throws RuleBaseException
	 */
	public SessionRuleExecuteResult executeValidator(RuntimeContext runtimeContext, IValidatorListener validatorListener) throws RuleBaseException;

}
