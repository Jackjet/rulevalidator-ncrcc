package com.yonyou.nc.codevalidator.resparser.bpf;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.md.IType;

/**
 * 业务操作-接口
 * @author mazhqa
 * @since V2.1
 */
public interface IOpInterface {
	
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
	 * 版本
	 * @return
	 */
	String getVersion();
	
	/**
	 * 多语资源id
	 * @return
	 */
	String getResId();
	
	/**
	 * 对应的接口类名fullClassName
	 * @return
	 */
	String getInterfaceName();

	/**
	 * 对应的实现类名implClsName
	 * @return
	 */
	String getImplementationClassName();
	
	/**
	 * 所属实体
	 * @return
	 */
	IType getOwnType();
	
	/**
	 * 扩展标签
	 * @return
	 */
	String getExtendTag();
	
	/**
	 * 是否单例
	 * @return
	 */
	boolean isSingleton();
	
	/**
	 * 是否业务活动
	 * @return
	 */
	boolean isBusiActivity();
	
	/**
	 * 是否业务操作
	 * @return
	 */
	boolean isBusiOperation();
	
	/**
	 * 是否授权
	 * @return
	 */
	boolean isAuthorization();
	
	/**
	 * 是否远程
	 * @return
	 */
	boolean isRemote();
	
	/**
	 * 业务操作方法集合
	 * @return
	 */
	List<IOperation> getOperations();
	
}
