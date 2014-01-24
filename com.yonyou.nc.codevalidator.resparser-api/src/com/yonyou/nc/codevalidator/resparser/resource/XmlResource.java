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
 * UI工厂2的spring资源文件，将XML文件解析成具体的资源
 * <p>
 * 如果XML文件中存在import元素，则进行额外的import处理，根据当前工程ClassLoader能加载到的资源进行读取
 * 
 * @author mazhqa
 * @since V1.0
 * @modify V2.5 - 增加了SpringXmlDocument，多级import的支持，并进行了重构
 */
public class XmlResource extends AbstractFileResource {

	private final String funcNodeCode;
	private final BusinessComponent businessComponent;

	private final SpringXmlDocument springXmlDocument;

	/**
	 * UI工厂2的Xml资源配置文件
	 * 
	 * @param resourcePath
	 *            - 资源的相对路径
	 * @param funcNodeCode
	 * @param businessComponent
	 * @throws RuleBaseException
	 *             - XML解析失败时出现此异常
	 */
	public XmlResource(String resourcePath, String funcNodeCode, BusinessComponent businessComponent)
			throws RuleBaseException {
		this.resourcePath = resourcePath;
		this.funcNodeCode = funcNodeCode;
		this.businessComponent = businessComponent;
		this.springXmlDocument = XmlUtils.parseSpringXml(businessComponent.getProjectName(), resourcePath);
	}

	/**
	 * 返回指定tagName的element列表
	 * 
	 * @param tagname
	 * @return
	 */
	public List<Element> getElementsByTagName(String tagname) {
		return springXmlDocument.getElementsByTagName(tagname);
	}

	/**
	 * 返回当前element的所有直接标签名为tagname的子element，如果没有查到返回空列表
	 * 
	 * @param element
	 *            - 要查询的element
	 * @param tagname
	 *            - 标签名称
	 * @return
	 */
	public List<Element> getChildElementsByTagName(Element element, String tagname) {
		return springXmlDocument.getChildElementsByTagName(element, tagname);
	}

	/**
	 * 返回具有指定值的 ID 属性的 Element;
	 * <p>
	 * 如果未找到，返回null
	 * 
	 * @param elementId
	 * @return
	 */
	public Element getElementById(String elementId) {
		return springXmlDocument.getElementById(elementId);
	}

	/**
	 * 查找返回具有指定值的 ID 属性的 Element中对应标记名称(tagname)为property且含有name属性的Element。
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
	 * 查找返回具有指定 Element中对应标记名称(tagname)为property且含有name属性的Element。
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
	 * 获取xml中继承于指定父类: parentClazz 的所有元素
	 * <p>
	 * 新加入了间接继承关系，比如<bean parent="a"> a的class是继承了parentClazz的bean定义
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
	 * 得到卡片actions elements
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
	 * 得到列表actions elements
	 * 
	 * @return
	 * @throws RuleClassLoadException
	 */
	public List<Element> getListActions() throws RuleClassLoadException {
		return springXmlDocument.getActionsElementList(businessComponent.getProjectName(), "actionsOfList");
	}

	/**
	 * 获取指定类名的所有element列表
	 * 
	 * @param classValue
	 *            - 类型全路径名称
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
