package com.yonyou.nc.codevalidator.rule;

/**
 * 规则执行上下文
 * @author mazhqa
 * @since V1.0
 */
public interface RuntimeContext {

	/**
	 * 当前规则是否在插件环境下执行
	 * @return
	 */
	@Deprecated
	boolean isRunInPlug();

	/**
	 * 得到当前执行上下文NCHOME路径
	 * @return
	 */
	String getNcHome();

	/**
	 * 当前执行的数据源
	 * @return
	 */
	String getDataSource();

	/**
	 * 执行检查顶层的业务组件
	 * @return
	 */
	BusinessComponent getBusinessComponents();
	
	/**
	 * 当前正在执行的业务组件
	 * @return
	 */
	BusinessComponent getCurrentExecuteBusinessComponent();
	
	void setCurrentExecuteBusinessComponent(BusinessComponent businessComponent);
	
	SystemRuntimeContext getSystemRuntimeContext();
	
	/**
	 * 当前执行任务id，用来设置文件夹名称，每次执行单独的执行文件夹
	 * @return
	 */
	String getTaskExecuteId();
	
	String getProjectName();
	
}
