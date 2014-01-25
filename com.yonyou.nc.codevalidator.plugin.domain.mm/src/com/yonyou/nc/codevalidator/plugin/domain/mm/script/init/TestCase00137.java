package com.yonyou.nc.codevalidator.plugin.domain.mm.script.init;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMDi18nConstants;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
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
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * 物料、客户、供应商、来源单据支持超链接联查
 * 
 * @author qiaoyanga
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.PRESCRIPT, description = "物料、客户、供应商、来源单据支持超链接联查", memo = "", solution = "", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, subCatalog = SubCatalogEnum.PS_CONTENTCHECK, relatedIssueId = "137", coder = "qiaoyanga")
public class TestCase00137 extends AbstractScriptQueryRuleDefinition implements IGlobalRuleDefinition {

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws ResourceParserException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] funnodes = ruleExecContext.getParameterArray(CommonRuleParams.FUNCNODEPARAM);
        List<String> notDataFunnodes = new LinkedList<String>();
        if (MMValueCheck.isNotEmpty(funnodes)) {
            notDataFunnodes.addAll(Arrays.asList(funnodes));
        }
        else {
            Logger.warn("当前规则功能节点参数为空，将检查所有功能节点！");
        }
        Map<String, MapList<String, String>> fun2templ2items = new HashMap<String, MapList<String, String>>();
        DataSet ds = this.executeQuery(ruleExecContext, this.getSql(funnodes));
        for (DataRow row : ds.getRows()) {
            String funnode = (String) row.getValue("funnode");
            if (MMValueCheck.isNotEmpty(funnodes)) {
                notDataFunnodes.remove(funnode);
            }
            String templetName = (String) row.getValue("bill_templetcaption");
            String reftype = (String) row.getValue("reftype");
            boolean hyperlinkflag = this.getBoolean((String) row.getValue("hyperlinkflag"));
            boolean listHyperlinkflag = this.getBoolean((String) row.getValue("listHyperlinkflag"));
            String itemkey = (String) row.getValue("itemkey");
            if (reftype != null && reftype.startsWith("物料")) {
                if (!hyperlinkflag || !listHyperlinkflag) {
                    this.putErrorContxt(fun2templ2items, funnode, templetName, itemkey);
                }
            }
            if (reftype != null && reftype.startsWith("客户")) {
                if (!hyperlinkflag || !listHyperlinkflag) {
                    this.putErrorContxt(fun2templ2items, funnode, templetName, itemkey);
                }
            }
            if (reftype != null && reftype.startsWith("供应商")) {
                if (!hyperlinkflag || !listHyperlinkflag) {
                    this.putErrorContxt(fun2templ2items, funnode, templetName, itemkey);
                }
            }
            if ("vfirstcode".equals(itemkey)) {
                if (!hyperlinkflag || !listHyperlinkflag) {
                    this.putErrorContxt(fun2templ2items, funnode, templetName, itemkey);
                }
            }
            if ("vsrccode".equals(itemkey)) {
                if (!hyperlinkflag || !listHyperlinkflag) {
                    this.putErrorContxt(fun2templ2items, funnode, templetName, itemkey);
                }
            }
        }

        if (MMValueCheck.isNotEmpty(fun2templ2items)) {
            for (Entry<String, MapList<String, String>> outEntry : fun2templ2items.entrySet()) {
                StringBuilder errorContxt = new StringBuilder();
                for (Entry<String, List<String>> inEntry : outEntry.getValue().entrySet()) {
                    errorContxt.append("名为[" + inEntry.getKey() + "]的单据模板中字段" + inEntry.getValue() + "需要勾选支持超链接。");
                }
                result.addResultElement(outEntry.getKey(), errorContxt.toString());
            }
        }
        if (MMValueCheck.isNotEmpty(notDataFunnodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "功能节点编码为" + notDataFunnodes
                    + "的节点不存在所需检查的数据。\n");
        }
        return result;
    }

    /**
     * 将数据库中结果中N/Y转为布尔类型
     * 
     * @param value
     * @return
     */
    private boolean getBoolean(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        if (value.toUpperCase().equals("Y") || value.toUpperCase().equals("true")) {
            return true;
        }
        return false;
    }

    /**
     * 获取需要检查的数据，pub_systemplate_base , pub_billtemplet ,pub_billtemplet_b 三个表连接，
     * 
     * @param funnodes
     * @return
     */
    private String getSql(String[] funnodes) {
        StringBuilder result = new StringBuilder();
        result.append("select pt.bill_templetcaption,pb.reftype,pb.hyperlinkflag,pb.listHyperlinkflag,pb.itemkey,ps.funnode  "
                + " from pub_systemplate_base ps, pub_billtemplet pt,pub_billtemplet_b pb  "
                + " where isnull(ps.dr,0) = 0 and isnull(pt.dr,0) = 0 and isnull(pb.dr,0) = 0 and ps.tempstyle = 0 "
                + " and ps.templateid=pt.pk_billtemplet and pb.pk_billtemplet=pt.pk_billtemplet");
        if (!MMValueCheck.isEmpty(funnodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("ps.funnode", funnodes));
        }
        return result.toString();
    }

    /**
     * 将错误信息按功能节点分类
     * 
     * @param fun2templ2items
     * @param funnode
     * @param templetName
     * @param itemkey
     */
    private void putErrorContxt(Map<String, MapList<String, String>> fun2templ2items, String funnode,
            String templetName, String itemkey) {
        if (fun2templ2items.keySet().contains(funnode)) {
            fun2templ2items.get(funnode).put(templetName, itemkey);
        }
        else {
            MapList<String, String> templet2items = new MapList<String, String>();
            templet2items.put(templetName, itemkey);
            fun2templ2items.put(funnode, templet2items);
        }

    }
}
