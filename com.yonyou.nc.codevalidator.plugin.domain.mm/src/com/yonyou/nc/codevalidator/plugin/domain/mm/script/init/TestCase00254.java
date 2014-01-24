package com.yonyou.nc.codevalidator.plugin.domain.mm.script.init;

import com.yonyou.nc.codevalidator.plugin.domain.mm.uif.util.SQLBuilder;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.IGlobalRuleDefinition;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * VO交换时应该自动根据上下游接口对应关系补充目的单据的交易类型
 * 
 * @since 6.0
 * @version 2013-11-14 下午2:46:37
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.PRESCRIPT, subCatalog = SubCatalogEnum.PS_CONTENTCHECK, description = "VO交换时应该自动根据上下游接口对应关系补充目的单据的交易类型", specialParamDefine = {
    "来源单据类型", "目的单据类型"
}, solution = "添加该单据类型名称多语，即bd_billtype中billtypename字段不为空，且 istransaction='N' ", coder = "zhongcha", relatedIssueId = "254", memo = "参数可空，两个参数值都为空则默认检查所有的数据。多个参数请用，隔开")
public class TestCase00254 extends AbstractScriptQueryRuleDefinition implements IGlobalRuleDefinition {

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] srcbilltypes = ruleExecContext.getParameterArray("来源单据类型");
        String[] destbilltypes = ruleExecContext.getParameterArray("目的单据类型");
        if (MMValueCheck.isEmpty(srcbilltypes) || srcbilltypes[0].trim().equals("")) {
            srcbilltypes = null;
        }
        if (MMValueCheck.isEmpty(destbilltypes) || destbilltypes[0].trim().equals("")) {
            destbilltypes = null;
        }
        DataSet ds = this.executeQuery(ruleExecContext, this.getSql(srcbilltypes, destbilltypes));
        MapList<String, String> errorMapList = new MapList<String, String>();
        for (int i = 0; i < ds.getRowCount(); i++) {
            String srcbilltype = (String) ds.getValue(i, "src_billtype");
            String destbilltype = (String) ds.getValue(i, "dest_billtype");
            if (MMValueCheck.isEmpty(ds.getValue(i, "dest_transtype"))) {
                errorMapList.put(srcbilltype, destbilltype);
            }
        }
        StringBuilder errorContxt = new StringBuilder();
        if (MMValueCheck.isNotEmpty(errorMapList)) {
            for (String srcbilltype : errorMapList.keySet()) {
                errorContxt.append("来源单据类型为[ " + srcbilltype + " ]目的单据类型为" + errorMapList.get(srcbilltype)
                        + "在单据接口定义中没有设置默认目的单据的交易类型。\n");
            }
        }
        if (MMValueCheck.isNotEmpty(errorContxt)) {
            result.addResultElement("错误", errorContxt.toString());
        }
        return result;
    }

    private String getSql(String[] srcbilltypes, String[] destbilltypes) {
        SQLBuilder sql = new SQLBuilder();
        sql.append("select distinct src_billtype,dest_billtype,dest_transtype from pub_billitfdef  where   isnull(dr,0)=0");
        if (MMValueCheck.isNotEmpty(srcbilltypes) || MMValueCheck.isNotEmpty(destbilltypes)) {
            sql.append(" and ");
        }
        if (MMValueCheck.isNotEmpty(srcbilltypes)) {
            sql.append("src_billtype", srcbilltypes);
        }
        if (MMValueCheck.isNotEmpty(srcbilltypes) && MMValueCheck.isNotEmpty(destbilltypes)) {
            sql.append(" and ");
        }
        if (MMValueCheck.isNotEmpty(destbilltypes)) {
            sql.append("dest_billtype", destbilltypes);
        }
        return sql.toString();
    }
}
