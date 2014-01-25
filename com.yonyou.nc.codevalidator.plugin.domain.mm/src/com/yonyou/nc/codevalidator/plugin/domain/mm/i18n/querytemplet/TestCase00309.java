package com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.querytemplet;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.util.QueryLangRuleValidatorUtil;
import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMDi18nConstants;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
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
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * 检查查询模板名称是否存在多语
 * 
 * @since 6.0
 * @version 2013-8-31 下午7:45:18
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_QUERYTEMP, description = "检查查询模板名称是否存在多语", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "添加该查询模板名称多语，即对应pub_query_templet中resid不为空 ", coder = "zhongcha", relatedIssueId = "309")
public class TestCase00309 extends AbstractLangRuleDefinition implements IGlobalRuleDefinition {

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] funnodes = ruleExecContext.getParameterArray(CommonRuleParams.FUNCNODEPARAM);
        List<String> notDataFunnodes = new LinkedList<String>();
        if (MMValueCheck.isNotEmpty(funnodes)) {
            notDataFunnodes.addAll(Arrays.asList(funnodes));
            // notDataFunnodes = new LinkedList<String>();
        }
        else {
            Logger.warn("当前规则功能节点参数为空，将检查所有功能节点！");
        }
        MapList<String, String> fun2qTemp1 = new MapList<String, String>();
        MapList<String, String> fun2qTemp2 = new MapList<String, String>();
        DataSet ds = this.executeQuery(ruleExecContext, this.getSql(funnodes));
        for (DataRow dataRow : ds.getRows()) {
            String queryTempName = (String) dataRow.getValue(MmMDi18nConstants.MODEL_NAME);
            String funnode = (String) dataRow.getValue("funnode");
            if (MMValueCheck.isNotEmpty(funnodes)) {
                notDataFunnodes.remove(funnode);
            }
            if (MMValueCheck.isEmpty(dataRow.getValue(MmMDi18nConstants.RESID))) {
                fun2qTemp1.put(funnode, queryTempName);
            }
            else if (QueryLangRuleValidatorUtil.notInLRV((String) dataRow.getValue(MmMDi18nConstants.RESID),
                    ruleExecContext.getRuntimeContext())) {
                fun2qTemp2.put(funnode, queryTempName);
            }
        }

        if (MMValueCheck.isNotEmpty(fun2qTemp1)) {
            for (String funnode : fun2qTemp1.keySet()) {
                result.addResultElement(funnode + MmMDi18nConstants.ERROR_DATABASE_NULL, "功能节点编码为[" + funnode
                        + "]的查询模板名称" + fun2qTemp1.get(funnode) + "未做多语。\n");

            }
        }
        if (MMValueCheck.isNotEmpty(fun2qTemp2)) {
            for (String funnode : fun2qTemp2.keySet()) {
                result.addResultElement(funnode + MmMDi18nConstants.ERROR_LANGFILE_NULL, "功能节点编码为[" + funnode
                        + "]的查询模板名称" + fun2qTemp2.get(funnode)
                        + "在所属模块对应的多语jar中lang\\simpchn\\文件夹下多语文件中不存在该查询模板名称的多语。\n");

            }
        }

        if (MMValueCheck.isNotEmpty(notDataFunnodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "功能节点编码为" + notDataFunnodes
                    + "的节点不存在所需检查的数据。\n");
        }
        return result;
    }

    private String getSql(String[] funnodes) {
        StringBuilder result = new StringBuilder();
        result.append("select distinct pt.resid,pt.model_name,pb.funnode from pub_query_templet pt, pub_systemplate_base pb where pt.id=pb.templateid and pb.tempstyle='1'");
        if (!MMValueCheck.isEmpty(funnodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("pb.funnode", funnodes));
        }
        return result.toString();
    }

}
