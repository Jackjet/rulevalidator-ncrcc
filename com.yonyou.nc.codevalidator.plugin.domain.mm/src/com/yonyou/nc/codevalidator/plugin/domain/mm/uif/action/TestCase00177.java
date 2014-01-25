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
 * 列表界面显示快速查询区域queryArea以及查询信息栏queryInfo
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "列表界面显示快速查询区域queryArea以及查询信息栏queryInfo", relatedIssueId = "177", coder = "lijbe", solution = "列表界面显示快速查询区域queryArea以及查询信息栏queryInfo")
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
        // 用来存放是否实现了nc.ui.pubapp.uif2app.actions.UEReturnAction按钮
        List<String> funNodesTwo = new ArrayList<String>();
        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "请检查输入的功能编码是否正确！\n");
            return result;
        }
        for (XmlResource xmlResource : resources) {

            List<Element> queryAreaElements = xmlResource.getBeanElementByClass(MmUIFactoryConstants.QUERY_AREA_CLASS);

            List<Element> queryInfoElements = xmlResource.getBeanElementByClass(MmUIFactoryConstants.QUERY_INFO_CLASS);

            // 判断xml文件中是否包含以上2个bean
            if (MMValueCheck.isEmpty(queryAreaElements)) {
                funNodes.add(xmlResource.getFuncNodeCode());

            }
            if (MMValueCheck.isEmpty(queryInfoElements)) {
                funNodesTwo.add(xmlResource.getFuncNodeCode());
            }
        }

        if (funNodes.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), String.format(
                    "功能编码为 %s 的节点配置文件中没有添加快速查询区域类：%s \n", funNodes, MmUIFactoryConstants.QUERY_AREA_CLASS));
        }
        if (funNodesTwo.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), String.format(
                    "功能编码为 %s 的节点配置文件中没有添加查询信息栏类：%s \n", funNodesTwo, MmUIFactoryConstants.QUERY_INFO_CLASS));
        }

        return result;
    }

}
