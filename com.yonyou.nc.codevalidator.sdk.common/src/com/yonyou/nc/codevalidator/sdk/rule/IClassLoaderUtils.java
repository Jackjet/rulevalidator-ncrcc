package com.yonyou.nc.codevalidator.sdk.rule;

import java.io.InputStream;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.sdk.code.JavaCodeFeature;

/**
 * 对于资源和类的访问处理都需要通过此类来进行
 * @author mazhqa
 * @since V1.0
 * @version V2.6.7 处理类中会存在空格的问题
 */
public interface IClassLoaderUtils {

	/**
	 * 试图在当前工程下加载特定class
	 *  
	 * @param projectName
	 *            - current ProjectName from businessComponent.mdeProjectName
	 * @param className
	 * @return
	 * @throws RuleClassLoadException
	 */
	Class<?> loadClass(String projectName, String className) throws RuleClassLoadException;

	/**
	 * 判断在当前的class是否为parentClass的子类
	 * 
	 * @param className
	 * @param parentClassName - 如果是接口，返回false
	 * @return
	 * @throws RuleClassLoadException
	 */
	boolean isParentClass(String projectName, String className, String parentClassName) throws RuleClassLoadException;

	/**
	 * 判断当前类型是否从接口中派生
	 * 
	 * @param projectName
	 * @param className
	 * @param interfaceName
	 *            不能是实现类，只能接口; 如果不是接口，返回false
	 * @return
	 * @throws RuleClassLoadException
	 */
	boolean isImplementedInterface(String projectName, String className, String interfaceName)
			throws RuleClassLoadException;

	/**
	 * 判断当前类型是否从指定类中派生，不考虑parentClassName是否为接口或抽象类
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
	 * 根据工程名称和对应的资源路径得到对应的资源流，输入流需要在外面代码中关闭;
	 * 如果资源不存在，返回流为null
	 * @param projectName
	 * @param resourcePath
	 * @return
	 * @throws RuleClassLoadException 工程Classloader未加载到
	 */
	InputStream getResourceStream(String projectName, String resourcePath) throws RuleClassLoadException;
	
	/**
	 * 根据业务组件和class得到对应的Code Feature，无论是在Jar包中还是输出的.class
	 * <p>
	 * 注：在获取没有业务组件的模块上，可能有误差
	 * @param businessComponent
	 * @param className
	 * @return
	 * @throws RuleClassLoadException
	 */
	JavaCodeFeature getCodeFeature(BusinessComponent businessComponent, String className) throws RuleClassLoadException;

}
