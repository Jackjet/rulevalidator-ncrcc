package com.yonyou.nc.codevalidator.rule.annotation;


/**
 * 规则执行的层级
 * 
 * @since 6.0
 * @version 2013-11-19 下午3:33:03
 * @author zhongcha
 */
public enum ExecuteLayer {

	/**
	 * 全局执行，单次执行仅执行一次即可
	 */
	GLOBAL("全局执行", 1),

	/**
	 * 模块级，每个模块都要至少进行一次检查
	 */
	MODULE("模块级", 5),

	/**
	 * 业务组件级
	 */
	BUSICOMP("业务组件级", 10);

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
	 * 判断当前执行规则能否在执行单元上执行该规则
	 * 
	 * @param executeLayer
	 *            - 当前执行单元执行的规则
	 * @return
	 */
	public boolean canExecuteInLayer(ExecuteLayer executeLayer) {
//		if (getLayerValue() < executeLayer.layerValue) {
//			throw new RuleConfigException("规则配置错误，不能在低执行层次配置高执行层次规则");
//		}
		return getLayerValue() == executeLayer.layerValue || (this == BUSICOMP && executeLayer == MODULE);
	}

}
