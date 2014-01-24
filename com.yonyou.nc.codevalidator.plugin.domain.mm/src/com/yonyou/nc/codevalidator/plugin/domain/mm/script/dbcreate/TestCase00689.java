package com.yonyou.nc.codevalidator.plugin.domain.mm.script.dbcreate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMDi18nConstants;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractDbcreateRuleDefinition;
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
 * 在建库脚本预置中，如单据号（40）、审核时间（19）、单据主键（20）等长度是固定，必须统一校验
 * 
 * @since 6.0
 * @version 2013-9-23 下午1:30:08
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.CREATESCRIPT, subCatalog = SubCatalogEnum.CS_UNIFORM, description = "在建库脚本预置中，如单据号（40）、审核时间（19）、单据主键（20）等长度是固定，必须统一校验  ", specialParamDefine = {
    "领域编码", "字段&长度"
}, memo = "请在参数中输入领域编码(如mmpac)，多个参数请用,分开。默认只检查标题中的三种字段，如果还需检查其他字段，请在【字段&长度】中输入检查字段即其规范长度(如：vbillcode&40)，若无需检查其他字段，该参数可为空。（注：该TestCase中的领域编码是取自包名，所以都强制转为小写）", solution = "修改该单据类型的pk_billtypeid使其与pk_billtypecode保持一致  ", coder = "zhongcha", relatedIssueId = "689")
public class TestCase00689 extends AbstractDbcreateRuleDefinition implements IGlobalRuleDefinition {
    // 用于保存所需检查的字段及其规范长度
    private Map<String, Integer> item2length = new HashMap<String, Integer>();

    /**
     * 初始化所需检查的默认字段
     */
    private void initItemMap() {
        this.item2length.put("vbillcode", Integer.valueOf(40));
        this.item2length.put("approvertime", Integer.valueOf(19));
    }

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] domainCodes = ruleExecContext.getParameterArray("领域编码");
        // 该TestCase中的领域编码是取自包名，所以都为小写
        domainCodes = this.toLowerCase(domainCodes);
        List<String> notDataDomainCodes = new LinkedList<String>();
        if (MMValueCheck.isNotEmpty(domainCodes)) {
            notDataDomainCodes.addAll(Arrays.asList(domainCodes));
        }
        else {
            Logger.warn("当前规则的参数为空，将检查所有领域！");
        }
        String[] itemAndlengths = ruleExecContext.getParameterArray("字段&长度");
        this.initItemMap();
        // 添加传入的需要检查的字段及其长度
        if (!(MMValueCheck.isEmpty(itemAndlengths) || itemAndlengths[0].trim().equals(""))) {
            for (String param : itemAndlengths) {
                String[] params = this.getSplitParameter(param);
                this.item2length.put(params[0], Integer.valueOf(params[1]));
            }
        }
        // 用于保存领域编码和表名与字段名的对应关系，Map<领域编码,MapList<表名, 字段名>>
        Map<String, MapList<String, String>> errorMap = new HashMap<String, MapList<String, String>>();
        // 查出该领域中各表的主键长度
        DataSet pkDs = this.executeDbcreateTableQuery(this.getPrimarykeySql(domainCodes), ruleExecContext);
        for (DataRow pkdr : pkDs.getRows()) {
            // 检查各表主键长度是否符合规范
            String tablename = (String) pkdr.getValue("tablename");
            String fieldname = (String) pkdr.getValue("fieldname");
            Integer fieldlength = (Integer) pkdr.getValue("fieldlength");
            String domainCode = (String) pkdr.getValue("businesscomponent");
            if (MMValueCheck.isNotEmpty(domainCodes)) {
                notDataDomainCodes.remove(domainCode);
            }
            if (fieldlength.intValue() != 20) {
                // 添加错误信息
                if (errorMap.keySet().contains(domainCode) && errorMap.get(domainCode).keySet().contains(tablename)) {
                    errorMap.get(domainCode).put(tablename, fieldname);
                }
                else {
                    MapList<String, String> tbname2fdnames = new MapList<String, String>();
                    tbname2fdnames.put(tablename, fieldname);
                    errorMap.put(domainCode, tbname2fdnames);
                }
            }
        }
        // 查出该领域所需检查字段的长度
        DataSet itemsDs =
                this.executeDbcreateTableQuery(
                        this.getItemnamesSql(domainCodes, this.item2length.keySet().toArray(new String[] {})),
                        ruleExecContext);
        for (DataRow itemsdr : itemsDs.getRows()) {
            // 检查各字段长度是否符合规范
            String tablename = (String) itemsdr.getValue("tablename");
            String fieldname = (String) itemsdr.getValue("fieldname");
            Integer fieldlength = (Integer) itemsdr.getValue("fieldlength");
            String domainCode = (String) itemsdr.getValue("businesscomponent");
            if (MMValueCheck.isNotEmpty(domainCodes)) {
                notDataDomainCodes.remove(domainCode);
            }
            if (fieldlength.intValue() != this.item2length.get(fieldname).intValue()) {
                // 添加错误信息
                if (errorMap.keySet().contains(domainCode) && errorMap.get(domainCode).keySet().contains(tablename)) {
                    errorMap.get(domainCode).put(tablename, fieldname);
                }
                else {
                    MapList<String, String> tbname2fdnames = new MapList<String, String>();
                    tbname2fdnames.put(tablename, fieldname);
                    errorMap.put(domainCode, tbname2fdnames);
                }
            }
        }
        if (MMValueCheck.isNotEmpty(errorMap)) {
            for (Entry<String, MapList<String, String>> outEntry : errorMap.entrySet()) {
                StringBuilder errorContxt = new StringBuilder();
                for (Entry<String, List<String>> inEntry : outEntry.getValue().entrySet()) {
                    errorContxt.append("领域编码为[" + outEntry.getKey() + "]中表名为" + inEntry.getKey() + "的字段"
                            + inEntry.getValue() + "的长度不符合规范。\n");
                }
                result.addResultElement(outEntry.getKey(), errorContxt.toString());
            }
        }
        if (MMValueCheck.isNotEmpty(notDataDomainCodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "领域编码为[" + notDataDomainCodes
                    + "]不存在所需检查的数据。\n");
        }
        return result;
    }

    /**
     * 将数组中字符都转化成小写
     * 
     * @param array
     * @return
     */
    private String[] toLowerCase(String[] array) {
        if (MMValueCheck.isEmpty(array)) {
            return null;
        }
        String[] result = new String[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].toLowerCase();
        }
        return result;
    }

    /**
     * 查出该领域所有表的主键及其长度的sql
     * 
     * @param domainCodes
     * @return
     */
    private String getPrimarykeySql(String[] domainCodes) {
        StringBuilder result = new StringBuilder();
        result.append("select distinct t.tablename,f.fieldname,f.fieldlength,t.businesscomponent from dbcreate_table t,dbcreate_field f where t.tablename=f.fieldtablename and  f.fieldname=t.primarykey ");
        if (!MMValueCheck.isEmpty(domainCodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("t.businesscomponent", domainCodes));
        }
        return result.toString();
    }

    /**
     * 查出该领域所有表的需要检查字段及其长度的sql
     * 
     * @param domainCode
     * @param itemnames
     * @return
     */
    private String getItemnamesSql(String[] domainCodes, String[] itemnames) {
        StringBuilder result = new StringBuilder();
        result.append("select distinct t.tablename,f.fieldname,f.fieldlength,t.businesscomponent from dbcreate_table t,dbcreate_field f where t.tablename=f.fieldtablename");
        result.append(" and ");
        result.append(SQLQueryExecuteUtils.buildSqlForIn(" f.fieldname", itemnames));
        if (!MMValueCheck.isEmpty(domainCodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("t.businesscomponent", domainCodes));
        }
        return result.toString();
    }

    /**
     * 将参数用&分成两个值的String数组
     * 
     * @param parameter
     * @return
     */
    public String[] getSplitParameter(String parameter) {
        if (MMValueCheck.isEmpty(parameter) || !parameter.contains("&") || parameter.indexOf("&") <= 0) {
            return null;
        }
        return parameter.split("&");
    }
}
