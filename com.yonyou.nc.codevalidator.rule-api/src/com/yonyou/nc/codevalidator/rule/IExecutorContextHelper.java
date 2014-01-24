package com.yonyou.nc.codevalidator.rule;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ����ִ�������ļ�أ��ɲ鿴����ִ�е�������
 * <p>
 * ������ִ�п�ʼ�ͽ���ʱ����Ҫ�ڸ�helper�н���ע����ͷ�
 * @author mazhqa
 * @since V2.3
 */
public interface IExecutorContextHelper {
	
	/**
	 * ��ʼ���й����ִ�У�ע��runtime������
	 * @param runtimeContext
	 */
	void startRuleExecutor(RuntimeContext runtimeContext) throws RuleBaseException;
	
	/**
	 * ȡ��ע�ᵱǰ�߳�����ִ�еĹ�������
	 * @throws RuleBaseException -��ǰ�߳�ִ�е������Ѿ���ȡ��ʱ���׳��˴���
	 */
	void endRuleExecutor() throws RuleBaseException;
	
	/**
	 * �õ���ǰ�߳�ִ�е�����������
	 * @return
	 * @throws RuleBaseException -��ǰ�߳�ִ�е������Ѿ���ȡ��ʱ���׳��˴���
	 */
	RuntimeContext getCurrentRuntimeContext() throws RuleBaseException;

}
