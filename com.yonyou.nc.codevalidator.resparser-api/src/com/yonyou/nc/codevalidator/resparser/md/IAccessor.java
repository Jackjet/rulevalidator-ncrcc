package com.yonyou.nc.codevalidator.resparser.md;

/**
 * 用于元数据实体中的访问器和访问器策略
 * @author mazhqa
 * @since V2.5
 */
public interface IAccessor {
	
	/**
	 * 访问器-访问器类型(简写)
	 * @return
	 */
	String getAccessorType();
	
	/**
	 * 访问器-访问器类型（全称）
	 * @return
	 */
	String getAccessorTypeFullName();
	
	/**
	 * 访问器参数-包装类名(如果不是AggVO类型，返回null)
	 * @return
	 */
	String getAccessorWrapperClassName();

}
