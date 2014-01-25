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
 * ��ѯģ�������ļ���queryTemplateContainer�����û�ж����ѯģ�壬��Ҫ����nodeKeyѡ��
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_LAYOUT, description = "��ѯģ�������ļ���queryTemplateContainer,���û�ж����ѯģ��,��Ҫ����nodeKeyѡ��", relatedIssueId = "169", coder = "lijbe", solution = "��ѯģ�������ļ���queryTemplateContainer,���û�ж����ѯģ��,��Ҫ����nodeKeyѡ��.")
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
        // ������������ļ��п�Ƭ��ͼ���ֲ���billFormEditor�Ĺ��ܽڵ�
        List<String> wrongNameNodes = new ArrayList<String>();

        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "��������Ĺ��ܱ����Ƿ���ȷ��\n");
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
                    String.format("���ܱ���Ϊ��%s �Ľڵ������ļ���û�����ò�ѯģ�����������%s \n", wrongNameNodes, "queryTemplateContainer"));

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
                    String.format("���ܱ���Ϊ��%s �Ľڵ������ļ���û�����ò�ѯģ�����������%s \n", wrongNameNodes, "queryTemplateContainer"));
        }
        if (unlawfulNodes.size() > 0) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("���ܱ���Ϊ��%s �Ľڵ�ֻ��һ����ѯģ��,ģ������queryTemplateContainer��������nodeKey \n", unlawfulNodes));
        }
        return result;
    }

    /**
     * ��ѯ���ܽڵ��Ӧ�ĵ���ģ��
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
     * ���ֻ��һ������ģ��ʱ���Ƿ�������nodeKeies��billFormEditor��nodekey��ֵ
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
