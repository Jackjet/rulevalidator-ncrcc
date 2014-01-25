package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.billtemplet;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MmTempletQueryUtil;
import com.yonyou.nc.codevalidator.plugin.domain.mm.vo.MmSystemplateVO;
import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IGlobalRuleDefinition;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 单据模板表头、表尾信息、审计信息要符合UE规范
 * 
 * @author gaojf
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, description = "单据模板表头、表尾信息、审计信息要符合UE规范", relatedIssueId = "128", coder = "gaojf", solution = "如果具有表尾页签。必须包含表尾tail信息字段（制单人、制单日期、审批人、审批日期）。审计页签必须包含审计audit信息字段（创建人、创建时间、最后修改人、最后修改时间）。")
public class TestCase00128 extends AbstractXmlRuleDefinition implements IGlobalRuleDefinition {

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

        // 没有默认单据模板的节点,存放funnode
        List<String> noBillTempletNodes = new ArrayList<String>();

        // 有单据模板，但单据模板没有字段，存放模板标识，nodekey
        List<String> noItemBillTemplets = new ArrayList<String>();

        // 有单据模板，有字段，但是不符合规范，物料和部门分别存放模板标识，nodekey
        MapList<String, String> noRelationItemsMap = new MapList<String, String>();

        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "请检查输入的功能编码是否正确！\n");
            return result;
        }
        for (XmlResource xmlResource : resources) {
            List<MmSystemplateVO> sysTemplateVOs =
                    MmTempletQueryUtil.queryBillSystemplateVOList(xmlResource.getFuncNodeCode(), ruleExecContext);
            if (sysTemplateVOs.isEmpty()) {
                noBillTempletNodes.add(xmlResource.getFuncNodeCode());
                continue;
            }
            this.check(sysTemplateVOs, ruleExecContext, noItemBillTemplets, noRelationItemsMap);
            if (noRelationItemsMap.size() > 0) {
                this.returnResults(result, ruleExecContext, xmlResource, noRelationItemsMap);
                result.addResultElement(
                        ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("【如果错误很多，请检查单据模板中是否将表尾页签编码设为了audit，审计页签设为了tail（表尾应为tail，审计应为audit）】",
                                xmlResource.getFuncNodeCode()));

            }
            // 清空mapList的值，存储下个模板的记录
            noRelationItemsMap.toMap().clear();
        }
        // 没有单据模板
        if (!noBillTempletNodes.isEmpty()) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("功能编码为: %s 的节点没有单据模板. \n", noBillTempletNodes));
        }
        return result;
    }

    /**
     * 通过单据模板ID查询单据模板表体信息，并判断是否符合规范
     * 
     * @param sysTemplateVOs
     * @param ruleExecContext
     * @param noItemBillTemplets
     *            没有字段的单据模板
     * @param noRelationItems
     *            没有通过物料关联主单位的单据模板
     * @throws RuleBaseException
     */
    private void check(List<MmSystemplateVO> sysTemplateVOs, IRuleExecuteContext ruleExecContext,
            List<String> noItemBillTemplets, MapList<String, String> noRelationItemsMap) throws RuleBaseException {

        for (MmSystemplateVO mmSystemplateVO : sysTemplateVOs) {
            // 查询该模板对应的表尾页签
            MapList<String, String> tableCodeMap =
                    this.queryTailMessage(mmSystemplateVO.getTemplateid(), ruleExecContext);
            // 获得表尾页签
            List<String> keyItemSet = new ArrayList<String>();
            keyItemSet = tableCodeMap.get("tail");
            // 如果没有表尾页签不报错
            if (null != keyItemSet && !keyItemSet.isEmpty()) {
                if (!keyItemSet.contains("billmaker")) {
                    noRelationItemsMap.put("billmaker", mmSystemplateVO.getNodekey());
                }
                if (!keyItemSet.contains("dmakedate")) {
                    noRelationItemsMap.put("dmakedate", mmSystemplateVO.getNodekey());
                }
                if (!keyItemSet.contains("approver")) {
                    noRelationItemsMap.put("approver", mmSystemplateVO.getNodekey());
                }
                if (!keyItemSet.contains("taudittime")) {
                    noRelationItemsMap.put("taudittime", mmSystemplateVO.getNodekey());
                }
            }
            // 获得审计页签
            keyItemSet = tableCodeMap.get("audit");
            if (null != keyItemSet && !keyItemSet.isEmpty()) {
                if (!keyItemSet.contains("creator")) {
                    noRelationItemsMap.put("creator", mmSystemplateVO.getNodekey());
                }
                if (!keyItemSet.contains("creationtime")) {
                    noRelationItemsMap.put("creationtime", mmSystemplateVO.getNodekey());
                }
                if (!keyItemSet.contains("modifier")) {
                    noRelationItemsMap.put("modifier", mmSystemplateVO.getNodekey());
                }
                if (!keyItemSet.contains("modifiedtime")) {
                    noRelationItemsMap.put("modifiedtime", mmSystemplateVO.getNodekey());
                }
            }
        }
    }

    /**
     * 根据单据模板查询tail和audit页签的值，放入一个MapList中，key值为tail或audit，List为Itemkey中的值。
     * 
     * @param templateid
     * @return
     * @throws RuleBaseException
     * @throws BusinessException
     */
    private MapList<String, String> queryTailMessage(String templateid, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.itemkey,b.table_code from pub_billtemplet_b b where b.pk_billtemplet='");
        sql.append(templateid);
        sql.append("' and (b.table_code='tail' or b.table_code='audit') and b.dr is not null and b.dr=0");

        DataSet dataSet = null;
        dataSet = SQLQueryExecuteUtils.executeQuery(sql.toString(), ruleExecContext.getRuntimeContext());

        if (MMValueCheck.isEmpty(dataSet)) {
            return null;
        }

        MapList<String, String> tableCodeMap = new MapList<String, String>();
        for (DataRow dataRow : dataSet.getRows()) {
            tableCodeMap.put((String) dataRow.getValue("table_code"), (String) dataRow.getValue("itemkey"));
        }
        return tableCodeMap;
    }

    /**
     * 将提示信息装入result结果集
     * 
     * @param result
     * @param ruleExecContext
     * @param xmlResource
     * @param noRelationItemsMap
     */
    private void returnResults(ResourceRuleExecuteResult result, IRuleExecuteContext ruleExecContext,
            XmlResource xmlResource, MapList<String, String> noRelationItemsMap) {
        List<String> noRelationItems = new ArrayList<String>();
        noRelationItems = noRelationItemsMap.get("billmaker");
        if (null != noRelationItems && !noRelationItems.isEmpty()) {
            result.addResultElement(
                    ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("功能编码为: %s,节点标识nodekey为：%s的单据模板的表尾页签中，没有制单人billmaker. \n",
                            xmlResource.getFuncNodeCode(), noRelationItems));
        }
        noRelationItems = noRelationItemsMap.get("dmakedate");
        if (null != noRelationItems && !noRelationItems.isEmpty()) {
            result.addResultElement(
                    ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("功能编码为: %s,节点标识nodekey为：%s的单据模板的表尾页签中，没有制单日期dmakedate. \n",
                            xmlResource.getFuncNodeCode(), noRelationItems));
        }
        noRelationItems = noRelationItemsMap.get("approver");
        if (null != noRelationItems && !noRelationItems.isEmpty()) {
            result.addResultElement(
                    ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("功能编码为: %s,节点标识nodekey为：%s的单据模板的表尾页签中，没有审批人approver. \n",
                            xmlResource.getFuncNodeCode(), noRelationItems));
        }
        noRelationItems = noRelationItemsMap.get("taudittime");
        if (null != noRelationItems && !noRelationItems.isEmpty()) {
            result.addResultElement(
                    ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("功能编码为: %s,节点标识nodekey为：%s的单据模板的表尾页签中，没有审批日期taudittime. \n",
                            xmlResource.getFuncNodeCode(), noRelationItems));
        }
        noRelationItems = noRelationItemsMap.get("creator");
        if (null != noRelationItems && !noRelationItems.isEmpty()) {
            result.addResultElement(
                    ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("功能编码为: %s,节点标识nodekey为：%s的单据模板的审计页签中，没有创建人creator. \n",
                            xmlResource.getFuncNodeCode(), noRelationItems));
        }
        noRelationItems = noRelationItemsMap.get("creationtime");
        if (null != noRelationItems && !noRelationItems.isEmpty()) {
            result.addResultElement(
                    ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("功能编码为: %s,节点标识nodekey为：%s的单据模板的审计页签中，没有创建时间creationtime. \n",
                            xmlResource.getFuncNodeCode(), noRelationItems));
        }
        noRelationItems = noRelationItemsMap.get("modifier");
        if (null != noRelationItems && !noRelationItems.isEmpty()) {
            result.addResultElement(
                    ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("功能编码为: %s,节点标识nodekey为：%s的单据模板的审计页签中，没有最后修改人modifier. \n",
                            xmlResource.getFuncNodeCode(), noRelationItems));
        }
        noRelationItems = noRelationItemsMap.get("modifiedtime");
        if (null != noRelationItems && !noRelationItems.isEmpty()) {
            result.addResultElement(
                    ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("功能编码为: %s,节点标识nodekey为：%s的单据模板的审计页签中，没有最后修改时间modifiedtime. \n",
                            xmlResource.getFuncNodeCode(), noRelationItems));
        }
    }

}
