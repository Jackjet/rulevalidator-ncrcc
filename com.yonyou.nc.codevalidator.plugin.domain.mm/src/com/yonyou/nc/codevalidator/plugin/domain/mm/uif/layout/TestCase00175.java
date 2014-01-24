package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.layout;

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
 * ��Ƭ��ͼbillFormEditor��֧���Զ����С����й���
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_LAYOUT, description = "��Ƭ��ͼbillFormEditor��֧���Զ����С����й���", relatedIssueId = "175", coder = "lijbe", solution = "��Ƭ��ͼbillFormEditor��֧���Զ����С����й���,��templateNotNullValidate���Ժ�autoAddLine���Ե�ֵ��Ϊtrue.����Ƭ��ͼbean��id����ΪbillFormEditor��")
public class TestCase00175 extends AbstractXmlRuleDefinition {

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
        List<String> wrongNameNodes = new ArrayList<String>();
        // �������û��֧���Զ����еĹ��ܽڵ�.
        List<String> notAutoAddLineNodes = new ArrayList<String>();

        // �������û��֧��ģ�����У��Ĺ��ܽڵ�.
        List<String> unTemplateNotNullNodes = new ArrayList<String>();
        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "��������Ĺ��ܱ����Ƿ���ȷ��\n");
            return result;
        }
        for (XmlResource xmlResource : resources) {
            Element billFormEditorElement = xmlResource.getElementById("billFormEditor");
            if (null == billFormEditorElement) {
                wrongNameNodes.add(xmlResource.getFuncNodeCode());
                continue;
            }
            Element autoAddLineElement = xmlResource.getChildPropertyElement(billFormEditorElement, "autoAddLine");

            if (null == autoAddLineElement) {
                notAutoAddLineNodes.add(xmlResource.getFuncNodeCode());
            }
            else {
                String atuoAddLineValue = autoAddLineElement.getAttribute("value");
                if (!"true".equals(atuoAddLineValue)) {
                    notAutoAddLineNodes.add(xmlResource.getFuncNodeCode());
                }
            }

            Element unTemplateElement =
                    xmlResource.getChildPropertyElement(billFormEditorElement, "templateNotNullValidate");

            if (null == unTemplateElement) {
                unTemplateNotNullNodes.add(xmlResource.getFuncNodeCode());
            }
            else {
                String templateNouNullValue = unTemplateElement.getAttribute("value");
                if (!"true".equals(templateNouNullValue)) {
                    unTemplateNotNullNodes.add(xmlResource.getFuncNodeCode());
                }
            }

        }
        if (wrongNameNodes.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("���ܱ���Ϊ��%s �Ľڵ������ļ���û�����ÿ�Ƭ��ͼ,���߿�Ƭ��ͼ�����Ʋ�Ϊ��%s \n", wrongNameNodes, "billFormEditor"));
        }
        if (notAutoAddLineNodes.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("���ܱ���Ϊ��%s �Ľڵ������ļ��п�Ƭ��ͼû��֧���Զ�����. \n", notAutoAddLineNodes));
        }
        if (unTemplateNotNullNodes.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("���ܱ���Ϊ��%s �Ľڵ������ļ��п�Ƭ��ͼû��֧��ģ��ǿ�У��. \n", unTemplateNotNullNodes));
        }
        return result;
    }

}
