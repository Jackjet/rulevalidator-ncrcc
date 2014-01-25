package com.yonyou.nc.codevalidator.resparser;

import java.sql.Connection;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.resource.AopResource;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.resparser.resource.MetadataResource;
import com.yonyou.nc.codevalidator.resparser.resource.ScriptDataSetResource;
import com.yonyou.nc.codevalidator.resparser.resource.ScriptResource;
import com.yonyou.nc.codevalidator.resparser.resource.UpmResource;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 资源管理外观类，当前NC中所有的资源都是从该外观类中获取的
 * @author mazhqa
 * @since V1.0
 */
public final class ResourceManagerFacade {

	private ResourceManagerFacade() {
	}

	private static IResourceManager resourceManagerProxy = new ResourceManagerProxy();
	private static IResourceVisitorManager resourceVisitorManager = new ResourceVisitorManagerProxy();

	/**
	 * 获取java源码资源
	 * 
	 * @param javaResourceQuery
	 * @return
	 * @throws RuleBaseException
	 */
	public static List<JavaClassResource> getResource(JavaResourceQuery javaResourceQuery) throws RuleBaseException {
		IJavaResourceQueryFactory resourceQueryFactory = (IJavaResourceQueryFactory) resourceManagerProxy
				.getResourceQueryFactory(ResourceType.JAVA);
		if (resourceQueryFactory == null) {
			throw new ResourceParserException("源代码资源查找工厂类未找到!");
		}
		return resourceQueryFactory.getResource(javaResourceQuery);
	}

	/**
	 * 获取数据库资源资源，主要通过传入含有sql的ScriptResourceQuery并返回sql的执行结果，以Map形式返回
	 * <p>
	 * 数据源
	 * @param resourceQuery
	 * @return
	 * @throws RuleBaseException
	 */
	public static List<ScriptResource> getResource(ScriptResourceQuery resourceQuery) throws RuleBaseException {
		IScriptResourceQueryFactory resourceQueryFactory = (IScriptResourceQueryFactory) resourceManagerProxy
				.getResourceQueryFactory(ResourceType.SCRIPT);
		if (resourceQueryFactory == null) {
			throw new ResourceParserException("脚本资源查找工厂类未找到!");
		}
		return resourceQueryFactory.getResource(resourceQuery);
	}

	/**
	 * 获取数据库资源资源，主要通过传入含有sql的ScriptResourceQuery并返回执行结果的数据集
	 * 
	 * @param resourceQuery
	 * @return
	 * @throws ResourceParserException
	 */
	public static ScriptDataSetResource getResourceAsDataSet(ScriptResourceQuery resourceQuery)
			throws ResourceParserException {
		IScriptResourceQueryFactory resourceQueryFactory = (IScriptResourceQueryFactory) resourceManagerProxy
				.getResourceQueryFactory(ResourceType.SCRIPT);
		if (resourceQueryFactory == null) {
			throw new ResourceParserException("脚本资源查找工厂类未找到!");
		}
		return resourceQueryFactory.getResourceAsDataSet(resourceQuery);
	}
	
	/**
	 * 获取数据库资源资源，主要通过传入含有sql的ScriptResourceQuery并返回执行结果的数据集
	 * 
	 * @param resourceQuery
	 * @return
	 * @throws ResourceParserException
	 */
	public static ScriptDataSetResource getResourceAsDataSet(ScriptResourceQuery resourceQuery, Connection connection)
			throws ResourceParserException {
		IScriptResourceQueryFactory resourceQueryFactory = (IScriptResourceQueryFactory) resourceManagerProxy
				.getResourceQueryFactory(ResourceType.SCRIPT);
		if (resourceQueryFactory == null) {
			throw new ResourceParserException("脚本资源查找工厂类未找到!");
		}
		return resourceQueryFactory.getResourceAsDataSetWithConnection(resourceQuery, connection);
	}

	/**
	 * 获取Upm资源
	 * 
	 * @param upmResourceQuery
	 * @return
	 * @throws RuleBaseException
	 */
	public static List<UpmResource> getResource(UpmResourceQuery upmResourceQuery) throws RuleBaseException {
		IUpmResourceQueryFactory resourceQueryFactory = (IUpmResourceQueryFactory) resourceManagerProxy
				.getResourceQueryFactory(ResourceType.UPM);
		if (resourceQueryFactory == null) {
			throw new ResourceParserException("UPM资源查找工厂类未找到!");
		}
		return resourceQueryFactory.getResource(upmResourceQuery);
	}

	/**
	 * 获取xml资源，目前主要指的是前台ui工厂2的配置文件
	 * 
	 * @param xmlResourceQuery
	 * @return
	 * @throws RuleBaseException
	 */
	public static List<XmlResource> getResource(XmlResourceQuery xmlResourceQuery) throws RuleBaseException {
		IXmlResourceQueryFactory resourceQueryFactory = (IXmlResourceQueryFactory) resourceManagerProxy
				.getResourceQueryFactory(ResourceType.XML);
		if (resourceQueryFactory == null) {
			throw new ResourceParserException("XML资源查找工厂类未找到!");
		}
		return resourceQueryFactory.getResource(xmlResourceQuery);
	}

	/**
	 * 获取元数据资源
	 * 
	 * @param metadataResourceQuery
	 * @return
	 * @throws RuleBaseException
	 */
	public static List<MetadataResource> getResource(MetadataResourceQuery metadataResourceQuery)
			throws RuleBaseException {
		IMetaDataResourceQueryFactory resourceQueryFactory = (IMetaDataResourceQueryFactory) resourceManagerProxy
				.getResourceQueryFactory(ResourceType.METADATA);
		if (resourceQueryFactory == null) {
			throw new ResourceParserException("元数据资源查找工厂类未找到!");
		}
		return resourceQueryFactory.getResource(metadataResourceQuery);
	}
	
	/**
	 * 获取aop资源
	 * 
	 * @param aopResourceQuery
	 * @return
	 * @throws RuleBaseException
	 */
	public static List<AopResource> getResource(AopResourceQuery aopResourceQuery)
			throws RuleBaseException {
		IAopResourceQueryFactory resourceQueryFactory = (IAopResourceQueryFactory) resourceManagerProxy
				.getResourceQueryFactory(ResourceType.AOP);
		if (resourceQueryFactory == null) {
			throw new ResourceParserException("AOP资源查找工厂类未找到!");
		}
		return resourceQueryFactory.getResource(aopResourceQuery);
	}

	public static void visitResource(BusinessComponent businessComponent, IResourceVisitor resourceVisitor)
			throws ResourceParserException {
		resourceVisitorManager.visitResource(businessComponent, resourceVisitor);
	}
	
	/**
	 * 脚本资源查询工厂类的获取，谨慎直接使用，建议尽量用getResource()方法
	 * @return
	 */
	public static IScriptResourceQueryFactory getScriptResourceQueryFactory() {
		return (IScriptResourceQueryFactory) resourceManagerProxy.getResourceQueryFactory(ResourceType.SCRIPT);
	}
	
}
