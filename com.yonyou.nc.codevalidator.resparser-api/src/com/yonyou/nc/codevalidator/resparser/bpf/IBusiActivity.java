package com.yonyou.nc.codevalidator.resparser.bpf;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.md.IType;

/**
 * 业务活动
 * @author mazhqa
 * @since V2.3
 */
public interface IBusiActivity {
	
	String getVersion();
	
	/**
	 * 多语资源id
	 * @return
	 */
	String getResId();
	
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
	 * 扩展标签
	 * @return
	 */
	String getExtendTag();
	
	/**
	 * 所属实体
	 * @return
	 */
	IType getOwnType();
	
	/**
	 * 是否授权
	 * @return
	 */
	boolean isAuthorization();
	
	/**
	 * 是否服务
	 * @return
	 */
	boolean isService();
	
	/**
	 * 业务活动对应的业务操作集合
	 * @return
	 */
	List<IBusiOperator> getBusiOperators();

}
