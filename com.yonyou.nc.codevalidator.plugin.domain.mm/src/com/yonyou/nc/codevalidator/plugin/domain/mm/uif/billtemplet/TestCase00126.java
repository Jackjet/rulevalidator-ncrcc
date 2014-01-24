package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.billtemplet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
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
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 单据号编码规则中默认应该有3段：固定值+业务日期+流水号
 * 
 * @author gaojf
 */
@RuleDefinition(executeLayer = ExecuteLayer.BUSICOMP, executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, description = "单据号编码规则中默认应该有3段：固定值+业务日期+流水号", relatedIssueId = "126", coder = "gaojf", solution = "单据号编码规则中默认应该有3段：固定值+业务日期+流水号")
public class TestCase00126 extends AbstractXmlRuleDefinition {

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
                    "请检查输入的功能编码是否正确！\n");
            return result;
        }
        for (XmlResource xmlResource : resources) {

            String billtypeid = this.queryBillTypeId(xmlResource.getFuncNodeCode(), ruleExecContext);
            List<String[]> billCodeBaseResult = this.queryBillCodeBase(billtypeid, ruleExecContext);
            if (MMValueCheck.isEmpty(billCodeBaseResult)) {
                result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                        "没有查到单据类型规则编码\n");
                return result;
            }
            for (String[] ruleandcode : billCodeBaseResult) {
                Set<Integer> elemTypes = this.queryElemStyles(ruleandcode, ruleExecContext);
                if (elemTypes == null || elemTypes.size() != 3) {
                    result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                            String.format("单据号规则[ %s]中默认应该有3段：固定值+业务日期+流水号. \n", ruleandcode[0]));
                    continue;
                }
                // 元素类型：常量0，业务实体1，时间类型2，流水号3
                if (!elemTypes.contains(0) || !elemTypes.contains(2) || !elemTypes.contains(3)) {
                    result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                            String.format("单据号规则[ %s]中默认应该有3段：固定值+业务日期+流水号. \n", ruleandcode[0]));
                }
            }
        }
        return result;
    }

    /**
     * 根据节点号找到对应的单据类型id
     * 
     * @param funcode
     * @param ruleExecContext
     * @return
     * @throws RuleBaseException
     */
    private String queryBillTypeId(String funcode, IRuleExecuteContext ruleExecContext) throws RuleBaseException {

        StringBuilder sql = new StringBuilder();
        sql.append(" select t.pk_billtypeid from bd_billtype t where  t.nodecode = '");
        sql.append(funcode);
        sql.append("' and t.pk_group = '~'");

        DataSet dataSet = null;
        dataSet = SQLQueryExecuteUtils.executeQuery(sql.toString(), ruleExecContext.getRuntimeContext());
        if (MMValueCheck.isEmpty(dataSet) || dataSet.getRows().size() == 0) {
            return null;
        }
        return (String) dataSet.getRow(0).getValue("pk_billtypeid");
    }

    /**
     * 根据单据类型查询规则名和规则主键
     * 
     * @param billtypeid
     * @param ruleExecContext
     * @return
     * @throws RuleBaseException
     */
    private List<String[]> queryBillCodeBase(String billtypeid, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {

        StringBuilder sql = new StringBuilder();
        sql.append(" select r.rulename,r.pk_billcodebase from pub_bcr_rulebase r "
                + "where r.dr=0 and r.dr is not null and r.nbcrcode = '" + billtypeid + "'");
        DataSet dataSet = null;
        dataSet = SQLQueryExecuteUtils.executeQuery(sql.toString(), ruleExecContext.getRuntimeContext());
        if (MMValueCheck.isEmpty(dataSet)) {
            return null;
        }

        List<String[]> billCodeBaseResult = new ArrayList<String[]>();
        for (DataRow dataRow : dataSet.getRows()) {
            List<String> ruleNameAndPkcode = new ArrayList<String>();
            ruleNameAndPkcode.add((String) dataRow.getValue("rulename"));
            ruleNameAndPkcode.add((String) dataRow.getValue("pk_billcodebase"));
            billCodeBaseResult.add(ruleNameAndPkcode.toArray(new String[ruleNameAndPkcode.size()]));
        }
        return billCodeBaseResult;
    }

    /**
     * 根据规则主键查询出单据号元素类型
     * 
     * @param billtypeid
     * @param ruleExecContext
     * @return
     * @throws RuleBaseException
     */
    private Set<Integer> queryElemStyles(String[] ruleandcode, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {

        StringBuilder sql = new StringBuilder();
        sql.append("select e.elemtype from pub_bcr_elem e ");
        sql.append("where e.pk_billcodebase ='");
        if (ruleandcode.length == 2) {
            sql.append(ruleandcode[1] + "'");
        }
        else {
            return null;
        }
        sql.append("and e.dr= 0 and e.dr is not null ");
        DataSet dataSet = null;
        dataSet = SQLQueryExecuteUtils.executeQuery(sql.toString(), ruleExecContext.getRuntimeContext());
        if (MMValueCheck.isEmpty(dataSet)) {
            return null;
        }
        Set<Integer> elemTypes = new HashSet<Integer>();
        for (DataRow dataRow : dataSet.getRows()) {
            elemTypes.add((Integer) dataRow.getValue("elemtype"));
        }
        return elemTypes;
    }
}
