package com.yonyou.nc.codevalidator.resparser.md;

import java.util.List;
import java.util.Map;

/**
 * 元数据-实体
 * @author mazhqa
 *
 */
public interface IEntity {
	
	/**
	 * 得到实体ID 
	 * @return
	 */
	String getId();
	
	/**
	 * 得到实体显示名称
	 * @return
	 */
	String getDisplayName();
	
	/**
	 * 实体的类名称
	 * @return
	 */
	String getFullClassName();
	
	/**
	 * 命名空间+"."+实体名称
	 * @return
	 */
	String getFullName();
	
	/**
	 * 实体的扩展标签
	 * @return
	 */
	String getExtendTag();
	
	/**
	 * 得到实体中的所有属性
	 * @return
	 */
	List<IAttribute> getAttributes();
	
	/**
	 * 得到实体中业务接口
	 * @return
	 */
	List<IBusiInterface> getBusiInterfaces();

//	/**
//	 * 得到业务组件属性映射对应的attributes
//	 * @return
//	 */
//	Map<String, IAttribute[]> getBusiattrAttrExtendMap();
	
	/**
	 * 根据业务接口名得到属性值列表
	 * @param busiInterfaceName
	 * @return 属性名称对应属性值的映射
	 */
	Map<String, IAttribute> getBusiInterfaceAttributes(String busiInterfaceName);
	
	/**
	 * 获取实体的缺省表名
	 */
	String getTableName();
	
	/**
	 * 获取实体主属性
	 */
	IAttribute getKeyAttribute();
	
	/**
	 * 获取以本实体为源的关联型（IRelation）联系（Connection）
	 */
	List<IRelation> getSourceRelations();
	
	/**
	 * 获取以本实体为目标的关联型（IRelation）联系（Connection）
	 */
	List<IRelation> getTargetRelations();
	
	/**
	 * 得到当前实体-参照的值
	 * @return
	 */
	List<IReference> getReferences();
	
	/**
	 * 得到实体的访问器
	 * @return
	 */
	IAccessor getAccessor();
	
	/**
	 * 得到聚合关系的target连接
	 * @return
	 */
	List<IEntityTargetConnection> getAggreConnections();
}
