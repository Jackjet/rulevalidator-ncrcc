package com.yonyou.nc.codevalidator.sdk.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.IClassLoaderUtils;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;

/**
 * ���ڽ���springʽ��xml�����ļ������п��ܻ����ö��������ļ���ʽ
 * 
 * @author mazhqa
 * @since V2.5
 */
public class SpringXmlDocument {

	private static final String PROPERTY_TAG = "property";
	private static final String NAME_TAG = "name";
	private static final String BEAN_TAG = "bean";
	private static final String CLASS_TAG = "class";
	private static final String PARENT_TAG = "parent";
	private static final String ID_TAG = "id";
	private static final String REF_TAG = "ref";
	private static final String ACTIONS_TAG = "actions";
	private static final String EDIT_ACTIONS_TAG = "editActions";
	private static final String LIST_TAG = "list";
	private static final String NCACTION_TOP_CLASS = "nc.ui.uif2.NCAction";
	private static final String ACTIONGROUP_TOP_CLASS = "nc.funcnode.ui.action.ActionContainer";

	private final Document document;
	private final List<SpringXmlDocument> referenceDocuments;

	public SpringXmlDocument(Document document, List<SpringXmlDocument> referenDocuments) {
		this.document = document;
		this.referenceDocuments = referenDocuments;
	}

	public List<Element> getElementsByTagName(String tagname) {
		List<Element> result = new ArrayList<Element>();
		NodeList nodeList = document.getElementsByTagName(tagname);
		if (nodeList != null) {
			int nodeListLength = nodeList.getLength();
			for (int i = 0; i < nodeListLength; i++) {
				result.add((Element) nodeList.item(i));
			}
		}
		if (referenceDocuments != null) {
			for (SpringXmlDocument referenceDocument : referenceDocuments) {
				result.addAll(referenceDocument.getElementsByTagName(tagname));
			}
		}
		return result;
	}

	/**
	 * ���parentElement�µ�tag��ΪtagName��ֱ���ӽڵ�
	 * 
	 * @param parentElement
	 * @param tagname
	 * @return
	 */
	public List<Element> getChildElementsByTagName(Element parentElement, String tagname) {
		List<Element> result = new ArrayList<Element>();
		if (parentElement != null && tagname != null && !"".equals(tagname.trim())) {
			NodeList nodeList = parentElement.getChildNodes();
			int nodeListLength = nodeList.getLength();
			for (int i = 0; i < nodeListLength; i++) {
				Node node = nodeList.item(i);
				if (node instanceof Element) {
					Element element = (Element) node;
					if (tagname.equals(element.getTagName())) {
						result.add(element);
					}
				}
			}
		}
		return result;
	}

	/**
	 * �������ΪelementId�Ľڵ㣬�����ڵ�ǰ�����õ��ĵ��в�ѯ�������ѯ����������null
	 * 
	 * @param elementId
	 * @return
	 */
	public Element getElementById(String elementId) {
		Element result = null;
		if (StringUtils.isNotBlank(elementId)) {
			result = document.getElementById(elementId);
			if (result == null && referenceDocuments != null) {
				for (SpringXmlDocument referenceDocument : referenceDocuments) {
					result = referenceDocument.getElementById(elementId);
					if (result != null) {
						break;
					}
				}
			}

		}
		return result;
	}

	/**
	 * ���ҷ��ؾ���ָ�� Element�ж�Ӧ�������(tagname)Ϊproperty�Һ���name���Ե�Element��
	 * "<bean id="xxx"> <property name="propertyName" value="cde"/> </bean>"
	 * 
	 * @param parentElement
	 * @param propertyName
	 * @return
	 */
	public Element getDirectChildPropertyElement(Element parentElement, String propertyName) {
		Element result = null;
		List<Element> propertyElementList = this.getChildElementsByTagName(parentElement, PROPERTY_TAG);
		for (Element propertyElement : propertyElementList) {
			if (propertyName.equals(propertyElement.getAttribute(NAME_TAG))) {
				result = propertyElement;
				break;
			}
		}
		return result;
	}

	/**
	 * ��õ�ǰ���м̳�parentClass��element�б�
	 * 
	 * @param projectName
	 * @param parentClass
	 * @return
	 * @throws RuleBaseException
	 */
	public List<Element> getBeanElementListWithGivenParentClass(String projectName, String parentClazz)
			throws RuleBaseException {
		List<Element> beanTagElementList = this.getElementsByTagName(BEAN_TAG);
		List<Element> result = new ArrayList<Element>();
		Set<String> existBeanNameSet = new HashSet<String>();
		Map<String, List<Element>> parentToElementMap = new HashMap<String, List<Element>>();
		for (Element beanTagElement : beanTagElementList) {
			String classTag = beanTagElement.getAttribute(CLASS_TAG);
			if (StringUtils.isNotBlank(classTag) && isExtendSuperClass(projectName, classTag, parentClazz)) {
				String beanId = beanTagElement.getAttribute(ID_TAG);
				existBeanNameSet.add(beanId);
				result.add(beanTagElement);
			}

			String parentTag = beanTagElement.getAttribute(PARENT_TAG);
			if (StringUtils.isNotBlank(parentTag)) {
				if(parentToElementMap.get(parentTag) == null) {
					parentToElementMap.put(parentTag, new ArrayList<Element>());
				}
				parentToElementMap.get(parentTag).add(beanTagElement);
			}
		}
		
		while(!existBeanNameSet.isEmpty()) {
			List<String> collectedBeanIdList = new ArrayList<String>();
			for (String existBeanId : existBeanNameSet) {
				List<Element> elements = parentToElementMap.get(existBeanId);
				if(elements != null) {
					result.addAll(elements);
					for (Element element : elements) {
						collectedBeanIdList.add(element.getAttribute(ID_TAG));
					}
				}
			}
			existBeanNameSet.clear();
			existBeanNameSet.addAll(collectedBeanIdList);
		}
		return result;
	}

	private boolean isExtendSuperClass(String projectName, String nowClazz, String parentClazz)
			throws RuleClassLoadException {
		return ClassLoaderUtilsFactory.getClassLoaderUtils().isParentClass(projectName, nowClazz, parentClazz);
	}

	/**
	 * ��ȡָ������������element�б�
	 * 
	 * @param classValue
	 *            - ����ȫ·������
	 * @return
	 */
	public List<Element> getBeanElementByClass(String classValue) {
		List<Element> beanTagElementList = this.getElementsByTagName(BEAN_TAG);
		List<Element> result = new ArrayList<Element>();
		for (Element beanTagElement : beanTagElementList) {
			if (classValue.equals(beanTagElement.getAttribute(CLASS_TAG))) {
				result.add(beanTagElement);
			}
		}
		return result;
	}

	/**
	 * �õ�elementId������actions�����action��Ӧelements
	 * 
	 * @param projectName
	 * @param elementId
	 * @return
	 * @throws RuleClassLoadException
	 */
	public List<Element> getActionsElementList(String projectName, String elementId) throws RuleClassLoadException {
		Element element = getElementById(elementId);
		List<Element> result = new ArrayList<Element>();
		if (element != null) {
			// ���̬��ť
			Element actionsElement = getDirectChildPropertyElement(element, ACTIONS_TAG);
			List<Element> showActionsBean = getChildElementsByTagName(actionsElement, LIST_TAG);
			List<Element> allRefList = new ArrayList<Element>();
			if (showActionsBean.size() == 1) {
				allRefList.addAll(getChildElementsByTagName(showActionsBean.get(0), REF_TAG));
			}
			// �༭̬��ť
			Element editActionsElement = getDirectChildPropertyElement(element, EDIT_ACTIONS_TAG);
			List<Element> editActionsBean = getChildElementsByTagName(editActionsElement, LIST_TAG);
			if (editActionsBean.size() == 1) {
				allRefList.addAll(getChildElementsByTagName(editActionsBean.get(0), REF_TAG));
			}
			result.addAll(getActionElements(projectName, allRefList));
		}
		return result;
	}

	/**
	 * ���ݰ�ť�Ͱ�ť���ϵ�ref�б���ȡ���а�ťelement
	 * 
	 * �ȿ��Դ���̳�NCAction�İ�ť��Ҳ�ɴ���ť���еİ�ť�б�
	 * 
	 * @param projectName
	 * @param refActionElementsList
	 *            - ��Ӧaction element�б�
	 * @return
	 * @throws RuleClassLoadException
	 */
	private List<Element> getActionElements(String projectName, List<Element> refActionElementsList)
			throws RuleClassLoadException {
		IClassLoaderUtils classLoaderUtils = ClassLoaderUtilsFactory.getClassLoaderUtils();
		// xml������
		List<Element> result = new ArrayList<Element>();
		for (Element ref : refActionElementsList) {
			String actionid = ref.getAttribute(BEAN_TAG);
			Element actionBean = getElementById(actionid);
			if (actionBean != null) {
				// ��ť
				if (classLoaderUtils.isExtendsParentClass(projectName, actionBean.getAttribute(CLASS_TAG),
						NCACTION_TOP_CLASS)) {
					result.add(actionBean);
				} else if (classLoaderUtils.isExtendsParentClass(projectName, actionBean.getAttribute(CLASS_TAG),
						ACTIONGROUP_TOP_CLASS)) {
					// ��ť��
					Element groupActions = getDirectChildPropertyElement(actionBean, ACTIONS_TAG);
					List<Element> groupBeans = getChildElementsByTagName(groupActions, LIST_TAG);
					if (groupBeans.size() == 1) {
						List<Element> groupRefList = getChildElementsByTagName(groupBeans.get(0), REF_TAG);
						for (Element element : groupRefList) {
							Element groupActionBean = getElementById(element.getAttribute(BEAN_TAG));
							// ��ť
							if (classLoaderUtils.isExtendsParentClass(projectName,
									groupActionBean.getAttribute(CLASS_TAG), NCACTION_TOP_CLASS)) {
								result.add(groupActionBean);
							}
						}
					}
				}
			}
		}
		return result;
	}

}
