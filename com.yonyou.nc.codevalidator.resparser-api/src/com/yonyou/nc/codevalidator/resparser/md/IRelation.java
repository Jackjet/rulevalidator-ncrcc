package com.yonyou.nc.codevalidator.resparser.md;

/**
 * 目前只加入Relation，即关联类型的联系，
 * 之后可考虑加入所有Connection
 * @author zhangwch1
 *
 */
public interface IRelation {
	
	/**
	 * 获取关联的源
	 * @return
	 */
	IEntity getTarget();
	
	/**
	 * 获取关联的目标
	 * @return
	 */
	IEntity getSource();
	
	/**
	 * 获取关联源的属性
	 * @return
	 */
	IAttribute getSrcAttribute();
}
