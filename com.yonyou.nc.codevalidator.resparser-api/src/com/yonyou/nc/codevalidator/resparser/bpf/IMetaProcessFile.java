package com.yonyou.nc.codevalidator.resparser.bpf;

import java.util.List;

/**
 * 对于业务组件文件，使用该接口进行处理
 * @author mazhqa
 * @since V2.0
 */
public interface IMetaProcessFile {
	
	String getID();
	
	/**
	 * 业务操作集合
	 * @return
	 */
	List<IBusiOperator> getBusiOperators();
	
	/**
	 * 业务活动集合
	 * @return
	 */
	List<IBusiActivity> getBusiActivities();
	
	/**
	 * 业务接口集合
	 * @return
	 */
	List<IOpInterface> getOpInterfaces();
	
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
	 * 所属模块
	 * @return
	 */
	String getOwnModule();
	
	/**
	 * 名称空间
	 * @return
	 */
	String getNameSpace();
	
	/**
	 * 多语资源模块名
	 * @return
	 */
	String getResModuleName();

	/**
	 * 多语资源id
	 * @return
	 */
	String getResId();
	
	/**
	 * 行业
	 * @return
	 */
	String getIndustry();
	
	/**
	 * 扩展标签
	 * @return
	 */
	String getExtendTag();
	
	/**
	 * 是否预加载
	 * @return
	 */
	boolean isPreLoad();
	
	/**
	 * 是否增量开发
	 * @return
	 */
	boolean isIndustryIncrease();
}
