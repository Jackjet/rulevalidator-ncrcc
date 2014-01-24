package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.action;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.yonyou.nc.codevalidator.plugin.domain.mm.uif.MmUIFactoryConstants;
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
 * ��Ƭ������ʾ���ذ�ť�Լ���ҳ������cardInfoPnl�Ƿ�������
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "�����ļ����Ƿ������˿�Ƭ������ʾ���ذ�ť�Լ���ҳ������cardInfoPnl", relatedIssueId = "178", coder = "lijbe", solution = "�����ļ��п�Ƭ����Ӧ�����ù�����cardInfoPnl,����ʹ��nc.ui.pubapp.uif2app.tangramlayout.UECardLayoutToolbarPanel��;���ذ�ť[returnaction]ʹ��nc.ui.pubapp.uif2app.actions.UEReturnAction��")
public class TestCase00178 extends AbstractXmlRuleDefinition {

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
        List<String> funNodes = new ArrayList<String>();
        // ��������Ƿ�ʵ����nc.ui.pubapp.uif2app.actions.UEReturnAction��ť
        List<String> funNodesTwo = new ArrayList<String>();
        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "��������Ĺ��ܱ����Ƿ���ȷ��\n");
            return result;
        }
        for (XmlResource xmlResource : resources) {
            Element element = xmlResource.getElementById("cardInfoPnl");
            if (null == element) {
                List<Element> elementList =
                        xmlResource.getBeanElementByClass(MmUIFactoryConstants.CARD_TOOL_BARPANEL_CLASS);
                if (MMValueCheck.isNotEmpty(elementList)) {
                    element = elementList.get(0);
                }
            }
            if (null == element) {
                funNodes.add(xmlResource.getFuncNodeCode());
                continue;
            }
            // �õ�titleAction�����µ�bean
            List<Element> titleActionBeans =
                    xmlResource.getBeanElementByClass("nc.ui.pubapp.uif2app.actions.UEReturnAction");
            if (MMValueCheck.isEmpty(titleActionBeans)) {
                funNodesTwo.add(xmlResource.getFuncNodeCode());
            }
        }

        if (funNodes.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), String.format(
                    "���ܱ���Ϊ %s �Ľڵ������ļ���û��ʵ�ֹ������ࣺ%s \n", funNodes, MmUIFactoryConstants.CARD_TOOL_BARPANEL_CLASS));
        }
        if (funNodesTwo.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), String.format(
                    "���ܱ���Ϊ %s �Ľڵ������ļ��п�Ƭ���ذ�ťû��ʵ�֣�%s \n", funNodesTwo, MmUIFactoryConstants.RETURN_ACTION_CLASS));
        }

        return result;
    }

}
