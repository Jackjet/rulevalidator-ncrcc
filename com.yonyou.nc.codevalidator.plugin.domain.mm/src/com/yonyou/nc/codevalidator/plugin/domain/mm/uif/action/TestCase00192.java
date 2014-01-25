package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.action;

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
 * ���水ť����ģ��ǿ�У���ⲻҪ����������ǰ̨У��
 * 
 * @since 6.0
 * @version 2013-9-5 ����8:10:34
 * @author zhongcha
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "���水ť����ģ��ǿ�У���ⲻҪ����������ǰ̨У��", solution = "���水ť����ģ��ǿ�У���ⲻҪ����������ǰ̨У�� ", coder = "zhongcha", relatedIssueId = "192")
public class TestCase00192 extends AbstractXmlRuleDefinition {

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
            Element validationService = xmlResource.getElementById("validationService");
            if (validationService != null) {
                Element validators = xmlResource.getChildPropertyElement(validationService, "validators");
                List<Element> listElement = xmlResource.getChildElementsByTagName(validators, "list");
                if (MMValueCheck.isNotEmpty(listElement)) {
                    // ���ģ��ǿյ�У��bean
                    List<Element> notnullvalidate =
                            xmlResource.getBeanElementByClass(MmUIFactoryConstants.TEMPLATE_NOT_NULL_VALIDATION);
                    // validationService��ע���У��
                    List<Element> validatorList = xmlResource.getChildElementsByTagName(listElement.get(0), "bean");
                    if (validatorList != null && MMValueCheck.isNotEmpty(notnullvalidate)) {
                        if (validatorList.size() != 1 || !validatorList.get(0).equals(notnullvalidate.get(0))) {
                            result.addResultElement(
                                    xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                                    String.format("���ܽڵ����:%s �ı��水ť����ģ��ǿ�У���⻹������������ǰ̨У��\n",
                                            xmlResource.getFuncNodeCode()));
                        }
                    }
                    else {
                        result.addResultElement(xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                                String.format("���ܽڵ����:%s �ı��水ť������ģ��ǿ�У��\n", xmlResource.getFuncNodeCode()));

                    }
                }
            }
        }

        return result;
    }

}
