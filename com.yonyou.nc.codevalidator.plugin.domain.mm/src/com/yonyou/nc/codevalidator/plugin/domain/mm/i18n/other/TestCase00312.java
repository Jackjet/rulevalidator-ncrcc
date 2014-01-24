package com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
 * 检查菜单名称是否做多语处理
 * 
 * @since 6.0
 * @version 2013-9-1 上午9:38:28
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_COMMON, description = "检查菜单名称是否做多语处理", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "添加该菜单名称多语，即 sm_menuitemreg对应的resid不为空 ", coder = "zhongcha", relatedIssueId = "312")
public class TestCase00312 extends AbstractLangRuleDefinition implements IGlobalRuleDefinition {

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
        // 数据库中resid不存在的MapList<功能节点,菜单名>
        MapList<String, String> notInDbFun2menus = new MapList<String, String>();
        // 多语文件中不存在多语的MapList<功能节点,菜单名>
        MapList<String, String> notInFileFun2menus = new MapList<String, String>();
        // 数据库中resid不存在的菜单名，但其没有对应的功能节点
        List<String> notInDbMenuList = new ArrayList<String>();
        // 多语文件中不存在多语的菜单名，但其没有对应的功能节点
        List<String> notInFileMenuList = new ArrayList<String>();
        for (DataRow dataRow : ds.getRows()) {
            String funcode = (String) dataRow.getValue("funcode");
            String menuItemName = (String) dataRow.getValue("menuitemname");
            if (MMValueCheck.isNotEmpty(funcodes)) {
                notDataFuncodes.remove(funcode);
            }
            if (MMValueCheck.isEmpty((String) dataRow.getValue(MmMDi18nConstants.RESID))) {
                if (MMValueCheck.isNotEmpty(funcodes)) {
                    notInDbFun2menus.put(funcode, menuItemName);
                }
                else {
                    notInDbMenuList.add(menuItemName);
                }
            }
            else if (QueryLangRuleValidatorUtil.notInLRV((String) dataRow.getValue(MmMDi18nConstants.RESID),
                    ruleExecContext.getRuntimeContext())) {
                if (MMValueCheck.isNotEmpty(funcodes)) {
                    notInFileFun2menus.put(funcode, menuItemName);
                }
                else {
                    notInFileMenuList.add(menuItemName);
                }
            }
        }
        if (MMValueCheck.isNotEmpty(notInDbFun2menus)) {
            for (Entry<String, List<String>> entry : notInDbFun2menus.entrySet()) {
                result.addResultElement(entry.getKey() + MmMDi18nConstants.ERROR_DATABASE_NULL,
                        "功能节点号为[" + entry.getKey() + "]的菜单[" + entry.getValue() + "]未做多语。\n");
            }
        }
        if (MMValueCheck.isNotEmpty(notInFileFun2menus)) {
            for (Entry<String, List<String>> entry : notInFileFun2menus.entrySet()) {
                result.addResultElement(entry.getKey() + MmMDi18nConstants.ERROR_LANGFILE_NULL,
                        "功能节点号为[" + entry.getKey() + "]的菜单[" + entry.getValue()
                                + "]在所属模块对应的多语jar中lang\\simpchn\\文件夹下元数据多语文件中不存在该元数据名称多语！\n");
            }
        }
        if (MMValueCheck.isNotEmpty(notInDbMenuList)) {
            result.addResultElement(MmMDi18nConstants.ERROR_DATABASE_NULL, "菜单名为" + notInDbMenuList + "未做多语。\n");

        }
        if (MMValueCheck.isNotEmpty(notInFileMenuList)) {
            result.addResultElement(MmMDi18nConstants.ERROR_LANGFILE_NULL, "菜单名为" + notInFileMenuList
                    + "在所属模块对应的多语jar中lang\\simpchn\\文件夹下元数据多语文件中不存在该元数据名称多语！\n");

        }
        if (MMValueCheck.isNotEmpty(notDataFuncodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "功能节点编码为" + notDataFuncodes
                    + "的节点不存在所需检查的数据。\n");
        }
        return result;
    }

    private String getSql(String[] funcodes) {
        StringBuilder result = new StringBuilder();
        result.append("select distinct resid,menuitemname,funcode from sm_menuitemreg  where  isnull(dr,0)=0 ");
        if (!MMValueCheck.isEmpty(funcodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("funcode", funcodes));
        }
        return result.toString();
    }

}
