package com.yonyou.nc.codevalidator.rule.annotation;

import com.yonyou.nc.codevalidator.rule.ExecutorContextHelperFactory;
import com.yonyou.nc.codevalidator.rule.SystemRuntimeContext;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ����ִ�еĲ㼶
 * 
 * @since 6.0
 * @author mazhqa
 */
public enum ExecuteLayer {

	/**
	 * ȫ��ִ�У�����ִ�н�ִ��һ�μ���
	 */
	GLOBAL("ȫ��ִ��", 1),

	/**
	 * ģ�鼶��ÿ��ģ�鶼Ҫ���ٽ���һ�μ��
	 */
	MODULE("ģ�鼶", 5),

	/**
	 * ҵ�������
	 */
	BUSICOMP("ҵ�������", 10);

	private String name;
	private int layerValue;

	private ExecuteLayer(String name, int layerValue) {
		this.name = name;
		this.layerValue = layerValue;
	}

	public String getName() {
		return name;
	}

	public int getLayerValue() {
		return layerValue;
	}

	/**
	 * �жϵ�ǰִ�й����ܷ���ִ�е�Ԫ��ִ�иù���
	 * <P>
	 * Ӧ��V5X�汾ʱ������ϵͳִ�в���executeLevelIn5x(true)��֧��ҵ������Ĺ������ģ�鼶��ִ��
	 * @param executeLayer
	 *            - ��ǰִ�е�Ԫִ�еĹ���
	 * @return
	 * @throws RuleBaseException 
	 */
	public boolean canExecuteInLayer(ExecuteLayer executeLayer) throws RuleBaseException {
		if(getLayerValue() == executeLayer.layerValue) {
			return true;
		}
		SystemRuntimeContext systemRuntimeContext = ExecutorContextHelperFactory.getExecutorContextHelper().getCurrentRuntimeContext().getSystemRuntimeContext();
		boolean executeLevelIn5x = systemRuntimeContext.isExecuteLevelIn5x();
		return executeLevelIn5x && this == BUSICOMP && executeLayer == MODULE;
	}

}
