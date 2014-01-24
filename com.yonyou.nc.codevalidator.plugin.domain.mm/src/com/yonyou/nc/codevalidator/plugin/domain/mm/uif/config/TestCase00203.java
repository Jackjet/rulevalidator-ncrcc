package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.config;

import java.util.List;

import org.w3c.dom.Element;

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
 * 检查懒加载配置中需要考虑批打印、批预览、批输出等按钮的子表数据打印不全问题
 * 
 * @author qiaoyanga
 * @since 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_CONFIGFILE, description = "懒加载配置中需要考虑批打印、批预览、批输出等按钮的子表数据打印不全问题", solution = "配置文件中nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad类的actionList属性中应该包含这些按钮", coder = "qiaoyanga", relatedIssueId = "203")
public class TestCase00203 extends AbstractXmlRuleDefinition {

    @Override
    protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        return new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
    }

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        StringBuffer noteBuilder = new StringBuffer();
        for (XmlResource xmlResource : resources) {
            List<Element> listLoadClasss =
                    xmlResource.getBeanElementByClass("nc.ui.pubapp.uif2app.lazilyload.LazilyLoadManager");
            if (listLoadClasss == null || listLoadClasss.size() == 0) {
                noteBuilder.append("未支持懒加载");
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
                continue;
            }

            Element lazilyLoadManagerEle = listLoadClasss.get(0);

            Element lazilyLoadSupporterEle =
                    xmlResource.getChildPropertyElement(lazilyLoadManagerEle, "lazilyLoadSupporter");

            if (lazilyLoadSupporterEle == null) {
                noteBuilder.append("懒加载配置中未考虑批打印、批预览、批输出");
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
                continue;
            }

            List<Element> lists = xmlResource.getChildElementsByTagName(lazilyLoadSupporterEle, "list");
            if (lists == null || lists.size() == 0) {
                noteBuilder.append("懒加载配置中未考虑批打印、批预览、批输出");
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
                continue;
            }

            // 只一个List Element
            Element listEle = lists.get(0);

            // 找到这个支持打印按钮懒加载的Element
            Element lazyActionsEle = null;

            // 一种是直接 写<bean class=xxx/>,还有一种是写 <ref bean="xxid"/>
            List<Element> listChildOfBeans = xmlResource.getChildElementsByTagName(listEle, "bean");

            if (listChildOfBeans != null && listChildOfBeans.size() > 0) {
                for (Element beanEle : listChildOfBeans) {
                    if ("nc.ui.pubapp.uif2app.lazilyload.ActionLazilyLoad".equals(beanEle.getAttribute("class"))) {
                        lazyActionsEle = beanEle;
                        break;
                    }
                }
            }

            if (lazyActionsEle == null) {
                // ref 形式
                List<Element> listChildOfRefs = xmlResource.getChildElementsByTagName(listEle, "ref");

                if (listChildOfRefs != null && listChildOfRefs.size() > 0) {
                    for (Element refEle : listChildOfRefs) {
                        String refBeanId = refEle.getAttribute("bean");
                        // 例子 <ref bean="lazyActions" />
                        if (refBeanId != null && (refBeanId.indexOf("Action") > 0 || refBeanId.indexOf("action") > 0)) {
                            lazyActionsEle = xmlResource.getElementById(refBeanId);
                            break;
                        }
                    }
                }

            }

            if (lazyActionsEle == null) {
                // 两种方式都没有找到,则没有配置打印按钮相关的懒加载支持
                noteBuilder.append("懒加载配置中未考虑支持批打印、批预览、批输出");
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
                continue;
            }

            Element actionListPropertyEle = xmlResource.getChildPropertyElement(lazyActionsEle, "actionList");

            if (actionListPropertyEle == null) {
                noteBuilder.append("懒加载配置中未考虑支持批打印、批预览、批输出");
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
                continue;
            }
            List<Element> plist = xmlResource.getChildElementsByTagName(actionListPropertyEle, "list");

            if (plist == null || plist.size() == 0) {
                noteBuilder.append("懒加载配置中未考虑支持批打印、批预览、批输出");
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
                continue;
            }

            Element e = plist.get(0);

            List<Element> actions = xmlResource.getChildElementsByTagName(e, "ref");
            if (actions == null || actions.size() < 3) {
                noteBuilder.append("懒加载配置中未配置完全.批打印、批预览、批输出");
                result.addResultElement(xmlResource.getResourcePath(), noteBuilder.toString());
                continue;
            }
            // if (noteBuilder.length() > 0) {
            // result.addResultElement(xmlResource.getResourcePath(),noteBuilder.toString());
            // }
        }
        return result;
    }

}
