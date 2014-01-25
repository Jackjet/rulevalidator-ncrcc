package com.yonyou.nc.codevalidator.runtime.core;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.yonyou.nc.codevalidator.rule.IExecutorContextHelper;
import com.yonyou.nc.codevalidator.rule.RuntimeContext;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ����ִ�������ļ�أ��ɲ鿴����ִ�е�������
 * <p>
 * ������ִ�п�ʼ�ͽ���ʱ����Ҫ�ڸ�helper�н���ע����ͷ�
 * @author mazhqa
 * @since V2.1
 */
public final class RuleExecutorContextHelper implements IExecutorContextHelper{
	
	private Map<String, RuntimeContext> taskId2RuntimeContextMap = new ConcurrentHashMap<String, RuntimeContext>();
	private ThreadLocal<String> taskIdTl = new ThreadLocal<String>();
	
	public RuleExecutorContextHelper() {
		
	}
	
	/**
	 * ��ʼ���й����ִ�У�ע��runtime������
	 * @param runtimeContext
	 */
	public void startRuleExecutor(RuntimeContext runtimeContext) {
		String taskId = UUID.randomUUID().toString();
		taskIdTl.set(taskId);
		taskId2RuntimeContextMap.put(taskId, runtimeContext);
	}

	/**
	 * ȡ��ע�ᵱǰ�߳�����ִ�еĹ�������
	 * @throws RuleBaseException -��ǰ�߳�ִ�е������Ѿ���ȡ��ʱ���׳��˴���
	 */
	public void endRuleExecutor() throws RuleBaseException {
		String taskId = taskIdTl.get();
		if(taskId == null) {
			throw new RuleBaseException("��ǰ�߳�ִ�е������Ѿ�ȡ���������ٴ�ȡ��!");
		}
		taskIdTl.remove();
		taskId2RuntimeContextMap.remove(taskId);
	}
	
	/**
	 * �õ���ǰ�߳�ִ�е�����������
	 * @return
	 * @throws RuleBaseException -��ǰ�߳�ִ�е������Ѿ���ȡ��ʱ���׳��˴���
	 */
	public RuntimeContext getCurrentRuntimeContext() throws RuleBaseException {
		String taskId = taskIdTl.get();
		if(taskId == null) {
			throw new RuleBaseException("��ǰ�߳�ִ�е������Ѿ�ȡ�����޷��������ִ��������!");
		}
		return taskId2RuntimeContextMap.get(taskId);
	}
}
