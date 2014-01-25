package com.yonyou.nc.codevalidator.resparser.bpf;

import com.yonyou.nc.codevalidator.resparser.md.IType;

/**
 * 业务操作-方法-参数定义
 * @author mazhqa
 * @since V2.1
 */
public interface IParameter {
	
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
	 * 是否聚合vo
	 * @return
	 */
	boolean isAggVO();
	
	/**
	 * 帮助
	 * @return
	 */
	String getHelp();
	
	/**
	 * 类型样式
	 * @return
	 */
	String getTypeStyle();

	/**
	 * 参数类型
	 * @return
	 */
	IType getParaType();
	
	/**
	 * 自定义类名
	 * @return
	 */
	String getParamDefClassName();
}
