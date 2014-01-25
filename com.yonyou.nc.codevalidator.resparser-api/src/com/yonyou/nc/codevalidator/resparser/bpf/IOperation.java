package com.yonyou.nc.codevalidator.resparser.bpf;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.md.IType;

/**
 * 业务操作接口-方法定义
 * @author mazhqa
 * @since V2.1
 */
public interface IOperation {
	
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
	 * 描述
	 * @return
	 */
	String getDescription();
	
	/**
	 * 类型样式
	 * @return
	 */
	String getTypeStyle();
	
	/**
	 * 可见性
	 * @return
	 */
	String getVisibility();
	
	/**
	 * 异常
	 * @return
	 */
	String getMethodException();
	
	/**
	 * 返回类型
	 * @return
	 */
	IType getReturnType();
	
	/**
	 * 业务操作
	 * @return
	 */
	boolean isBusiActivity();
	
	/**
	 * 返回聚合VO
	 * @return
	 */
	boolean isAggVOReturn();
	
	/**
	 * 事务属性
	 * @return
	 */
	String getTransKind();
	
	/**
	 * 参数定义
	 * @return
	 */
	List<IParameter> getParameters();

}
