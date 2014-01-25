package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.action;

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
 * ��Ƭ�������б����֧�����ò�ͬ�İ�ť
 * 
 * @since 6.0
 * @version 2013-9-5 ����8:10:21
 * @author zhongcha
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "��Ƭ�������б����֧�����ò�ͬ�İ�ť", solution = "�б�ť������ΪactionsOfList����Ƭ��ť������ΪactionsOfCard  ", coder = "zhongcha", relatedIssueId = "187")
public class TestCase00187 extends AbstractXmlRuleDefinition {

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
        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "�������Ĺ��ܽڵ�����Ƿ���ȷ\n");
        }
        // �����б�ť����������actionsOfList�Ĺ��ܽڵ����
        List<String> listVNotMatch = new ArrayList<String>();

        // ���濨Ƭ��ť����������actionsOfCard�Ĺ��ܽڵ����
        List<String> billFNotMatch = new ArrayList<String>();

        // ����actionsOfListȱʧconstructor-arg��ǩ�Ľڵ����
        List<String> listConstArgNull = new ArrayList<String>();

        // ����actionsOfCardȱʧconstructor-arg��ǩ�Ľڵ����
        List<String> cardConstArgNull = new ArrayList<String>();

        // ���治����actionsOfList�Ĺ��ܽڵ����
        List<String> actionsOfListNull = new ArrayList<String>();

        // ���治����actionsOfCard�Ĺ��ܽڵ����
        List<String> actionsOfCardNull = new ArrayList<String>();

        for (XmlResource xmlResource : resources) {
            Element actionsOfList = xmlResource.getElementById("actionsOfList");
            if (actionsOfList != null) {
                String listView = null;
                List<Element> con_argList = xmlResource.getChildElementsByTagName(actionsOfList, "constructor-arg");
                if (MMValueCheck.isNotEmpty(con_argList)) {
                    listView = con_argList.get(0).getAttribute("ref");
                }
                if (listView != null) {
                    if (!listView.equals("listView")) {
                        listVNotMatch.add(xmlResource.getFuncNodeCode());
                    }
                }
                else {
                    listConstArgNull.add(xmlResource.getFuncNodeCode());
                }
            }
            else {
                actionsOfListNull.add(xmlResource.getFuncNodeCode());
            }
            Element actionsOfCard = xmlResource.getElementById("actionsOfCard");
            if (actionsOfCard != null) {
                String billFormEditor = null;
                List<Element> con_argList = xmlResource.getChildElementsByTagName(actionsOfCard, "constructor-arg");
                if (MMValueCheck.isNotEmpty(con_argList)) {
                    billFormEditor = con_argList.get(0).getAttribute("ref");
                }
                if (billFormEditor != null) {
                    if (!billFormEditor.equals("billFormEditor")) {
                        billFNotMatch.add(xmlResource.getFuncNodeCode());
                    }
                }
                else {
                    cardConstArgNull.add(xmlResource.getFuncNodeCode());
                }
            }
            else {
                actionsOfCardNull.add(xmlResource.getFuncNodeCode());
            }
            // �ֱ�����б�Ϳ�Ƭ�в�ƥ�����
            if (MMValueCheck.isNotEmpty(listVNotMatch)) {
                result.addResultElement(xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("���ܽڵ����:%s ���б�ť����������ȷ��ӦΪactionsOfList.\n", listVNotMatch));
            }
            if (MMValueCheck.isNotEmpty(billFNotMatch)) {
                result.addResultElement(xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("���ܽڵ����:%s �Ŀ�Ƭ��ť����������ȷ��ӦΪactionsOfCard.\n", billFNotMatch));
            }
            // �ֱ�����б�Ϳ�Ƭ�в�����constructor-arg�Ĵ���
            if (MMValueCheck.isNotEmpty(listConstArgNull)) {
                result.addResultElement(xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("���ܽڵ����:%s ��actionsOfList��û������constructor-arg��\n", listConstArgNull));
            }
            if (MMValueCheck.isNotEmpty(cardConstArgNull)) {
                result.addResultElement(xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("���ܽڵ����:%s ��actionsOfCard��û������constructor-arg��\n", cardConstArgNull));
            }
            // �ֱ�����б�Ϳ�Ƭ�в�����actionsOfList����actionOfList
            if (MMValueCheck.isNotEmpty(actionsOfListNull)) {
                result.addResultElement(xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("���ܽڵ����:%s �������ļ���û������actionsOfList��bean.\n", actionsOfListNull));
            }
            if (MMValueCheck.isNotEmpty(actionsOfCardNull)) {
                result.addResultElement(xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("���ܽڵ����:%s �������ļ���û������actionsOfCard��bean.\n", actionsOfCardNull));
            }

        }
        return result;
    }
}
