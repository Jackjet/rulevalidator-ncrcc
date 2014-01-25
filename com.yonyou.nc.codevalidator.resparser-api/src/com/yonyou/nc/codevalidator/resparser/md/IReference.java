package com.yonyou.nc.codevalidator.resparser.md;

/**
 * 元数据-实体-参照
 * @author mazhqa
 * @since V2.3
 */
public interface IReference {
	
	/**
	 * 缺省
	 * @return
	 */
	boolean isDefault();
	
	/**
	 * 名称
	 * @return
	 */
	String getName();
	
	/**
	 * 引用实体
	 * @return
	 */
	IEntity getEntity();

}
