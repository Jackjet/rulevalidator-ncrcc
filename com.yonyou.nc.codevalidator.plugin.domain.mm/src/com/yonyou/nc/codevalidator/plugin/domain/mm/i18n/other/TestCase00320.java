package com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.other;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.util.QueryLangRuleValidatorUtil;
import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMDi18nConstants;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractLangRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
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
 * 检查支持数据权限后业务实体管理中操作名称多语处理
 * 
 * @since 6.0
 * @version 2013-9-1 下午12:49:01
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_COMMON, description = "检查支持数据权限后业务实体管理中操作名称多语处理", specialParamDefine = {
    "模块编码"
}, solution = "添加支持数据权限后业务实体管理中操作名称多语，即sm_res_operation对应的resid非空  ", coder = "zhongcha", relatedIssueId = "320", memo = "多个参数请用,隔开")
public class TestCase00320 extends AbstractLangRuleDefinition implements IGlobalRuleDefinition {

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] modules = ruleExecContext.getParameterArray(MmMDi18nConstants.MODULE_CODE);
        List<String> notDataModules = new LinkedList<String>();
        if (MMValueCheck.isNotEmpty(modules)) {
            notDataModules.addAll(Arrays.asList(modules));
        }
        else {
            Logger.warn("当前规则功能节点参数为空，将检查所有模块！");
        }
        DataSet ds = this.executeQuery(ruleExecContext, this.getSql(modules));
        MapList<String, String> errorMapList1 = new MapList<String, String>();
        MapList<String, String> errorMapList2 = new MapList<String, String>();
        for (int i = 0; i < ds.getRowCount(); i++) {
            String operationName = (String) ds.getValue(i, "operationname");
            String ownmodule = (String) ds.getValue(i, "ownmodule");
            if (MMValueCheck.isNotEmpty(modules)) {
                notDataModules.remove(ownmodule);
            }
            if (MMValueCheck.isEmpty(ds.getValue(i, MmMDi18nConstants.RESID))) {
                errorMapList1.put((String) ds.getValue(i, "ownmodule"), operationName);
            }
            else if (QueryLangRuleValidatorUtil.notInLRV((String) ds.getValue(i, MmMDi18nConstants.RESID),
                    ruleExecContext.getRuntimeContext())) {
                errorMapList2.put((String) ds.getValue(i, "ownmodule"), operationName);
            }
        }
        if (MMValueCheck.isNotEmpty(errorMapList1)) {
            for (Map.Entry<String, List<String>> entry : errorMapList1.entrySet()) {
                result.addResultElement(entry.getKey() + MmMDi18nConstants.ERROR_DATABASE_NULL,
                        "模块编码为[" + entry.getKey() + "]支持数据权限后业务实体管理中操作名称为" + entry.getValue() + "未做多语。\n");
            }
        }
        if (MMValueCheck.isNotEmpty(errorMapList2)) {
            for (Map.Entry<String, List<String>> entry : errorMapList1.entrySet()) {
                result.addResultElement(entry.getKey() + MmMDi18nConstants.ERROR_LANGFILE_NULL,
                        "模块编码为[" + entry.getKey() + "]支持数据权限后业务实体管理中操作名称为" + entry.getValue()
                                + "在所属模块对应的多语jar中lang\\simpchn\\文件夹下元数据多语文件中不存在该元数据名称多语。\n");
            }
        }

        if (MMValueCheck.isNotEmpty(notDataModules)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "模块编码为" + notDataModules + "不存在所需检查的数据。\n");
        }
        return result;
    }

    private String getSql(String[] modules) {
        StringBuilder result = new StringBuilder();
        result.append("select distinct resid,operationname,ownmodule from sm_res_operation where  isnull(dr,0)=0 ");
        if (!MMValueCheck.isEmpty(modules)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("ownmodule", modules));
        }
        return result.toString();
    }
}
