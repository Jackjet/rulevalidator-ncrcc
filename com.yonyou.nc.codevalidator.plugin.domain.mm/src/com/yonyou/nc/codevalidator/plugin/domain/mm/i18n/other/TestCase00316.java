package com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.other;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
 * 检查业务活动名称是否做多语处理
 * 
 * @since 6.0
 * @version 2013-9-1 下午12:19:20
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_COMMON, description = "检查业务活动名称是否做多语处理", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "添加该业务活动名称多语， 即sm_busiactivereg的resid非空 ", coder = "zhongcha", relatedIssueId = "316")
public class TestCase00316 extends AbstractLangRuleDefinition implements IGlobalRuleDefinition {

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] funcodes = ruleExecContext.getParameterArray(CommonRuleParams.FUNCNODEPARAM);
        List<String> notDataFuncodes = new LinkedList<String>();
        if (MMValueCheck.isNotEmpty(funcodes)) {
            notDataFuncodes.addAll(Arrays.asList(funcodes));
        }
        else {
            Logger.warn("当前规则功能节点参数为空，将检查所有功能节点！");
        }
        DataSet ds = this.executeQuery(ruleExecContext, this.getSql(funcodes));

        MapList<String, String> errorMapList1 = new MapList<String, String>();
        MapList<String, String> errorMapList2 = new MapList<String, String>();
        for (DataRow dataRow : ds.getRows()) {
            String funcode = (String) dataRow.getValue("funcode");
            if (MMValueCheck.isNotEmpty(funcodes)) {
                notDataFuncodes.remove(funcode);
            }
            String activeName = (String) dataRow.getValue("name");

            if (MMValueCheck.isEmpty(dataRow.getValue(MmMDi18nConstants.RESID))) {
                errorMapList1.put(funcode, activeName);
            }
            else if (QueryLangRuleValidatorUtil.notInLRV((String) dataRow.getValue(MmMDi18nConstants.RESID),
                    ruleExecContext.getRuntimeContext())) {
                errorMapList2.put(funcode, activeName);
            }
        }
        if (MMValueCheck.isNotEmpty(errorMapList1)) {
            for (Map.Entry<String, List<String>> entry : errorMapList1.entrySet()) {
                result.addResultElement(entry.getKey() + MmMDi18nConstants.ERROR_DATABASE_NULL,
                        "功能节点编码为[" + entry.getKey() + "]的业务活动名称为" + entry.getValue() + "未做多语。\n");
            }
        }
        if (MMValueCheck.isNotEmpty(errorMapList2)) {
            for (Map.Entry<String, List<String>> entry : errorMapList1.entrySet()) {
                result.addResultElement(entry.getKey() + MmMDi18nConstants.ERROR_LANGFILE_NULL,
                        "功能节点编码为[" + entry.getKey() + "]的业务活动名称为" + entry.getValue()
                                + "所属模块对应的多语jar中lang\\simpchn\\文件夹下元数据多语文件中不存在该业务活动名称的多语。\n");
            }
        }
        if (MMValueCheck.isNotEmpty(notDataFuncodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "功能节点编码为" + notDataFuncodes
                    + "的节点不存在所需检查的数据。\n");
        }
        return result;
    }

    private String getSql(String[] funcodes) {
        StringBuilder result = new StringBuilder();
        result.append("select distinct sb.resid,sb.name,sf.funcode from sm_busiactivereg sb,sm_funcregister sf where sf.cfunid=sb.parent_id and isnull(sb.dr,0)=0  and isnull(sf.dr,0)=0");
        if (!MMValueCheck.isEmpty(funcodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("sf.funcode", funcodes));
        }
        return result.toString();
    }

}
