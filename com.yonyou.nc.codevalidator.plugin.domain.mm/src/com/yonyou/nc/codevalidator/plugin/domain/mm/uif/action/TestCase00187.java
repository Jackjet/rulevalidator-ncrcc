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
 * 卡片界面与列表界面支持配置不同的按钮
 * 
 * @since 6.0
 * @version 2013-9-5 下午8:10:21
 * @author zhongcha
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_BTNOREVENT, description = "卡片界面与列表界面支持配置不同的按钮", solution = "列表按钮的容器为actionsOfList，卡片按钮的容器为actionsOfCard  ", coder = "zhongcha", relatedIssueId = "187")
public class TestCase00187 extends AbstractXmlRuleDefinition {

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
                    "检查输入的功能节点编码是否正确\n");
        }
        // 保存列表按钮的容器不是actionsOfList的功能节点编码
        List<String> listVNotMatch = new ArrayList<String>();

        // 保存卡片按钮的容器不是actionsOfCard的功能节点编码
        List<String> billFNotMatch = new ArrayList<String>();

        // 保存actionsOfList缺失constructor-arg标签的节点编码
        List<String> listConstArgNull = new ArrayList<String>();

        // 保存actionsOfCard缺失constructor-arg标签的节点编码
        List<String> cardConstArgNull = new ArrayList<String>();

        // 保存不存在actionsOfList的功能节点编码
        List<String> actionsOfListNull = new ArrayList<String>();

        // 保存不存在actionsOfCard的功能节点编码
        List<String> actionsOfCardNull = new ArrayList<String>();

        for (XmlResource xmlResource : resources) {
            Element actionsOfList = xmlResource.getElementById("actionsOfList");
            if (actionsOfList != null) {
                String listView = null;
                List<Element> con_argList = xmlResource.getChildElementsByTagName(actionsOfList, "constructor-arg");
                if (MMValueCheck.isNotEmpty(con_argList)) {
                    listView = con_argList.get(0).getAttribute("ref");
                }
                if (listView != null) {
                    if (!listView.equals("listView")) {
                        listVNotMatch.add(xmlResource.getFuncNodeCode());
                    }
                }
                else {
                    listConstArgNull.add(xmlResource.getFuncNodeCode());
                }
            }
            else {
                actionsOfListNull.add(xmlResource.getFuncNodeCode());
            }
            Element actionsOfCard = xmlResource.getElementById("actionsOfCard");
            if (actionsOfCard != null) {
                String billFormEditor = null;
                List<Element> con_argList = xmlResource.getChildElementsByTagName(actionsOfCard, "constructor-arg");
                if (MMValueCheck.isNotEmpty(con_argList)) {
                    billFormEditor = con_argList.get(0).getAttribute("ref");
                }
                if (billFormEditor != null) {
                    if (!billFormEditor.equals("billFormEditor")) {
                        billFNotMatch.add(xmlResource.getFuncNodeCode());
                    }
                }
                else {
                    cardConstArgNull.add(xmlResource.getFuncNodeCode());
                }
            }
            else {
                actionsOfCardNull.add(xmlResource.getFuncNodeCode());
            }
            // 分别添加列表和卡片中不匹配错误
            if (MMValueCheck.isNotEmpty(listVNotMatch)) {
                result.addResultElement(xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("功能节点编码:%s 的列表按钮的容器不正确，应为actionsOfList.\n", listVNotMatch));
            }
            if (MMValueCheck.isNotEmpty(billFNotMatch)) {
                result.addResultElement(xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("功能节点编码:%s 的卡片按钮的容器不正确，应为actionsOfCard.\n", billFNotMatch));
            }
            // 分别添加列表和卡片中不存在constructor-arg的错误
            if (MMValueCheck.isNotEmpty(listConstArgNull)) {
                result.addResultElement(xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("功能节点编码:%s 的actionsOfList中没有配置constructor-arg。\n", listConstArgNull));
            }
            if (MMValueCheck.isNotEmpty(cardConstArgNull)) {
                result.addResultElement(xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("功能节点编码:%s 的actionsOfCard中没有配置constructor-arg。\n", cardConstArgNull));
            }
            // 分别添加列表和卡片中不存在actionsOfList或者actionOfList
            if (MMValueCheck.isNotEmpty(actionsOfListNull)) {
                result.addResultElement(xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("功能节点编码:%s 的配置文件中没有配置actionsOfList的bean.\n", actionsOfListNull));
            }
            if (MMValueCheck.isNotEmpty(actionsOfCardNull)) {
                result.addResultElement(xmlResource.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("功能节点编码:%s 的配置文件中没有配置actionsOfCard的bean.\n", actionsOfCardNull));
            }

        }
        return result;
    }
}
