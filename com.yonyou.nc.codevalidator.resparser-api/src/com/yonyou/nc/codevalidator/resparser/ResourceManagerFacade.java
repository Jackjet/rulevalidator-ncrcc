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
 * ��Դ��������࣬��ǰNC�����е���Դ���ǴӸ�������л�ȡ��
 * @author mazhqa
 * @since V1.0
 */
public final class ResourceManagerFacade {

	private ResourceManagerFacade() {
	}

	private static IResourceManager resourceManagerProxy = new ResourceManagerProxy();
	private static IResourceVisitorManager resourceVisitorManager = new ResourceVisitorManagerProxy();

	/**
	 * ��ȡjavaԴ����Դ
	 * 
	 * @param javaResourceQuery
	 * @return
	 * @throws RuleBaseException
	 */
	public static List<JavaClassResource> getResource(JavaResourceQuery javaResourceQuery) throws RuleBaseException {
		IJavaResourceQueryFactory resourceQueryFactory = (IJavaResourceQueryFactory) resourceManagerProxy
				.getResourceQueryFactory(ResourceType.JAVA);
		if (resourceQueryFactory == null) {
			throw new ResourceParserException("Դ������Դ���ҹ�����δ�ҵ�!");
		}
		return resourceQueryFactory.getResource(javaResourceQuery);
	}

	/**
	 * ��ȡ���ݿ���Դ��Դ����Ҫͨ�����뺬��sql��ScriptResourceQuery������sql��ִ�н������Map��ʽ����
	 * <p>
	 * ����Դ
	 * @param resourceQuery
	 * @return
	 * @throws RuleBaseException
	 */
	public static List<ScriptResource> getResource(ScriptResourceQuery resourceQuery) throws RuleBaseException {
		IScriptResourceQueryFactory resourceQueryFactory = (IScriptResourceQueryFactory) resourceManagerProxy
				.getResourceQueryFactory(ResourceType.SCRIPT);
		if (resourceQueryFactory == null) {
			throw new ResourceParserException("�ű���Դ���ҹ�����δ�ҵ�!");
		}
		return resourceQueryFactory.getResource(resourceQuery);
	}

	/**
	 * ��ȡ���ݿ���Դ��Դ����Ҫͨ�����뺬��sql��ScriptResourceQuery������ִ�н�������ݼ�
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
			throw new ResourceParserException("�ű���Դ���ҹ�����δ�ҵ�!");
		}
		return resourceQueryFactory.getResourceAsDataSet(resourceQuery);
	}
	
	/**
	 * ��ȡ���ݿ���Դ��Դ����Ҫͨ�����뺬��sql��ScriptResourceQuery������ִ�н�������ݼ�
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
			throw new ResourceParserException("�ű���Դ���ҹ�����δ�ҵ�!");
		}
		return resourceQueryFactory.getResourceAsDataSetWithConnection(resourceQuery, connection);
	}

	/**
	 * ��ȡUpm��Դ
	 * 
	 * @param upmResourceQuery
	 * @return
	 * @throws RuleBaseException
	 */
	public static List<UpmResource> getResource(UpmResourceQuery upmResourceQuery) throws RuleBaseException {
		IUpmResourceQueryFactory resourceQueryFactory = (IUpmResourceQueryFactory) resourceManagerProxy
				.getResourceQueryFactory(ResourceType.UPM);
		if (resourceQueryFactory == null) {
			throw new ResourceParserException("UPM��Դ���ҹ�����δ�ҵ�!");
		}
		return resourceQueryFactory.getResource(upmResourceQuery);
	}

	/**
	 * ��ȡxml��Դ��Ŀǰ��Ҫָ����ǰ̨ui����2�������ļ�
	 * 
	 * @param xmlResourceQuery
	 * @return
	 * @throws RuleBaseException
	 */
	public static List<XmlResource> getResource(XmlResourceQuery xmlResourceQuery) throws RuleBaseException {
		IXmlResourceQueryFactory resourceQueryFactory = (IXmlResourceQueryFactory) resourceManagerProxy
				.getResourceQueryFactory(ResourceType.XML);
		if (resourceQueryFactory == null) {
			throw new ResourceParserException("XML��Դ���ҹ�����δ�ҵ�!");
		}
		return resourceQueryFactory.getResource(xmlResourceQuery);
	}

	/**
	 * ��ȡԪ������Դ
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
			throw new ResourceParserException("Ԫ������Դ���ҹ�����δ�ҵ�!");
		}
		return resourceQueryFactory.getResource(metadataResourceQuery);
	}
	
	/**
	 * ��ȡaop��Դ
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
			throw new ResourceParserException("AOP��Դ���ҹ�����δ�ҵ�!");
		}
		return resourceQueryFactory.getResource(aopResourceQuery);
	}

	public static void visitResource(BusinessComponent businessComponent, IResourceVisitor resourceVisitor)
			throws ResourceParserException {
		resourceVisitorManager.visitResource(businessComponent, resourceVisitor);
	}
	
	/**
	 * �ű���Դ��ѯ������Ļ�ȡ������ֱ��ʹ�ã����龡����getResource()����
	 * @return
	 */
	public static IScriptResourceQueryFactory getScriptResourceQueryFactory() {
		return (IScriptResourceQueryFactory) resourceManagerProxy.getResourceQueryFactory(ResourceType.SCRIPT);
	}
	
}
