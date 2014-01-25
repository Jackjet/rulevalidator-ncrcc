package com.yonyou.nc.codevalidator.persistence.dao;

/**
 * 用于在规则系统启动时初始化设置规则的接口
 * @author mazhqa
 * @since V2.7
 */
public interface IRuleDefinitionInitializer {
	
	/**
	 * 系统初始化操作，先会对当前库中的数据进行清理，然后重新放入最新的规则
	 * @throws RuleDaoException
	 */
	void initialize() throws RuleDaoException;

}
