package com.yonyou.nc.codevalidator.runtime;

/**
 * ���������ִ̬�г���ӿ�
 * 
 * @author mazhqa
 * @since V2.7
 */
public interface IRuleRuntimeExecutor {
	//
	// /**
	// * ִ�й�����֤
	// *
	// * @param ncHome
	// * - ָ����NCHome��ַ
	// * @param codePath
	// * - nc mde����·��
	// * @param dataSourceName
	// * - ����Դ����
	// */
	// @Deprecated
	// void execute(String ncHome, String codePath, String dataSourceName);

	/**
	 * ִ�й�����֤���Զ�����򼶱�
	 * 
	 * @param ncHome
	 * @param codePath
	 * @param dataSourceName
	 * @param executePerid
	 * @param productCode
	 */
	void execute(String ncHome, String codePath, String dataSourceName, String executePeriod, String productCode);

}
