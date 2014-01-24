package com.yonyou.nc.codevalidator.rule.executor;

import java.util.Map;

import com.yonyou.nc.codevalidator.rule.IRuleDefinition;

/**
 * ��ԭ�еĲ�ѯ����ִ�����ľ�̬�����Ϊ����ʽ����ʵ�ָ��ӷ���OSGi�ı�׼
 * @author mazhqa
 * @since V2.7
 */
public interface IRuleExecutorFactory {
	
	/**
	 * �����ʶ�����������ӳ��
	 * @return
	 */
	Map<String, IRuleDefinition> getIdentifierToDefinitionMap();
	

}
