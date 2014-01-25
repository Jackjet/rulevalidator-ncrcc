package com.yonyou.nc.codevalidator.rule;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 任务执行上下文监控，可查看所有执行的上下文
 * <p>
 * 任务在执行开始和结束时都需要在该helper中进行注册和释放
 * @author mazhqa
 * @since V2.3
 */
public interface IExecutorContextHelper {
	
	/**
	 * 开始进行规则的执行，注册runtime上下文
	 * @param runtimeContext
	 */
	void startRuleExecutor(RuntimeContext runtimeContext) throws RuleBaseException;
	
	/**
	 * 取消注册当前线程正在执行的规则任务
	 * @throws RuleBaseException -当前线程执行的任务已经被取消时，抛出此错误
	 */
	void endRuleExecutor() throws RuleBaseException;
	
	/**
	 * 得到当前线程执行的任务上下文
	 * @return
	 * @throws RuleBaseException -当前线程执行的任务已经被取消时，抛出此错误
	 */
	RuntimeContext getCurrentRuntimeContext() throws RuleBaseException;

}
