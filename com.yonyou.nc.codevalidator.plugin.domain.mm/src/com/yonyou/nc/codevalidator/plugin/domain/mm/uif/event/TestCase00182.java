package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.event;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.yonyou.nc.codevalidator.plugin.domain.mm.uif.MmUIFactoryConstants;
import com.yonyou.nc.codevalidator.plugin.domain.mm.uif.util.MmXmlAnalysisUtil;
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
 * ��鵥���Ƿ�֧���˶�������б༩���¼�
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "��鵥���Ƿ�֧���˶�������б༩���¼�", solution = "��鵥���Ƿ�֧���˶�������б༩���¼�", coder = "lijbe", relatedIssueId = "182")
public class TestCase00182 extends AbstractXmlRuleDefinition {

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
        List<String> funNodeNames = new ArrayList<String>();

        boolean flag = false;
        Element e = null;
        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "�������Ĺ��ܽڵ�����Ƿ���ȷ\n");
        }
        for (XmlResource xmlResource : resources) {
            List<Element> eventElement = MmXmlAnalysisUtil.getEventHandlerElementList(xmlResource, result);

            if (null == eventElement || eventElement.size() < 1) {
                result.addResultElement(xmlResource.getBusinessComponent().getDisplayBusiCompName(), "û���ҵ��¼���ص�������Ϣ.\n");
                continue;
            }
            for (Element element : eventElement) {
                e = xmlResource.getChildPropertyElement(element, "event");
                if (null != e && MmUIFactoryConstants.BODY_AFTER_ROW_EDIT_EVENT.equals(e.getAttribute("value"))) {
                    flag = true;

                    break;
                }
            }
            if (!flag) {
                funNodeNames.add(xmlResource.getFuncNodeCode());
            }

        }
        if (!funNodeNames.isEmpty()) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), String.format(
                    "���ܽڵ����:%s �������ļ�û��ʵ�ֱ����б༭���¼�[%s]\n", funNodeNames, MmUIFactoryConstants.BODY_AFTER_ROW_EDIT_EVENT));
        }
        return result;
    }

}
