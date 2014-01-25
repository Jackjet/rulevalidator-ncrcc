package com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.billtemplet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.util.QueryLangRuleValidatorUtil;
import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMDi18nConstants;
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
 * 主表、子表页签名称多语处理
 * 
 * @author qiaoyang
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_COMMON, description = "主表、子表页签名称多语处理", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "", coder = "qiaoyang", relatedIssueId = "307")
public class TestCase00307 extends AbstractLangRuleDefinition implements IGlobalRuleDefinition {

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        StringBuffer noteBuilder = new StringBuffer();
        String[] funcNodes = ruleExecContext.getParameterArray(CommonRuleParams.FUNCNODEPARAM);
        List<String> notDataFunnodes = new ArrayList<String>();
        if (MMValueCheck.isNotEmpty(funcNodes)) {
            List<String> funcnodes = Arrays.asList(funcNodes);
            DataSet ds = SQLQueryExecuteUtils.executeQuery(this.getSql(funcNodes), ruleExecContext.getRuntimeContext());
            for (DataRow dataRow : ds.getRows()) {
                String funnode = (String) dataRow.getValue("funnode");
                String templetName = (String) dataRow.getValue(MmMDi18nConstants.BILL_TEMPLETCAPTION);
                String tabName = (String) dataRow.getValue(MmMDi18nConstants.BILL_TABNAME);
                String resId = (String) dataRow.getValue(MmMDi18nConstants.RESID);
                if (resId == null) {
                    noteBuilder.append(templetName + "(" + funnode + ")中的" + tabName + "页签名称未做多语\n");
                }
                else if (QueryLangRuleValidatorUtil.notInLRV(resId, ruleExecContext.getRuntimeContext())) {
                    noteBuilder.append(templetName + "(" + funnode + ")中的" + tabName + "页签多语资源(" + resId
                            + ")不存在于对应的环境多语文件中\n");
                }
                if (!funcnodes.contains(funnode)) {
                    notDataFunnodes.add(funnode);
                }
            }
            if (notDataFunnodes.size() > 0) {
                noteBuilder.append(notDataFunnodes.toString() + "未找到单据模板页签\n");
            }
        }
        else {
            DataSet ds = SQLQueryExecuteUtils.executeQuery(this.getSql(funcNodes), ruleExecContext.getRuntimeContext());
            for (DataRow dataRow : ds.getRows()) {
                String funnode = (String) dataRow.getValue("funnode");
                String templetName = (String) dataRow.getValue(MmMDi18nConstants.BILL_TEMPLETCAPTION);
                String tabName = (String) dataRow.getValue(MmMDi18nConstants.BILL_TABNAME);
                String resId = (String) dataRow.getValue(MmMDi18nConstants.RESID);
                if (resId == null) {
                    noteBuilder.append(templetName + "(" + funnode + ")中的" + tabName + "页签名称未做多语\n");
                }
                else if (QueryLangRuleValidatorUtil.notInLRV(resId, ruleExecContext.getRuntimeContext())) {
                    noteBuilder.append(templetName + "(" + funnode + ")中的" + tabName + "页签多语资源(" + resId
                            + ")不存在于对应的环境多语文件中\n");
                }
            }
        }
        if (noteBuilder.length() > 0) {
            result.addResultElement("页签名称多语", noteBuilder.toString());
        }

        return result;
    }

    private String getSql(String[] funnodes) {
        StringBuilder result = new StringBuilder();
        result.append("select distinct pt.resid,p.bill_templetcaption,pt.tabname,pb.funnode from pub_billtemplet_t pt ,pub_systemplate_base pb, pub_billtemplet p where pt.pk_billtemplet=pb.templateid and p.pk_billtemplet = pt.pk_billtemplet and p.pk_billtemplet=pb.templateid  and pb.tempstyle='0' ");
        if (!MMValueCheck.isEmpty(funnodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("pb.funnode", funnodes));
        }
        result.append(" order by pb.funnode");
        return result.toString();
    }

}
