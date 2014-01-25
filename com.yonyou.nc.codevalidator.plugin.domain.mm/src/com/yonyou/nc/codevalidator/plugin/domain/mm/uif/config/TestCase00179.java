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
 * 定义picky并在编辑事件中进行引用，以便能够支持不同页签相同字段的编辑事件
 * 
 * @author qiaoyang
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "定义picky并在编辑事件中进行引用，以便能够支持不同页签相同字段的编辑事件", solution = "", coder = "qiaoyang", relatedIssueId = "179")
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
            // 获得picky类的Bean
            List<Element> pickyElementList =
                    xmlResource.getBeanElementByClass("nc.ui.pubapp.uif2app.event.ChildrenPicky");
            if (pickyElementList == null || pickyElementList.size() == 0) {
                errorBuilder.append("找不到picky类的Bean");
            }
            // 获得事件的handlerGroup的list下的所有Bean
            List<Element> beanList = null;
            String errorTypeContxt = null;
            beanList = this.getEventHandlerList(xmlResource, errorTypeContxt);
            if (beanList == null && MMValueCheck.isNotEmpty(errorTypeContxt)) {
                errorBuilder.append(errorTypeContxt);
            }
            // 记录下被事件Bean引用的picky
            List<String> pickyList = new ArrayList<String>();
            if (beanList != null) {
                for (Element element : beanList) {
                    Element pickyElement = xmlResource.getChildPropertyElement(element, "picky");
                    if (pickyElement != null && pickyElement.getAttribute("ref") != null) {
                        pickyList.add(pickyElement.getAttribute("ref"));
                    }
                }
            }
            // 遍历所有picky类的Bean，查找是否有没被引用的，有则报错
            for (Element element : pickyElementList) {
                if (!pickyList.contains(element.getAttribute("id"))) {
                    errorBuilder.append("id为[" + element.getAttribute("id") + "]的picky类的Bean没有被引用\n");
                }
            }
            if (errorBuilder.toString().trim().length() > 0) {
                result.addResultElement(xmlResource.getResourcePath(), errorBuilder.toString());
            }
        }
        return result;
    }

    /**
     * 获得xml中事件容器的所有Handler
     * 
     * @param context
     * @return
     * @throws BusinessException
     */
    public List<Element> getEventHandlerList(XmlResource xmlResource, String errorTypeContxt) {
        // 获得事件容器
        List<Element> eventElementList =
                xmlResource.getBeanElementByClass("nc.ui.pubapp.uif2app.model.AppEventHandlerMediator");
        if (eventElementList == null || eventElementList.size() == 0) {
            errorTypeContxt = "找不到事件容器";
            return null;
        }
        List<Element> resultBeanLists = new ArrayList<Element>();
        for (Element element : eventElementList) {
            // 获得事件容器的handlerGroup
            Element handlerGroupElement = xmlResource.getChildPropertyElement(element, "handlerGroup");
            if (handlerGroupElement == null) {
                errorTypeContxt = "找不到事件容器的handlerGroup";
                return null;
            }
            // 获得事件容器的handlerGroup的list
            List<Element> handlerGroupList = xmlResource.getChildElementsByTagName(handlerGroupElement, "list");
            if (handlerGroupList == null || handlerGroupList.size() == 0) {
                errorTypeContxt = "找不到事件容器的handlerGroup的list";
                return null;
            }
            Element listElement = handlerGroupList.get(0);
            // 获得所有事件Bean
            List<Element> beanList = xmlResource.getChildElementsByTagName(listElement, "bean");
            if (beanList == null || beanList.size() == 0) {
                errorTypeContxt = "事件容器的handlerGroup的list是空的";
                return null;
            }
            resultBeanLists.addAll(beanList);
        }
        return resultBeanLists;
    }
}
/**
 * List<Element> eventMediatorEles = new ArrayList<Element>(); if
 * (eventElementList.size() > 1) { // 当有多个事件容器的时候. 可能有别的对话框也支持事件配置. //
 * 但是主界面的世界配置类,ref对应的model应该是标准的manageAppModel for (Element element :
 * eventElementList) { Element modelEle =
 * xmlResource.getChildPropertyElement(element, "model"); //
 * ref对应的model应该是标准的manageAppModel if
 * ("manageAppModel".equals(modelEle.getAttribute("ref"))) { eventMediatorEle =
 * element; break; } }
 * if (eventMediatorEle == null) { // 此种情况是由于 主事件容器配置的model, id
 * 不是标准的命名manageAppModel eventMediatorEle = eventElementList.get(0); }
 * // throw new BusinessException("存在多个事件容器，应该只有一个"); } else { eventMediatorEle
 * = eventElementList.get(0); } // 获得事件容器的handlerGroup Element
 * handlerGroupElement = xmlResource.getChildPropertyElement( eventMediatorEle,
 * "handlerGroup"); if (handlerGroupElement == null) { throw new
 * Exception("找不到事件容器的handlerGroup"); } // 获得事件容器的handlerGroup的list
 * List<Element> handlerGroupList = xmlResource.getChildElementsByTagName(
 * handlerGroupElement, "list"); if (handlerGroupList == null ||
 * handlerGroupList.size() == 0) { throw new
 * Exception("找不到事件容器的handlerGroup的list"); } Element listElement =
 * handlerGroupList.get(0); // 获得所有事件Bean List<Element> beanList =
 * xmlResource.getChildElementsByTagName( listElement, "bean"); if (beanList ==
 * null || beanList.size() == 0) { throw new
 * Exception("事件容器的handlerGroup的list是空的"); } return beanList; }
 **/

