package com.yonyou.nc.codevalidator.resparser.temp;

import java.sql.Connection;

import com.yonyou.nc.codevalidator.rule.IIdentifier;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ������ʱ����Դ������ͳһ�ӿ�
 * @author mazhqa
 * @since V2.3
 */
public interface ITempTableResourceCreator extends IIdentifier{
	
	/**
	 * �ڹ���ִ�п�ʼʱ��������ʱ��Դ��׼������
	 * @param connection
	 * @throws RuleBaseException
	 */
	void createTempResources(Connection connection) throws RuleBaseException;
	
	/**
	 * ����ִ�н���������Դ���������
	 * @throws RuleBaseException
	 */
	void cleanUp() throws RuleBaseException;
	
}
