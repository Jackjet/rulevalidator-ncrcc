package com.yonyou.nc.codevalidator.runtime.core;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.yonyou.nc.codevalidator.rule.IExecutorContextHelper;
import com.yonyou.nc.codevalidator.rule.RuntimeContext;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 任务执行上下文监控，可查看所有执行的上下文
 * <p>
 * 任务在执行开始和结束时都需要在该helper中进行注册和释放
 * @author mazhqa
 * @since V2.1
 */
public final class RuleExecutorContextHelper implements IExecutorContextHelper{
	
	private Map<String, RuntimeContext> taskId2RuntimeContextMap = new ConcurrentHashMap<String, RuntimeContext>();
	private ThreadLocal<String> taskIdTl = new ThreadLocal<String>();
	
	public RuleExecutorContextHelper() {
		
	}
	
	/**
	 * 开始进行规则的执行，注册runtime上下文
	 * @param runtimeContext
	 */
	public void startRuleExecutor(RuntimeContext runtimeContext) {
		String taskId = UUID.randomUUID().toString();
		taskIdTl.set(taskId);
		taskId2RuntimeContextMap.put(taskId, runtimeContext);
	}

	/**
	 * 取消注册当前线程正在执行的规则任务
	 * @throws RuleBaseException -当前线程执行的任务已经被取消时，抛出此错误
	 */
	public void endRuleExecutor() throws RuleBaseException {
		String taskId = taskIdTl.get();
		if(taskId == null) {
			throw new RuleBaseException("当前线程执行的任务已经取消，不能再次取消!");
		}
		taskIdTl.remove();
		taskId2RuntimeContextMap.remove(taskId);
	}
	
	/**
	 * 得到当前线程执行的任务上下文
	 * @return
	 * @throws RuleBaseException -当前线程执行的任务已经被取消时，抛出此错误
	 */
	public RuntimeContext getCurrentRuntimeContext() throws RuleBaseException {
		String taskId = taskIdTl.get();
		if(taskId == null) {
			throw new RuleBaseException("当前线程执行的任务已经取消，无法获得任务执行上下文!");
		}
		return taskId2RuntimeContextMap.get(taskId);
	}
}
