package com.yonyou.nc.codevalidator.rule.annotation;


/**
 * ����ִ�еĲ㼶
 * 
 * @since 6.0
 * @version 2013-11-19 ����3:33:03
 * @author zhongcha
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
	 * 
	 * @param executeLayer
	 *            - ��ǰִ�е�Ԫִ�еĹ���
	 * @return
	 */
	public boolean canExecuteInLayer(ExecuteLayer executeLayer) {
//		if (getLayerValue() < executeLayer.layerValue) {
//			throw new RuleConfigException("�������ô��󣬲����ڵ�ִ�в�����ø�ִ�в�ι���");
//		}
		return getLayerValue() == executeLayer.layerValue || (this == BUSICOMP && executeLayer == MODULE);
	}

}
