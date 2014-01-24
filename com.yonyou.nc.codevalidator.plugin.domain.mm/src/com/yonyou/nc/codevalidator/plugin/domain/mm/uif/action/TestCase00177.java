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
 * �б������ʾ���ٲ�ѯ����queryArea�Լ���ѯ��Ϣ��queryInfo
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "�б������ʾ���ٲ�ѯ����queryArea�Լ���ѯ��Ϣ��queryInfo", relatedIssueId = "177", coder = "lijbe", solution = "�б������ʾ���ٲ�ѯ����queryArea�Լ���ѯ��Ϣ��queryInfo")
public class TestCase00177 extends AbstractXmlRuleDefinition {

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

            List<Element> queryAreaElements = xmlResource.getBeanElementByClass(MmUIFactoryConstants.QUERY_AREA_CLASS);

            List<Element> queryInfoElements = xmlResource.getBeanElementByClass(MmUIFactoryConstants.QUERY_INFO_CLASS);

            // �ж�xml�ļ����Ƿ��������2��bean
            if (MMValueCheck.isEmpty(queryAreaElements)) {
                funNodes.add(xmlResource.getFuncNodeCode());

            }
            if (MMValueCheck.isEmpty(queryInfoElements)) {
                funNodesTwo.add(xmlResource.getFuncNodeCode());
            }
        }

        if (funNodes.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), String.format(
                    "���ܱ���Ϊ %s �Ľڵ������ļ���û����ӿ��ٲ�ѯ�����ࣺ%s \n", funNodes, MmUIFactoryConstants.QUERY_AREA_CLASS));
        }
        if (funNodesTwo.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), String.format(
                    "���ܱ���Ϊ %s �Ľڵ������ļ���û����Ӳ�ѯ��Ϣ���ࣺ%s \n", funNodesTwo, MmUIFactoryConstants.QUERY_INFO_CLASS));
        }

        return result;
    }

}
