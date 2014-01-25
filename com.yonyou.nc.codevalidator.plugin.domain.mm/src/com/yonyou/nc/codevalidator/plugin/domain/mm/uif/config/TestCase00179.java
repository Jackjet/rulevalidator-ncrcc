package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.config;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ����picky���ڱ༭�¼��н������ã��Ա��ܹ�֧�ֲ�ͬҳǩ��ͬ�ֶεı༭�¼�
 * 
 * @author qiaoyang
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "����picky���ڱ༭�¼��н������ã��Ա��ܹ�֧�ֲ�ͬҳǩ��ͬ�ֶεı༭�¼�", solution = "", coder = "qiaoyang", relatedIssueId = "179")
public class TestCase00179 extends AbstractXmlRuleDefinition {

    @Override
    protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        XmlResourceQuery xmlResourceQuery = new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
        return xmlResourceQuery;
    }

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        for (XmlResource xmlResource : resources) {
            final StringBuilder errorBuilder = new StringBuilder();
            // ���picky���Bean
            List<Element> pickyElementList =
                    xmlResource.getBeanElementByClass("nc.ui.pubapp.uif2app.event.ChildrenPicky");
            if (pickyElementList == null || pickyElementList.size() == 0) {
                errorBuilder.append("�Ҳ���picky���Bean");
            }
            // ����¼���handlerGroup��list�µ�����Bean
            List<Element> beanList = null;
            String errorTypeContxt = null;
            beanList = this.getEventHandlerList(xmlResource, errorTypeContxt);
            if (beanList == null && MMValueCheck.isNotEmpty(errorTypeContxt)) {
                errorBuilder.append(errorTypeContxt);
            }
            // ��¼�±��¼�Bean���õ�picky
            List<String> pickyList = new ArrayList<String>();
            if (beanList != null) {
                for (Element element : beanList) {
                    Element pickyElement = xmlResource.getChildPropertyElement(element, "picky");
                    if (pickyElement != null && pickyElement.getAttribute("ref") != null) {
                        pickyList.add(pickyElement.getAttribute("ref"));
                    }
                }
            }
            // ��������picky���Bean�������Ƿ���û�����õģ����򱨴�
            for (Element element : pickyElementList) {
                if (!pickyList.contains(element.getAttribute("id"))) {
                    errorBuilder.append("idΪ[" + element.getAttribute("id") + "]��picky���Beanû�б�����\n");
                }
            }
            if (errorBuilder.toString().trim().length() > 0) {
                result.addResultElement(xmlResource.getResourcePath(), errorBuilder.toString());
            }
        }
        return result;
    }

    /**
     * ���xml���¼�����������Handler
     * 
     * @param context
     * @return
     * @throws BusinessException
     */
    public List<Element> getEventHandlerList(XmlResource xmlResource, String errorTypeContxt) {
        // ����¼�����
        List<Element> eventElementList =
                xmlResource.getBeanElementByClass("nc.ui.pubapp.uif2app.model.AppEventHandlerMediator");
        if (eventElementList == null || eventElementList.size() == 0) {
            errorTypeContxt = "�Ҳ����¼�����";
            return null;
        }
        List<Element> resultBeanLists = new ArrayList<Element>();
        for (Element element : eventElementList) {
            // ����¼�������handlerGroup
            Element handlerGroupElement = xmlResource.getChildPropertyElement(element, "handlerGroup");
            if (handlerGroupElement == null) {
                errorTypeContxt = "�Ҳ����¼�������handlerGroup";
                return null;
            }
            // ����¼�������handlerGroup��list
            List<Element> handlerGroupList = xmlResource.getChildElementsByTagName(handlerGroupElement, "list");
            if (handlerGroupList == null || handlerGroupList.size() == 0) {
                errorTypeContxt = "�Ҳ����¼�������handlerGroup��list";
                return null;
            }
            Element listElement = handlerGroupList.get(0);
            // ��������¼�Bean
            List<Element> beanList = xmlResource.getChildElementsByTagName(listElement, "bean");
            if (beanList == null || beanList.size() == 0) {
                errorTypeContxt = "�¼�������handlerGroup��list�ǿյ�";
                return null;
            }
            resultBeanLists.addAll(beanList);
        }
        return resultBeanLists;
    }
}
/**
 * List<Element> eventMediatorEles = new ArrayList<Element>(); if
 * (eventElementList.size() > 1) { // ���ж���¼�������ʱ��. �����б�ĶԻ���Ҳ֧���¼�����. //
 * ���������������������,ref��Ӧ��modelӦ���Ǳ�׼��manageAppModel for (Element element :
 * eventElementList) { Element modelEle =
 * xmlResource.getChildPropertyElement(element, "model"); //
 * ref��Ӧ��modelӦ���Ǳ�׼��manageAppModel if
 * ("manageAppModel".equals(modelEle.getAttribute("ref"))) { eventMediatorEle =
 * element; break; } }
 * if (eventMediatorEle == null) { // ������������� ���¼��������õ�model, id
 * ���Ǳ�׼������manageAppModel eventMediatorEle = eventElementList.get(0); }
 * // throw new BusinessException("���ڶ���¼�������Ӧ��ֻ��һ��"); } else { eventMediatorEle
 * = eventElementList.get(0); } // ����¼�������handlerGroup Element
 * handlerGroupElement = xmlResource.getChildPropertyElement( eventMediatorEle,
 * "handlerGroup"); if (handlerGroupElement == null) { throw new
 * Exception("�Ҳ����¼�������handlerGroup"); } // ����¼�������handlerGroup��list
 * List<Element> handlerGroupList = xmlResource.getChildElementsByTagName(
 * handlerGroupElement, "list"); if (handlerGroupList == null ||
 * handlerGroupList.size() == 0) { throw new
 * Exception("�Ҳ����¼�������handlerGroup��list"); } Element listElement =
 * handlerGroupList.get(0); // ��������¼�Bean List<Element> beanList =
 * xmlResource.getChildElementsByTagName( listElement, "bean"); if (beanList ==
 * null || beanList.size() == 0) { throw new
 * Exception("�¼�������handlerGroup��list�ǿյ�"); } return beanList; }
 **/

