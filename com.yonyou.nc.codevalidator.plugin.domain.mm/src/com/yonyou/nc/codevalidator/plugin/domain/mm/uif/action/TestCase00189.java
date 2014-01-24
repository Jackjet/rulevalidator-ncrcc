package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.action;

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
 * 新增按钮、编辑按钮、复制按钮点击后要自动切换到卡片界面
 * 
 * @since 6.0
 * @version 2013-9-5 下午8:10:28
 * @author zhongcha
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = " 新增按钮、编辑按钮、复制按钮点击后要自动切换到卡片界面", solution = "按钮上配置了拦截器nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor【新增,修改,复制按钮的bean命名必须为:addAction,editAction,copyAction】", coder = "zhongcha", relatedIssueId = "189")
public class TestCase00189 extends AbstractXmlRuleDefinition {

    @Override
    protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        XmlResourceQuery xmlResQry = new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
        return xmlResQry;
    }

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        for (XmlResource xmlResource : resources) {
            Element addAction = xmlResource.getElementById("addAction");
            Element editAction = xmlResource.getElementById("editAction");
            Element copyAction = xmlResource.getElementById("copyAction");
            if (addAction != null) {
                if (!this.hasShowCardInterceptor(addAction, xmlResource)) {
                    result.addResultElement(
                            xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                            String.format(
                                    "功能节点编码:%s 的新增按钮上未配置了拦截器nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor。\n",
                                    xmlResource.getFuncNodeCode()));
                }
            }
            else {
                result.addResultElement(xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("功能节点编码:%s 的不存在新增按钮（addAction）。\n", xmlResource.getFuncNodeCode()));
            }
            if (editAction != null) {
                if (!this.hasShowCardInterceptor(editAction, xmlResource)) {
                    result.addResultElement(
                            xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                            String.format(
                                    "功能节点编码:%s 的编辑按钮上未配置了拦截器nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor。\n",
                                    xmlResource.getFuncNodeCode()));
                }
            }
            else {
                result.addResultElement(xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("功能节点编码:%s 的不存在编辑按钮（editAction）。\n", xmlResource.getFuncNodeCode()));
            }

            if (copyAction != null) {
                if (!this.hasShowCardInterceptor(copyAction, xmlResource)) {
                    result.addResultElement(
                            xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                            String.format(
                                    "功能节点编码:%s 的复制按钮上未配置了拦截器nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor。\n",
                                    xmlResource.getFuncNodeCode()));
                }
            }
            else {
                result.addResultElement(xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("功能节点编码:%s 的不存在复制按钮（copyAction）。\n", xmlResource.getFuncNodeCode()));
            }

        }
        return result;
    }

    private boolean hasShowCardInterceptor(Element action, XmlResource xmlResource) {
        boolean flag = false;
        if (MMValueCheck.isEmpty(xmlResource.getChildPropertyElement(action, "interceptor"))) {
            return flag;
        }
        Element showCardInterceptor =
                xmlResource.getElementById(xmlResource.getChildPropertyElement(action, "interceptor").getAttribute(
                        "ref"));
        if (MMValueCheck.isEmpty(showCardInterceptor)) {
            return flag;
        }
        if ("nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor".equals(showCardInterceptor
                .getAttribute("class"))) {
            flag = true;
        }
        else {
            // 该if语句判断的是另一种正确的场景：主子孙单据的配置文件中的不同的配置方法（如BOM）。
            List<Element> listList =
                    xmlResource.getChildElementsByTagName(
                            xmlResource.getChildPropertyElement(showCardInterceptor, "interceptors"), "list");
            if (MMValueCheck.isNotEmpty(listList)) {
                Element listElmt = listList.get(0);
                List<Element> inteceptorList = xmlResource.getChildElementsByTagName(listElmt, "ref");
                for (Element elmt : inteceptorList) {
                    Element inteceptorElmt = xmlResource.getElementById(elmt.getAttribute("bean"));
                    if ("nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor".equals(inteceptorElmt
                            .getAttribute("class"))) {
                        flag = true;
                        break;
                    }
                }
            }
        }

        return flag;
    }
}
