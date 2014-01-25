package com.yonyou.nc.codevalidator.resparser.resource;

import java.util.List;

import org.w3c.dom.Element;

import com.yonyou.nc.codevalidator.resparser.IResourceVisitor;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.ResourceType;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;
import com.yonyou.nc.codevalidator.sdk.utils.SpringXmlDocument;
import com.yonyou.nc.codevalidator.sdk.utils.XmlUtils;

/**
 * UI����2��spring��Դ�ļ�����XML�ļ������ɾ������Դ
 * <p>
 * ���XML�ļ��д���importԪ�أ�����ж����import�������ݵ�ǰ����ClassLoader�ܼ��ص�����Դ���ж�ȡ
 * 
 * @author mazhqa
 * @since V1.0
 * @modify V2.5 - ������SpringXmlDocument���༶import��֧�֣����������ع�
 */
public class XmlResource extends AbstractFileResource {

	private final String funcNodeCode;
	private final BusinessComponent businessComponent;

	private final SpringXmlDocument springXmlDocument;

	/**
	 * UI����2��Xml��Դ�����ļ�
	 * 
	 * @param resourcePath
	 *            - ��Դ�����·��
	 * @param funcNodeCode
	 * @param businessComponent
	 * @throws RuleBaseException
	 *             - XML����ʧ��ʱ���ִ��쳣
	 */
	public XmlResource(String resourcePath, String funcNodeCode, BusinessComponent businessComponent)
			throws RuleBaseException {
		this.resourcePath = resourcePath;
		this.funcNodeCode = funcNodeCode;
		this.businessComponent = businessComponent;
		this.springXmlDocument = XmlUtils.parseSpringXml(businessComponent.getProjectName(), resourcePath);
	}

	/**
	 * ����ָ��tagName��element�б�
	 * 
	 * @param tagname
	 * @return
	 */
	public List<Element> getElementsByTagName(String tagname) {
		return springXmlDocument.getElementsByTagName(tagname);
	}

	/**
	 * ���ص�ǰelement������ֱ�ӱ�ǩ��Ϊtagname����element�����û�в鵽���ؿ��б�
	 * 
	 * @param element
	 *            - Ҫ��ѯ��element
	 * @param tagname
	 *            - ��ǩ����
	 * @return
	 */
	public List<Element> getChildElementsByTagName(Element element, String tagname) {
		return springXmlDocument.getChildElementsByTagName(element, tagname);
	}

	/**
	 * ���ؾ���ָ��ֵ�� ID ���Ե� Element;
	 * <p>
	 * ���δ�ҵ�������null
	 * 
	 * @param elementId
	 * @return
	 */
	public Element getElementById(String elementId) {
		return springXmlDocument.getElementById(elementId);
	}

	/**
	 * ���ҷ��ؾ���ָ��ֵ�� ID ���Ե� Element�ж�Ӧ�������(tagname)Ϊproperty�Һ���name���Ե�Element��
	 * 
	 * @param parentElementId
	 * @param propertyName
	 * @return
	 */
	public Element getChildPropertyElement(String parentElementId, String propertyName) {
		Element parentElement = getElementById(parentElementId);
		return getChildPropertyElement(parentElement, propertyName);
	}

	/**
	 * ���ҷ��ؾ���ָ�� Element�ж�Ӧ�������(tagname)Ϊproperty�Һ���name���Ե�Element��
	 * "<bean id="xxx"> <property name="propertyName" value="cde"/> </bean>"
	 * 
	 * @param parentElement
	 * @param propertyName
	 * @return
	 */
	public Element getChildPropertyElement(Element parentElement, String propertyName) {
		return springXmlDocument.getDirectChildPropertyElement(parentElement, propertyName);
	}

	/**
	 * ��ȡxml�м̳���ָ������: parentClazz ������Ԫ��
	 * <p>
	 * �¼����˼�Ӽ̳й�ϵ������<bean parent="a"> a��class�Ǽ̳���parentClazz��bean����
	 * 
	 * @param parentClazz
	 * @return
	 * @throws ResourceParserException
	 */
	public List<Element> getAllBeanClass(String parentClazz) throws RuleBaseException {
		return springXmlDocument
				.getBeanElementListWithGivenParentClass(businessComponent.getProjectName(), parentClazz);
	}

	/**
	 * �õ���Ƭactions elements
	 * 
	 * @param xmlResource
	 * @param projectName
	 * @return
	 * @throws RuleClassLoadException
	 */
	public List<Element> getCardActions() throws RuleClassLoadException {
		return springXmlDocument.getActionsElementList(businessComponent.getProjectName(), "actionsOfCard");
	}

	/**
	 * �õ��б�actions elements
	 * 
	 * @return
	 * @throws RuleClassLoadException
	 */
	public List<Element> getListActions() throws RuleClassLoadException {
		return springXmlDocument.getActionsElementList(businessComponent.getProjectName(), "actionsOfList");
	}

	/**
	 * ��ȡָ������������element�б�
	 * 
	 * @param classValue
	 *            - ����ȫ·������
	 * @return
	 */
	public List<Element> getBeanElementByClass(String classValue) {
		return springXmlDocument.getBeanElementByClass(classValue);
	}

	public String getFuncNodeCode() {
		return funcNodeCode;
	}

	@Override
	public void accept(IResourceVisitor resourceVisitor) throws ResourceParserException {
		resourceVisitor.visit(this);
	}

	@Override
	public ResourceType getResourceType() {
		return ResourceType.XML;
	}

	public BusinessComponent getBusinessComponent() {
		return businessComponent;
	}

}
