package com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.querytemplet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
 * 检查查询模板非元数据字段名称多语
 * 
 * @since 6.0
 * @version 2013-8-31 下午9:10:28
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_QUERYTEMP, description = "检查查询模板非元数据字段名称多语", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "添加该查询模板非元数据字段名称多语，即对应pub_query_condition中if_notmdcondition=Y的字段对应的resid不为空", coder = "zhongcha", relatedIssueId = "310")
public class TestCase00310 extends AbstractLangRuleDefinition implements IGlobalRuleDefinition {

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] funnodes = ruleExecContext.getParameterArray(CommonRuleParams.FUNCNODEPARAM);
        List<String> notDataFunnodes = new LinkedList<String>();
        if (MMValueCheck.isNotEmpty(funnodes)) {
            notDataFunnodes.addAll(Arrays.asList(funnodes));
        }
        else {
            Logger.warn("当前规则功能节点参数为空，将检查所有功能节点！");
        }
        // 存放resid为空的错误信息 Map<功能节点编码, MapList<查询模板名称, 非元数据字段>>
        Map<String, MapList<String, String>> notInDbErrorMap = new HashMap<String, MapList<String, String>>();
        // 多语文件中不存在的错误信息 Map<功能节点编码, MapList<查询模板名称, 非元数据字段>>
        Map<String, MapList<String, String>> notInFileErrorMap = new HashMap<String, MapList<String, String>>();
        DataSet ds = this.executeQuery(ruleExecContext, this.getSql(funnodes));
        for (DataRow dataRow : ds.getRows()) {
            String queryTempName = (String) dataRow.getValue(MmMDi18nConstants.MODEL_NAME);
            String field = (String) dataRow.getValue("field_name");
            String funnode = (String) dataRow.getValue("funnode");
            if (MMValueCheck.isNotEmpty(funnodes)) {
                notDataFunnodes.remove(funnode);
            }
            if (MMValueCheck.isEmpty(field)) {
                field = (String) dataRow.getValue("field_code");
            }
            if (MMValueCheck.isEmpty(dataRow.getValue(MmMDi18nConstants.RESID))) {
                // 添加错误信息
                if (notInDbErrorMap.keySet().contains(funnode)
                        && notInDbErrorMap.get(funnode).keySet().contains(queryTempName)) {
                    notInDbErrorMap.get(funnode).put(queryTempName, field);
                }
                else {
                    MapList<String, String> templet2items = new MapList<String, String>();
                    templet2items.put(queryTempName, field);
                    notInDbErrorMap.put(funnode, templet2items);
                }
            }
            else if (QueryLangRuleValidatorUtil.notInLRV((String) dataRow.getValue(MmMDi18nConstants.RESID),
                    ruleExecContext.getRuntimeContext())) {
                // 添加错误信息
                if (notInFileErrorMap.keySet().contains(funnode)
                        && notInFileErrorMap.get(funnode).keySet().contains(queryTempName)) {
                    notInFileErrorMap.get(funnode).put(queryTempName, field);
                }
                else {
                    MapList<String, String> templet2items = new MapList<String, String>();
                    templet2items.put(queryTempName, field);
                    notInFileErrorMap.put(funnode, templet2items);
                }
            }
        }

        if (MMValueCheck.isNotEmpty(notInDbErrorMap)) {
            for (Entry<String, MapList<String, String>> outEntry : notInDbErrorMap.entrySet()) {
                StringBuilder errorContxt1 = new StringBuilder();
                for (Entry<String, List<String>> inEntry : outEntry.getValue().entrySet()) {
                    errorContxt1.append("查询模板名称[" + inEntry.getKey() + "]的非元数据字段：" + inEntry.getValue() + "未做多语。\n");
                }
                result.addResultElement(outEntry.getKey() + MmMDi18nConstants.ERROR_DATABASE_NULL,
                        errorContxt1.toString());
            }
        }
        if (MMValueCheck.isNotEmpty(notInFileErrorMap)) {
            for (Entry<String, MapList<String, String>> outEntry : notInFileErrorMap.entrySet()) {
                StringBuilder errorContxt1 = new StringBuilder();
                for (Entry<String, List<String>> inEntry : outEntry.getValue().entrySet()) {
                    errorContxt1.append("查询模板名称[" + inEntry.getKey() + "]的非元数据字段：" + inEntry.getValue()
                            + "在所属模块对应的多语jar中lang\\simpchn\\文件夹下多语文件中不存在该查询模板这些字段名称的多语。\n");
                }
                result.addResultElement(outEntry.getKey() + MmMDi18nConstants.ERROR_DATABASE_NULL,
                        errorContxt1.toString());
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
        result.append("select distinct pc.resid,pc.field_code,pc.field_name,pt.model_name,pb.funnode from pub_query_condition pc,pub_query_templet pt, pub_systemplate_base pb  where pt.id=pc.pk_templet and pc.if_notmdcondition='Y' and pc.pk_templet=pb.templateid and pb.tempstyle='1' ");
        if (!MMValueCheck.isEmpty(funnodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("pb.funnode", funnodes));
        }
        return result.toString();
    }

}
