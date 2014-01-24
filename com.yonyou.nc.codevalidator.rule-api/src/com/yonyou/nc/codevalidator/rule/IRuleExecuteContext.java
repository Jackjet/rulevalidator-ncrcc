package com.yonyou.nc.codevalidator.rule;

/**
 * 规则执行的上下文，本上下文可能与任务配置有所不同
 * <p>
 * 在运行态，私有属性的获取可能会覆盖掉共有属性
 * @author mazhqa
 * @since V1.0
 */
public interface IRuleExecuteContext {

	/**
	 * 规则的执行状态时，参数的实际内容
	 * @param parameter
	 * @return
	 */
	String getParameter(String parameter);

	/**
	 * 根据参数值获得对应的参数数组值，数组内容以, 隔开
	 * @param parameter
	 * @return
	 */
	String[] getParameterArray(String parameter);

	/**
	 * 得到当前执行的业务组件
	 * @return
	 */
	BusinessComponent getBusinessComponent();
	
	/**
	 * 在任务执行时，设置执行上下文
	 * @param runtimeContext
	 */
	void setRuntimeContext(RuntimeContext runtimeContext);
	
	/**
	 * 仅在任务执行过程中能够获得该任务执行上下文
	 * @return
	 */
	RuntimeContext getRuntimeContext();
	
	/**
	 * 得到规则配置态的相关内容，任务运行态的相关参数都是从该配置上下文中获得
	 * @return
	 */
	IRuleConfigContext getRuleConfigContext();

}
