package com.yonyou.nc.codevalidator.resparser.bpf;

/**
 * 业务活动-业务操作
 * @author mazhqa
 * @since V2.1
 */
public interface IRefOperation {
	
	String getID();
	
	/**
	 * 名称
	 * @return
	 */
	String getName();
	
	/**
	 * 显示名称
	 * @return
	 */
	String getDisplayName();
	
	/**
	 * 引用相关的业务操作接口
	 * @return
	 */
	IOpInterface getRefOptInterface();
	
	/**
	 * 引用相关的业务操作名称
	 * @return
	 */
	IOperation getRefOperation();

}
