package com.yonyou.nc.codevalidator.resparser.bpf;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.md.IType;

/**
 * 业务操作
 * @author mazhqa
 * @since V2.1
 */
public interface IBusiOperator {
	
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
	 * 多语资源ID
	 * @return
	 */
	String getResId();
	
	/**
	 * 扩展标签
	 * @return
	 */
	String getExtendTag();
	
	/**
	 * 描述
	 * @return
	 */
	String getDescription();
	
	/**
	 * 是否授权
	 * @return
	 */
	boolean isAuthorization();
	
	/**
	 * 是否业务活动
	 * @return
	 */
	boolean isBusiActivity();
	
	/**
	 * 是否日志
	 * @return
	 */
	boolean isNeedLog();
	
	/**
	 * 日志类型
	 * @return
	 */
	String getLogType();
	
	/**
	 * 所属实体
	 * @return
	 */
	IType getOwnType();
	
	/**
	 * 引用操作集合
	 * @return
	 */
	List<IRefOperation> getRefOperations();

}
