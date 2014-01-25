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
 * 卡片界面显示返回按钮以及翻页工具栏cardInfoPnl是否配置了
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "配置文件中是否配置了卡片界面显示返回按钮以及翻页工具栏cardInfoPnl", relatedIssueId = "178", coder = "lijbe", solution = "配置文件中卡片界面应该配置工具栏cardInfoPnl,并且使用nc.ui.pubapp.uif2app.tangramlayout.UECardLayoutToolbarPanel类;返回按钮[returnaction]使用nc.ui.pubapp.uif2app.actions.UEReturnAction类")
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
        // 用来存放是否实现了nc.ui.pubapp.uif2app.actions.UEReturnAction按钮
        List<String> funNodesTwo = new ArrayList<String>();
        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "请检查输入的功能编码是否正确！\n");
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
            // 得到titleAction属性下的bean
            List<Element> titleActionBeans =
                    xmlResource.getBeanElementByClass("nc.ui.pubapp.uif2app.actions.UEReturnAction");
            if (MMValueCheck.isEmpty(titleActionBeans)) {
                funNodesTwo.add(xmlResource.getFuncNodeCode());
            }
        }

        if (funNodes.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), String.format(
                    "功能编码为 %s 的节点配置文件中没有实现工具栏类：%s \n", funNodes, MmUIFactoryConstants.CARD_TOOL_BARPANEL_CLASS));
        }
        if (funNodesTwo.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), String.format(
                    "功能编码为 %s 的节点配置文件中卡片返回按钮没有实现：%s \n", funNodesTwo, MmUIFactoryConstants.RETURN_ACTION_CLASS));
        }

        return result;
    }

}
