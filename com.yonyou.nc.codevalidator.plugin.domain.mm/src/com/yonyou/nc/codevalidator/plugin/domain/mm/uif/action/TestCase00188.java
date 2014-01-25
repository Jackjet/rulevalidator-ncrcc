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
 * ��Ƭ���������̬�ͱ༭̬֧�����ò�ͬ�İ�ť
 * 
 * @since 6.0
 * @version 2013-9-5 ����8:10:25
 * @author zhongcha
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "��Ƭ���������̬�ͱ༭̬֧�����ò�ͬ�İ�ť", solution = "��鿨Ƭ��ť����actionsOfCard���Ƿ���actions��editActions���� ", coder = "zhongcha", relatedIssueId = "188")
public class TestCase00188 extends AbstractXmlRuleDefinition {

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
        for (XmlResource xmlResource : resources) {
            Element actionsOfCard = xmlResource.getElementById("actionsOfCard");
            if (actionsOfCard != null) {
                List<String> errorList = new ArrayList<String>();
                if (MMValueCheck.isEmpty(xmlResource.getChildPropertyElement(actionsOfCard, "actions"))) {
                    errorList.add("actions");
                }
                if (MMValueCheck.isEmpty(xmlResource.getChildPropertyElement(actionsOfCard, "editActions"))) {
                    errorList.add("editActions");
                }
                if (MMValueCheck.isNotEmpty(errorList)) {
                    result.addResultElement(
                            xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                            String.format("���ܽڵ����:%s ��actionsOfCard��û����������:" + errorList + "��\n",
                                    xmlResource.getFuncNodeCode()));
                }
            }
            else {
                result.addResultElement(xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("���ܽڵ����:%s �������ļ���û����ΪactionsOfCard��bean��\n", xmlResource.getFuncNodeCode()));
            }
        }
        return result;
    }
}
