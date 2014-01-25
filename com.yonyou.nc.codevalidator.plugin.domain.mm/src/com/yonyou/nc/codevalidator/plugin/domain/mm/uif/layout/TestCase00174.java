package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.layout;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;

import com.yonyou.nc.codevalidator.plugin.domain.mm.uif.MmUIFactoryConstants;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 卡片视图billFormEditor，如果没有多个单据模板，不要配置nodeKeies选项
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_LAYOUT, description = "卡片视图billFormEditor，如果没有多个单据模板,不要配置nodeKeies选项", relatedIssueId = "174", coder = "lijbe", solution = "卡片视图billFormEditor，如果没有多个单据模板,不要配置nodeKeies选项.【卡片视图bean的id必须为billFormEditor】")
public class TestCase00174 extends AbstractXmlRuleDefinition {

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

        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "请检查输入的功能编码是否正确！\n");
            return result;
        }
        List<String> nodeCodes = new ArrayList<String>();
        List<XmlResource> validXmlResources = new ArrayList<XmlResource>();
        for (XmlResource xmlResource : resources) {
            Element billFormEditorElement = xmlResource.getElementById("billFormEditor");
            if (null == billFormEditorElement) {
                wrongNameNodes.add(xmlResource.getFuncNodeCode());
                continue;
            }
            nodeCodes.add(xmlResource.getFuncNodeCode());
            validXmlResources.add(xmlResource);
        }

        if (wrongNameNodes.size() == resources.size()) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("功能编码为：%s 的节点配置文件中没有配置卡片视图,或者卡片视图的名称不为：%s \n", wrongNameNodes, "billFormEditor"));

            return result;
        }
        MapList<String, String> nodeCode2TemplateMap =
                this.getBillTemplateCount(nodeCodes.toArray(new String[nodeCodes.size()]), ruleExecContext);
        List<String> unlawfulNodes = new ArrayList<String>();
        for (XmlResource xmlResource : validXmlResources) {
            if (MMValueCheck.isEmpty(nodeCode2TemplateMap.get(xmlResource.getFuncNodeCode()))) {
                continue;
            }
            if (!this.checkOneTemplateWithNodeKeies(nodeCode2TemplateMap.get(xmlResource.getFuncNodeCode()).size(),
                    xmlResource)) {
                unlawfulNodes.add(xmlResource.getFuncNodeCode());
            }
        }
        if (wrongNameNodes.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("功能编码为：%s 的节点配置文件中没有配置卡片视图,或者卡片视图的名称不为：%s \n", wrongNameNodes, "billFormEditor"));
        }
        if (unlawfulNodes.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), String.format(
                    "功能编码为：%s 的节点只有一个单据模板,模板容器templateContainer不用配置nodeKeies,并且卡片视图billFormEditor不用注入属性:nodekey. \n",
                    unlawfulNodes));
        }
        return result;
    }

    /**
     * 查询功能节点对应的单据模板
     * 
     * @param nodeCodes
     * @param ruleExecContext
     * @return
     * @throws RuleBaseException
     */
    private MapList<String, String> getBillTemplateCount(String[] nodeCodes, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {

        MapList<String, String> nodeCode2TemplateMap = new MapList<String, String>();
        String sql = "select pk_billtypecode,nodecode from pub_billtemplet where nodecode in(";

        String whereValue = "";

        for (int i = 0; i < nodeCodes.length; i++) {

            if (i == nodeCodes.length - 1) {
                whereValue += "'" + nodeCodes[i] + "'";
            }
            else {
                whereValue += "'" + nodeCodes[i] + "',";
            }
        }
        whereValue += ") and (dr = 0 or dr is null ) and pk_corp='@@@@' ";
        sql += whereValue;
        DataSet dataSet = null;
        dataSet = SQLQueryExecuteUtils.executeQuery(sql, ruleExecContext.getRuntimeContext());
        if (MMValueCheck.isEmpty(dataSet)) {
            return nodeCode2TemplateMap;
        }

        List<DataRow> dataRows = dataSet.getRows();
        for (DataRow dataRow : dataRows) {
            nodeCode2TemplateMap.put((String) dataRow.getValue("nodecode"),
                    (String) dataRow.getValue("pk_billtypecode"));
        }

        return nodeCode2TemplateMap;
    }

    /**
     * 检查只有一个单据模板时，是否配置了nodeKeies和billFormEditor的nodekey有值
     * 
     * @param templateNum
     * @param xmlResource
     * @return
     */
    private boolean checkOneTemplateWithNodeKeies(int templateNum, XmlResource xmlResource) {

        List<Element> templateContainerElements =
                xmlResource.getBeanElementByClass(MmUIFactoryConstants.TEMPLATE_CONTAINER);

        Element nodeKeiesElement;
        if (templateContainerElements == null || templateContainerElements.size() == 0) {
            nodeKeiesElement = null;
        }
        else {
            nodeKeiesElement = xmlResource.getChildPropertyElement(templateContainerElements.get(0), "nodeKeies");
        }

        Element billFormEditorElement = xmlResource.getElementById("billFormEditor");

        Element nodeKeyElement = xmlResource.getChildPropertyElement(billFormEditorElement, "nodekey");

        String nodeKeyValue = null;
        if (null != nodeKeyElement) {
            nodeKeyValue = nodeKeyElement.getAttribute("value");
        }

        if (templateNum == 1 && (null != nodeKeiesElement || MMValueCheck.isNotEmpty(nodeKeyValue))) {
            return false;
        }

        return true;
    }

}
