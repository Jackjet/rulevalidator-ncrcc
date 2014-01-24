package com.yonyou.nc.codevalidator.resparser.temp;

import java.sql.Connection;

import com.yonyou.nc.codevalidator.rule.IIdentifier;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 用于临时表资源创建的统一接口
 * @author mazhqa
 * @since V2.3
 */
public interface ITempTableResourceCreator extends IIdentifier{
	
	/**
	 * 在规则执行开始时，进行临时资源的准备操作
	 * @param connection
	 * @throws RuleBaseException
	 */
	void createTempResources(Connection connection) throws RuleBaseException;
	
	/**
	 * 规则执行结束进行资源的清理操作
	 * @throws RuleBaseException
	 */
	void cleanUp() throws RuleBaseException;
	
}
