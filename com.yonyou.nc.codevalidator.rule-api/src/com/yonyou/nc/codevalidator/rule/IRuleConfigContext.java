package com.yonyou.nc.codevalidator.rule;

import java.util.List;

import com.yonyou.nc.codevalidator.rule.vo.RuleExecuteLevel;

/**
 * �����������õ������� 
 * @author mazhqa
 * @since V1.0
 */
public interface IRuleConfigContext {

	/**
	 * �õ��������Ψһ��ʶ��
	 * 
	 * @return
	 */
	String getRuleDefinitionIdentifier();
	
	/**
	 * ����ִ�е�ȫ�����ò�������Щ���ò��������й�����ģ�������ͬһ�ݣ�
	 * 
	 * @param property - ���ò���������
	 * @return
	 */
	String getGlobalProperty(String property);

	/**
	 * �õ�ȫ�����ò��������������б�
	 * @return
	 */
	List<String> getGlobalPropertyNames();
	
	/**
	 * ���������������
	 * @param property
	 * @return
	 */
	String getProperty(String property);

	/**
	 * �õ����������ֵ�б�
	 * @return
	 */
	List<String> getParameterNames();
	
	/**
	 * �õ����������String�ַ�����ʾ
	 * @return
	 */
	String getSpecialParamString();
	
	RuleExecuteLevel getRuleExecuteLevel();
	
	void setRuleExecuteLevel(RuleExecuteLevel ruleExecuteLevel);

}
