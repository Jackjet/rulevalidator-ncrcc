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
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * 检测单据模板、查询模板、打印模板的layer字段是否为水平产品
 * 
 * @since 6.0
 * @version 2013-9-25 上午9:38:16
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.PRESCRIPT, subCatalog = SubCatalogEnum.PS_UNIFORM, description = "检测单据模板、查询模板、打印模板的layer字段是否为水平产品 ", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "修改该单据模板、查询模板、打印模板的layer字段为水平产品,即0  ", coder = "zhongcha", relatedIssueId = "696")
public class TestCase00696 extends AbstractScriptQueryRuleDefinition implements IGlobalRuleDefinition {
    private static final String BILL_TEMPLET = "单据模板";

    private static final String QUERY_TEMPLET = "查询模板";

    private static final String PRINT_TEMPLET = "打印模板";

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] funnodes = ruleExecContext.getParameterArray(CommonRuleParams.FUNCNODEPARAM);
        List<String> notDataFunnodes = new LinkedList<String>();
        if (MMValueCheck.isNotEmpty(funnodes)) {
            notDataFunnodes.addAll(Arrays.asList(funnodes));
        }
        else {
            Logger.warn("当前规则功能节点参数为空，将检查所有功能节点！");
        }
        // 在表pub_systemplate_base中错误的信息，Map<功能节点编码, MapList<模板类型, 模板名称>>
        Map<String, MapList<String, String>> notInSysErrorMap = new HashMap<String, MapList<String, String>>();
        // 在各自模板表中错误的信息，Map<功能节点编码, MapList<模板类型, 模板名称>>
        Map<String, MapList<String, String>> notInTemplErrorMap = new HashMap<String, MapList<String, String>>();
        // 检查单据模板
        DataSet billtempletds = this.executeQuery(ruleExecContext, this.getBillTempletSql(funnodes));
        for (int i = 0; i < billtempletds.getRowCount(); i++) {
            // layer1为pub_systemplet_base表中的layer字段的值，layer2为各自类型模板在各自模板表中layer字段的值。下同
            Integer layer1 = (Integer) billtempletds.getValue(i, "layer");
            Integer layer2 = (Integer) billtempletds.getValue(i, "1_layer");
            String btempletname = (String) billtempletds.getValue(i, "bill_templetcaption");
            String funnode = (String) billtempletds.getValue(i, "funnode");
            // 从没有找到数据中取出已经找到数据的功能节点
            if (MMValueCheck.isNotEmpty(funnodes)) {
                notDataFunnodes.remove(funnode);
            }
            if (!Integer.valueOf(0).equals(layer1)) {
                this.putErrorContxt(notInSysErrorMap, funnode, TestCase00696.BILL_TEMPLET, btempletname);
            }
            if (!Integer.valueOf(0).equals(layer2)) {
                this.putErrorContxt(notInTemplErrorMap, funnode, TestCase00696.BILL_TEMPLET, btempletname);
            }
        }
        // 检查查询模板
        DataSet querytempletds = this.executeQuery(ruleExecContext, this.getQueryTempletSql(funnodes));
        for (int i = 0; i < querytempletds.getRowCount(); i++) {
            Integer layer1 = (Integer) querytempletds.getValue(i, "layer");
            Integer layer2 = (Integer) querytempletds.getValue(i, "1_layer");
            String qtempletname = (String) querytempletds.getValue(i, "model_name");
            String funnode = (String) querytempletds.getValue(i, "funnode");
            // 从没有找到数据中取出已经找到数据的功能节点
            if (MMValueCheck.isNotEmpty(funnodes)) {
                notDataFunnodes.remove(funnode);
            }
            if (!Integer.valueOf(0).equals(layer1)) {
                this.putErrorContxt(notInSysErrorMap, funnode, TestCase00696.QUERY_TEMPLET, qtempletname);
            }
            if (!Integer.valueOf(0).equals(layer2)) {
                this.putErrorContxt(notInTemplErrorMap, funnode, TestCase00696.QUERY_TEMPLET, qtempletname);
            }
        }
        // 检查打印模板
        DataSet ptempletds = this.executeQuery(ruleExecContext, this.getPrintTempletSql(funnodes));
        for (int i = 0; i < ptempletds.getRowCount(); i++) {
            Integer layer1 = (Integer) ptempletds.getValue(i, "layer");
            Integer layer2 = (Integer) ptempletds.getValue(i, "1_layer");
            String ptempletname = (String) ptempletds.getValue(i, "vtemplatename");
            String funnode = (String) ptempletds.getValue(i, "funnode");
            // 从没有找到数据中取出已经找到数据的功能节点
            if (MMValueCheck.isNotEmpty(funnodes)) {
                notDataFunnodes.remove(funnode);
            }
            if (!Integer.valueOf(0).equals(layer1)) {
                this.putErrorContxt(notInSysErrorMap, funnode, TestCase00696.PRINT_TEMPLET, ptempletname);
            }
            if (!Integer.valueOf(0).equals(layer2)) {
                this.putErrorContxt(notInTemplErrorMap, funnode, TestCase00696.PRINT_TEMPLET, ptempletname);
            }
        }
        if (MMValueCheck.isNotEmpty(notInSysErrorMap)) {
            for (Entry<String, MapList<String, String>> outEntry : notInSysErrorMap.entrySet()) {
                StringBuilder errorContxt = new StringBuilder();
                for (Entry<String, List<String>> inEntry : outEntry.getValue().entrySet()) {
                    errorContxt.append("名称为" + inEntry.getValue() + "的" + inEntry.getKey()
                            + "在表pub_systemplate_base中layer字段不是水平产品，即layer的值不为0。\n");
                }
                result.addResultElement("错误类型1：功能节点编码为[" + outEntry.getKey() + "]", errorContxt.toString());
            }
        }

        if (MMValueCheck.isNotEmpty(notInTemplErrorMap)) {
            // 用于保存三种模板对应的表名
            Map<String, String> templetTable = new HashMap<String, String>();
            templetTable.put(TestCase00696.BILL_TEMPLET, "pub_billtemplet");
            templetTable.put(TestCase00696.QUERY_TEMPLET, "pub_query_templet");
            templetTable.put(TestCase00696.PRINT_TEMPLET, "pub_print_template");
            for (Entry<String, MapList<String, String>> outEntry : notInTemplErrorMap.entrySet()) {
                StringBuilder errorContxt = new StringBuilder();
                for (Entry<String, List<String>> inEntry : outEntry.getValue().entrySet()) {
                    errorContxt.append("名称为" + inEntry.getValue() + "的" + inEntry.getKey() + "在表"
                            + templetTable.get(inEntry.getKey()) + "中layer字段不是水平产品，即layer的值不为0。\n");
                }
                result.addResultElement("错误类型2：功能节点编码为[" + outEntry.getKey() + "]", errorContxt.toString());
            }
        }
        if (MMValueCheck.isNotEmpty(notDataFunnodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "功能节点编码为" + notDataFunnodes
                    + "的节点不存在所需检查的数据。\n");
        }
        return result;
    }

    /**
     * 将错误信息放入map中
     * 
     * @param errorMap：错误信息map
     * @param funnode：功能节点编码
     * @param templetType：模板类型
     * @param templetName：模板名称
     */
    private void putErrorContxt(Map<String, MapList<String, String>> errorMap, String funnode, String templetType,
            String templetName) {
        // 添加错误信息
        if (errorMap.keySet().contains(funnode) && errorMap.get(funnode).keySet().contains(templetType)) {
            errorMap.get(funnode).put(templetType, templetName);
        }
        else {
            MapList<String, String> templetType2Name = new MapList<String, String>();
            templetType2Name.put(templetType, templetName);
            errorMap.put(funnode, templetType2Name);
        }
    }

    /**
     * 根据功能节点编码查询系统默认单据模板的sql
     * 
     * @param funcode
     * @return
     */
    private String getBillTempletSql(String[] funnodes) {
        StringBuilder result = new StringBuilder();
        result.append("select ps.layer,pb.layer,pb.bill_templetcaption,ps.funnode from pub_systemplate_base ps,pub_billtemplet pb where ps.templateid=pb.pk_billtemplet and ps.tempstyle=0  and isnull(ps.dr,0)=0  and isnull(pb.dr,0)=0 ");
        if (!MMValueCheck.isEmpty(funnodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("ps.funnode", funnodes));
        }
        return result.toString();
    }

    /**
     * 根据功能节点编码查询系统默认查询模板的sql
     * 
     * @param funcode
     * @return
     */
    private String getQueryTempletSql(String[] funnodes) {
        StringBuilder result = new StringBuilder();
        result.append("select ps.layer,pt.layer,pt.model_name,ps.funnode from pub_systemplate_base ps,pub_query_templet pt where ps.templateid=pt.id and ps.tempstyle=1   and isnull(ps.dr,0)=0  and isnull(pt.dr,0)=0 ");
        if (!MMValueCheck.isEmpty(funnodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("ps.funnode", funnodes));
        }
        return result.toString();
    }

    /**
     * 根据功能节点编码查询系统默认打印模板的sql
     * 
     * @param funcode
     * @return
     */
    private String getPrintTempletSql(String[] funnodes) {
        StringBuilder result = new StringBuilder();
        result.append("select ps.layer,pp.layer,pp.vtemplatename,ps.funnode from pub_systemplate_base ps,pub_print_template pp where ps.templateid=pp.ctemplateid and ps.tempstyle=3   and isnull(ps.dr,0)=0  and isnull(pp.dr,0)=0 ");
        if (!MMValueCheck.isEmpty(funnodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("ps.funnode", funnodes));
        }
        return result.toString();
    }

}
