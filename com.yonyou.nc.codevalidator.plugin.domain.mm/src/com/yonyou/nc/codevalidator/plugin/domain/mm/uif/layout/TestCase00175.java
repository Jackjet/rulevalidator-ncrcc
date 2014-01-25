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
 * 卡片视图billFormEditor，支持自动增行、空行过滤
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_LAYOUT, description = "卡片视图billFormEditor，支持自动增行、空行过滤", relatedIssueId = "175", coder = "lijbe", solution = "卡片视图billFormEditor，支持自动增行、空行过滤,即templateNotNullValidate属性和autoAddLine属性的值都为true.【卡片视图bean的id必须为billFormEditor】")
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
        // 用来存放配置文件中卡片视图名字不叫billFormEditor的功能节点
        List<String> wrongNameNodes = new ArrayList<String>();
        // 用来存放没有支持自动增行的功能节点.
        List<String> notAutoAddLineNodes = new ArrayList<String>();

        // 用来存放没有支持模板空行校验的功能节点.
        List<String> unTemplateNotNullNodes = new ArrayList<String>();
        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "请检查输入的功能编码是否正确！\n");
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
                    String.format("功能编码为：%s 的节点配置文件中没有配置卡片视图,或者卡片视图的名称不为：%s \n", wrongNameNodes, "billFormEditor"));
        }
        if (notAutoAddLineNodes.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("功能编码为：%s 的节点配置文件中卡片视图没有支持自动增行. \n", notAutoAddLineNodes));
        }
        if (unTemplateNotNullNodes.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("功能编码为：%s 的节点配置文件中卡片视图没有支持模板非空校验. \n", unTemplateNotNullNodes));
        }
        return result;
    }

}
