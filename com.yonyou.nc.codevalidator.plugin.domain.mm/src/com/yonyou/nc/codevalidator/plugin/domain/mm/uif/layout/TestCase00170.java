package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.layout;

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
 * �����û��Զ���������userdefitemContainer����ѡ��
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_LAYOUT, description = "�����ļ����м����û��Զ���������userdefitemContainer����ѡ��", relatedIssueId = "170", coder = "lijbe", solution = "�����ļ����м����û��Զ���������userdefitemContainer����ѡ��,������classΪ��nc.ui.uif2.userdefitem.UserDefItemContainer��Bean")
public class TestCase00170 extends AbstractXmlRuleDefinition {

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
        // ������������ļ��п�Ƭ��ͼ���ֲ���billFormEditor�Ĺ��ܽڵ�
        List<String> unlawfulNodes = new ArrayList<String>();

        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "��������Ĺ��ܱ����Ƿ���ȷ��\n");
            return result;
        }

        for (XmlResource xmlResource : resources) {
            List<Element> userDefContainerElement =
                    xmlResource.getBeanElementByClass(MmUIFactoryConstants.USER_DEF_CONTAINER);
            if (MMValueCheck.isEmpty(userDefContainerElement)) {
                unlawfulNodes.add(xmlResource.getFuncNodeCode());
            }
        }
        if (unlawfulNodes.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("���ܱ���Ϊ��%s �Ľڵ������ļ���û�������û��Զ�����������%s \n", unlawfulNodes, "userdefitemContainer"));
        }
        return result;
    }

}
