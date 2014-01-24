package com.yonyou.nc.codevalidator.sdk.rule;

import java.io.InputStream;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.sdk.code.JavaCodeFeature;

/**
 * ������Դ����ķ��ʴ�����Ҫͨ������������
 * @author mazhqa
 * @since V1.0
 * @version V2.6.7 �������л���ڿո������
 */
public interface IClassLoaderUtils {

	/**
	 * ��ͼ�ڵ�ǰ�����¼����ض�class
	 *  
	 * @param projectName
	 *            - current ProjectName from businessComponent.mdeProjectName
	 * @param className
	 * @return
	 * @throws RuleClassLoadException
	 */
	Class<?> loadClass(String projectName, String className) throws RuleClassLoadException;

	/**
	 * �ж��ڵ�ǰ��class�Ƿ�ΪparentClass������
	 * 
	 * @param className
	 * @param parentClassName - ����ǽӿڣ�����false
	 * @return
	 * @throws RuleClassLoadException
	 */
	boolean isParentClass(String projectName, String className, String parentClassName) throws RuleClassLoadException;

	/**
	 * �жϵ�ǰ�����Ƿ�ӽӿ�������
	 * 
	 * @param projectName
	 * @param className
	 * @param interfaceName
	 *            ������ʵ���ֻ࣬�ܽӿ�; ������ǽӿڣ�����false
	 * @return
	 * @throws RuleClassLoadException
	 */
	boolean isImplementedInterface(String projectName, String className, String interfaceName)
			throws RuleClassLoadException;

	/**
	 * �жϵ�ǰ�����Ƿ��ָ������������������parentClassName�Ƿ�Ϊ�ӿڻ������
	 * 
	 * @param projectName
	 * @param className
	 * @param parentClassName
	 * @return
	 * @throws RuleClassLoadException
	 */
	boolean isExtendsParentClass(String projectName, String className, String parentClassName)
			throws RuleClassLoadException;

	/**
	 * ���ݹ������ƺͶ�Ӧ����Դ·���õ���Ӧ����Դ������������Ҫ����������йر�;
	 * �����Դ�����ڣ�������Ϊnull
	 * @param projectName
	 * @param resourcePath
	 * @return
	 * @throws RuleClassLoadException ����Classloaderδ���ص�
	 */
	InputStream getResourceStream(String projectName, String resourcePath) throws RuleClassLoadException;
	
	/**
	 * ����ҵ�������class�õ���Ӧ��Code Feature����������Jar���л��������.class
	 * <p>
	 * ע���ڻ�ȡû��ҵ�������ģ���ϣ����������
	 * @param businessComponent
	 * @param className
	 * @return
	 * @throws RuleClassLoadException
	 */
	JavaCodeFeature getCodeFeature(BusinessComponent businessComponent, String className) throws RuleClassLoadException;

}
