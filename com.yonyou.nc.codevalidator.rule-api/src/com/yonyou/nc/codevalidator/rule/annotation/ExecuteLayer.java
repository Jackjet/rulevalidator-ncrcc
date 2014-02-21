package com.yonyou.nc.codevalidator.rule.annotation;

import com.yonyou.nc.codevalidator.rule.ExecutorContextHelperFactory;
import com.yonyou.nc.codevalidator.rule.SystemRuntimeContext;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 规则执行的层级
 * 
 * @since 6.0
 * @author mazhqa
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
	 * <P>
	 * 应对V5X版本时，根据系统执行参数executeLevelIn5x(true)，支持业务组件的规则可在模块级别执行
	 * @param executeLayer
	 *            - 当前执行单元执行的规则
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
