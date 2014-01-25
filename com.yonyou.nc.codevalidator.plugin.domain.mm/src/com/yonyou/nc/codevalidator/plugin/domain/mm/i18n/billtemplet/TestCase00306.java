package com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.billtemplet;

import java.util.Arrays;
import java.util.HashMap;
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
 * 检查单据模板的名称是否做了多语
 * 
 * @since 6.0
 * @version 2013-8-31 下午5:20:06
 * @author zhongcha
 */
@RuleDefinition(catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_BILLTEMP, description = "检查单据模板的名称是否做多语", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "添加该单据模板名称多语，即对应pub_billtemplet中resid不为空 ", coder = "zhongcha", relatedIssueId = "306", executeLayer = ExecuteLayer.GLOBAL)
public class TestCase00306 extends AbstractLangRuleDefinition implements IGlobalRuleDefinition {

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
        DataSet ds = this.executeQuery(ruleExecContext, this.getSql(funnodes));
        MapList<String, String> notInDbErrorMapList = new MapList<String, String>();
        MapList<String, String> notInFileErrorMapList = new MapList<String, String>();
        Map<String, String> inDbResid2Fun = new HashMap<String, String>();
        Map<String, String> inDbResid2TempletNm = new HashMap<String, String>();
        for (DataRow dataRow : ds.getRows()) {
            String funnode = (String) dataRow.getValue("funnode");
            if (MMValueCheck.isNotEmpty(funnodes)) {
                notDataFunnodes.remove(funnode);
            }
            String templetName = (String) dataRow.getValue(MmMDi18nConstants.BILL_TEMPLETCAPTION);
            String resId = (String) dataRow.getValue(MmMDi18nConstants.RESID);
            if (MMValueCheck.isEmpty(resId)) {
                notInDbErrorMapList.put(funnode, templetName);
            }
            else {
                inDbResid2Fun.put(resId, funnode);
                inDbResid2TempletNm.put(resId, templetName);
            }
        }
        if (MMValueCheck.isNotEmpty(inDbResid2Fun)) {
            List<String> notInLRVResids =
                    QueryLangRuleValidatorUtil.notInLRV(inDbResid2Fun.keySet().toArray(new String[] {}),
                            ruleExecContext.getRuntimeContext());
            if (MMValueCheck.isNotEmpty(notInLRVResids)) {
                for (String resid : notInLRVResids) {
                    notInFileErrorMapList.put(inDbResid2Fun.get(resid), inDbResid2TempletNm.get(resid));
                }
            }
        }
        if (MMValueCheck.isNotEmpty(notInDbErrorMapList)) {
            for (Map.Entry<String, List<String>> entry : notInDbErrorMapList.entrySet()) {
                result.addResultElement(entry.getKey() + MmMDi18nConstants.ERROR_DATABASE_NULL,
                        "单据模板名称为" + entry.getValue() + "未做多语！\n");
            }
        }
        if (MMValueCheck.isNotEmpty(notInFileErrorMapList)) {
            for (Map.Entry<String, List<String>> entry : notInFileErrorMapList.entrySet()) {
                result.addResultElement(entry.getKey() + MmMDi18nConstants.ERROR_LANGFILE_NULL,
                        "单据模板名称为" + entry.getValue() + "所属模块对应的多语jar中lang\\simpchn\\文件夹下多语文件中不存在该模板名称多语！\n");
            }
        }
        // }
        if (MMValueCheck.isNotEmpty(notDataFunnodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "功能节点编码为" + notDataFunnodes
                    + "的节点不存在所需检查的数据。\n");
        }
        return result;
    }

    private String getSql(String[] funnodes) {
        StringBuilder result = new StringBuilder();
        result.append("select distinct resid,bill_templetcaption,pb.funnode from pub_billtemplet pt ,pub_systemplate_base pb where pt.pk_billtemplet=pb.templateid and pb.tempstyle='0' and isnull(pt.dr,0)=0 and isnull(pb.dr,0)=0 ");
        if (!MMValueCheck.isEmpty(funnodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("pb.funnode", funnodes));
        }
        return result.toString();
    }
}
