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
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 查询模板容器的加载queryTemplateContainer，如果没有多个查询模板，不要配置nodeKey选项
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_LAYOUT, description = "查询模板容器的加载queryTemplateContainer,如果没有多个查询模板,不要配置nodeKey选项", relatedIssueId = "169", coder = "lijbe", solution = "查询模板容器的加载queryTemplateContainer,如果没有多个查询模板,不要配置nodeKey选项.")
public class TestCase00169 extends AbstractXmlRuleDefinition {

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
            List<Element> queryTemplateElements =
                    xmlResource.getBeanElementByClass(MmUIFactoryConstants.QUERY_TEMPLATE_CONTAINER);
            if (MMValueCheck.isEmpty(queryTemplateElements)) {
                wrongNameNodes.add(xmlResource.getFuncNodeCode());
                continue;
            }
            nodeCodes.add(xmlResource.getFuncNodeCode());
            validXmlResources.add(xmlResource);
        }

        if (wrongNameNodes.size() == resources.size()) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("功能编码为：%s 的节点配置文件中没有配置查询模板加载容器：%s \n", wrongNameNodes, "queryTemplateContainer"));

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
                    String.format("功能编码为：%s 的节点配置文件中没有配置查询模板加载容器：%s \n", wrongNameNodes, "queryTemplateContainer"));
        }
        if (unlawfulNodes.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("功能编码为：%s 的节点只有一个查询模板,模板容器queryTemplateContainer不用配置nodeKey \n", unlawfulNodes));
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
        String sql = "select node_code, model_code,id from pub_query_templet where node_code in(";

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
            nodeCode2TemplateMap.put((String) dataRow.getValue("node_code"), (String) dataRow.getValue("id"));
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
                xmlResource.getBeanElementByClass(MmUIFactoryConstants.QUERY_TEMPLATE_CONTAINER);

        Element nodeKeyElement = xmlResource.getChildPropertyElement(templateContainerElements.get(0), "nodeKey");

        String nodeKeyValue = null;
        if (null != nodeKeyElement) {
            nodeKeyValue = nodeKeyElement.getAttribute("value");
        }

        if (templateNum == 1 && (null != nodeKeyElement || MMValueCheck.isNotEmpty(nodeKeyValue))) {
            return false;
        }

        return true;
    }

}
