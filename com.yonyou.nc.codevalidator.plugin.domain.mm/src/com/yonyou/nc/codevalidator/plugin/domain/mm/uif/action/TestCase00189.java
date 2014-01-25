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
 * ������ť���༭��ť�����ư�ť�����Ҫ�Զ��л�����Ƭ����
 * 
 * @since 6.0
 * @version 2013-9-5 ����8:10:28
 * @author zhongcha
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = " ������ť���༭��ť�����ư�ť�����Ҫ�Զ��л�����Ƭ����", solution = "��ť��������������nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor������,�޸�,���ư�ť��bean��������Ϊ:addAction,editAction,copyAction��", coder = "zhongcha", relatedIssueId = "189")
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
                                    "���ܽڵ����:%s ��������ť��δ������������nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor��\n",
                                    xmlResource.getFuncNodeCode()));
                }
            }
            else {
                result.addResultElement(xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("���ܽڵ����:%s �Ĳ�����������ť��addAction����\n", xmlResource.getFuncNodeCode()));
            }
            if (editAction != null) {
                if (!this.hasShowCardInterceptor(editAction, xmlResource)) {
                    result.addResultElement(
                            xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                            String.format(
                                    "���ܽڵ����:%s �ı༭��ť��δ������������nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor��\n",
                                    xmlResource.getFuncNodeCode()));
                }
            }
            else {
                result.addResultElement(xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("���ܽڵ����:%s �Ĳ����ڱ༭��ť��editAction����\n", xmlResource.getFuncNodeCode()));
            }

            if (copyAction != null) {
                if (!this.hasShowCardInterceptor(copyAction, xmlResource)) {
                    result.addResultElement(
                            xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                            String.format(
                                    "���ܽڵ����:%s �ĸ��ư�ť��δ������������nc.ui.pubapp.uif2app.actions.interceptor.ShowUpComponentInterceptor��\n",
                                    xmlResource.getFuncNodeCode()));
                }
            }
            else {
                result.addResultElement(xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("���ܽڵ����:%s �Ĳ����ڸ��ư�ť��copyAction����\n", xmlResource.getFuncNodeCode()));
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
            // ��if����жϵ�����һ����ȷ�ĳ����������ﵥ�ݵ������ļ��еĲ�ͬ�����÷�������BOM����
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
