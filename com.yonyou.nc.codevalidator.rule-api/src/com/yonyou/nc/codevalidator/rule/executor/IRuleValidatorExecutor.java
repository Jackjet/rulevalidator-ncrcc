package com.yonyou.nc.codevalidator.rule.executor;

import com.yonyou.nc.codevalidator.rule.RuntimeContext;
import com.yonyou.nc.codevalidator.rule.SessionRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 规则执行验证的接口服务
 * @author mazhqa
 * @since V2.7
 */
public interface IRuleValidatorExecutor {
	
	/**
	 * 执行规则验证 执行, 并适时进行通知操作
	 * @param runtimeContext
	 * @param validatorListener
	 * @return
	 * @throws RuleBaseException
	 */
	public SessionRuleExecuteResult executeValidator(RuntimeContext runtimeContext, IValidatorListener validatorListener) throws RuleBaseException;

}
