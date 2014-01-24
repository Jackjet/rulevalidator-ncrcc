package com.yonyou.nc.codevalidator.resparser.md;

import java.util.List;

/**
 * 每个元数据文件(bmf)都解析成该对象
 * <p>
 * @author mazhqa
 * @since V1.0
 */
public interface IMetadataFile {

	/**
	 * 得到元数据文件的主实体，如果元数据文件都是接口，可能返回null
	 * @return
	 */
	IEntity getMainEntity();
	
	/**
	 * 得到元数据文件中的所有元数据实体
	 * @return
	 */
	List<IEntity> getAllEntities();
	
	/**
	 * 命名空间
	 * @return
	 */
	String getNamespace();
	
	/**
	 * 代码风格
	 * @return
	 */
	String getCodeStyle();
	
	/**
	 * 多语资源id
	 * @return
	 */
	String getResId();
	
	/**
	 * 多语资源模块名称
	 * @return
	 */
	String getResModuleName();
	
	/**
	 * 名称
	 * @return
	 */
	String getName();
	
	/**
	 * 是否预加载
	 * @return
	 */
	boolean isPreLoad();
	
	/**
	 * 所属模块
	 * @return
	 */
	String getOwnModule();
	
	/**
	 * 显示名称
	 * @return
	 */
	String getDisplayName();
	
	/**
	 * 行业
	 * @return
	 */
	String getHyName();
	
	/**
	 * 是否增量开发模型
	 * @return
	 */
	boolean isIncrementDevelopMode();
	
	/**
	 * 扩展标签
	 * @return
	 */
	String getExtendTag();
	
	/**
	 * 该元数据文件是否为单据(扩展标签BDMODE判断)
	 * @return
	 */
	boolean isBill();
	
	/**
	 * 该元数据文件是否为档案(扩展标签BDMODE判断)
	 * @return
	 */
	boolean isDoc();
	
	/**
	 * 有些元数据文件中只包含一些接口和枚举，而没有主实体
	 * @return
	 */
	boolean containsMainEntity();
}
