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
 * ɾ����ť֧��������
 * 
 * @since 6.0
 * @version 2013-9-5 ����8:10:31
 * @author zhongcha
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "ɾ����ť֧��������", solution = "���ɾ����ť�Ƿ���ȷʹ����ָ���Ĵ����࣬����ť�Ƿ�������singleBillService���Ժ�multiBillService���ԣ����߾߱���һ���ɡ���ɾ����ťBean��ID����Ϊ:deleteAction��  ", coder = "zhongcha", relatedIssueId = "190")
public class TestCase00190 extends AbstractXmlRuleDefinition {

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
            Element deleteAction = xmlResource.getElementById("deleteAction");
            if (deleteAction != null) {
                if (MMValueCheck.isEmpty(xmlResource.getChildPropertyElement(deleteAction, "singleBillService"))
                        && MMValueCheck.isEmpty(xmlResource.getChildPropertyElement(deleteAction, "multiBillService"))) {
                    result.addResultElement(
                            xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                            String.format("���ܽڵ����:%s ��ɾ����ťδ������singleBillService���Ի�multiBillService����.\n",
                                    xmlResource.getFuncNodeCode()));
                }
            }
        }
        return result;
    }

}
