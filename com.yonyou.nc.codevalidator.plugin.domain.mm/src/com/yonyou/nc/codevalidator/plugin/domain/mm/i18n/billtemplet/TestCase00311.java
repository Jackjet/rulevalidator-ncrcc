package com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.billtemplet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractLangRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.utils.CommonRuleParams;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IGlobalRuleDefinition;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 功能节点名称多语处理检查
 * 
 * @author qiaoyang
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_COMMON, description = "功能节点名称多语处理检查", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "", coder = "qiaoyang", relatedIssueId = "311")
public class TestCase00311 extends AbstractLangRuleDefinition implements IGlobalRuleDefinition {

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] funcNodes = ruleExecContext.getParameterArray(CommonRuleParams.FUNCNODEPARAM);
        if (funcNodes != null && funcNodes.length > 0) {
            this.check(ruleExecContext, Arrays.asList(funcNodes), result);
        }
        else {
            List<String> funcodes = new ArrayList<String>();
            String sql = "select funcode from sm_funcregister";
            DataSet ds = SQLQueryExecuteUtils.executeQuery(sql, ruleExecContext.getRuntimeContext());
            for (DataRow dataRow : ds.getRows()) {
                funcodes.add((String) dataRow.getValue("funcode"));
            }
            this.check(ruleExecContext, funcodes, result);
        }
        return result;
    }

    private void check(IRuleExecuteContext ruleExecContext, List<String> funcodes, ResourceRuleExecuteResult result)
            throws RuleBaseException {
        String langsql =
                "select id from " + SQLQueryExecuteUtils.getCurrentMultiLangTableName() + "  where "
                        + SQLQueryExecuteUtils.buildSqlForIn("id", funcodes.toArray(new String[0]));
        DataSet langds = SQLQueryExecuteUtils.executeMultiLangQuery(langsql, ruleExecContext.getRuntimeContext());
        List<String> langids = new ArrayList<String>();
        for (DataRow dataRow : langds.getRows()) {
            String funcnode = (String) dataRow.getValue("id");
            langids.add(funcnode);
        }
        if (MMValueCheck.isNotEmpty(funcodes)) {
            result.addResultElement("功能节点名称多语", "功能节点编码为" + funcodes
                    + "的功能节点名称的多语在所属模块对应的多语jar中lang\\simpchn\\funcode文件夹下多语文件中不存在！");
        }
    }
}
