package com.yonyou.nc.codevalidator.config;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.vo.IRuleCheckConfiguration;

/**
 * ����������ؽӿڣ����ڹ������õĴ洢�Ͷ�ȡ����
 * @author mazhqa
 * @since V2.3
 */
public interface IRuleConfig {
	
	/**
	 * ���������õ������������
	 * 
	 * @param os
	 *            - ����رմ����������Ҫ������ر�
	 * @param ruleCheckConfiguration
	 * @throws RuleBaseException
	 */
	void exportConfig(OutputStream os, IRuleCheckConfiguration ruleCheckConfiguration) throws RuleBaseException;

	/**
	 * ���������е����ݵ��룬���ع�������
	 * 
	 * @param is
	 *            - �������������ڴ˷����б��ر�
	 * @return
	 * @throws RuleBaseException
	 *             - ��������ʱ���ܻ���ָ�ʽ����Ҳ�������ļ����½���ȡ����
	 */
	IRuleCheckConfiguration loadConfiguration(InputStream is) throws RuleBaseException;

	/**
	 * ������Ӧ��ҵ������£��������ִ�е�context
     * 
	 * @param businessComponent
	 * @return
	 * @throws RuleParserException - ���� �����ļ���ȡ����ʱ�׳����쳣
	 */
	IRuleCheckConfiguration parseRule(BusinessComponent businessComponent) throws RuleBaseException;
	
	/**
	 * �õ����������ļ�·��
	 * 
	 * @param businessComponent
	 * @return
	 */
	String getRuleConfigFilePath(BusinessComponent businessComponent);

	/**
	 * �õ����������ļ�����ڱ�����project��·��
	 * 
	 * @return
	 */
	String getRuleConfigRelativePath();

	/**
	 * ��ҵ������£����������ļ������ƣ�Ӧ����ҵ�����/rulecase�ļ�����
	 * 
	 * @return
	 */
	String getRuleConfigFileName();

	/**
	 * �����ļ��г�ʼ������
	 * 
	 * @param configFolder
	 * @throws RuleBaseException
	 */
	void configFolderInitialize(File configFolder) throws RuleBaseException;
	
	/**
	 * �����ļ���ʼ�����������ڳ�ʼ���ļ��ǽ��������ļ���ʼ���ݵ�д��
	 * @param configFile
	 * @throws RuleBaseException
	 */
	void configFileInitialize(File configFile) throws RuleBaseException;
}
