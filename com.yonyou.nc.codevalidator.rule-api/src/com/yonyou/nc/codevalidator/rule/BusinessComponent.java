package com.yonyou.nc.codevalidator.rule;

import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;

/**
 * ִ�й���������ȣ����ݲ�ִͬ�л�����ִ�е�������ʵ�ֻ���������
 * 
 * ע�⣺Ӧ�ý���������ع�(ExecuteUnit)����ʹ�����ڶ࣬�޷������޸�
 * @author mazhqa
 * @since V2.7
 */
public interface BusinessComponent {

	/**
	 * ִ�е�Ԫ��Ӧ�Ĳ�Ʒ
	 * @return
	 */
	String getProduct();
	/**
	 * ����ģ�����ƣ�ȫ�ֹ���ִ�в����������ƣ��������Ǵ�module.xml�ж�ȡ�ģ�
	 * @return
	 */
	String getModule();

	/**
	 * ����ҵ��������ƣ�ȫ��ִ�й����Լ�ģ��ִ�й��򲻰��������ƣ�
	 * @return
	 */
	String getBusinessComp();

	/**
	 * ��������·��
	 * @return
	 */
	String getProjectPath();

	/**
	 * ���̵����ƣ�����ClassLoader��ִ�й�����֤ʱ��ȥ��������������������ع���
	 * @return
	 */
	String getProjectName();
	
//	void setProjectName(String projectName);

	/**
	 * ҵ���������ʾ���ƣ����Ϊȫ����ʾglobal���������ȫ�֣���ʾmodule-busicomp
	 * @return
	 */
	String getDisplayBusiCompName();

	/**
	 * ִ�е�Ԫ���ڶ����ļ���
	 * @return
	 */
	String getBusinessComponentPath();

	/**
	 * �õ��������·��
	 * @return
	 */
	String getCodePath();

	/**
	 * ���ݴ������ڼ���õ�����·��
	 * 
	 * @param codeFolder
	 * @return
	 */
	String getCodePath(String codeFolder);
	
	/**
	 * �õ��ù����ִ�в��
	 * @return
	 */
	ExecuteLayer getExecuteLayer();

//	/**
//	 * �õ���Դ�ļ���·��
//	 * @return
//	 */
//	String getResourcePath();
//
//	/**
//	 * �õ�Ԫ�����ļ�������·��
//	 * @return
//	 */
//	String getMetadataPath();

}
