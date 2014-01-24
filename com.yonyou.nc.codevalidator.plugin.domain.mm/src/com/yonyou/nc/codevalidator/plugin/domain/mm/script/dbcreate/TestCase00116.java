package com.yonyou.nc.codevalidator.plugin.domain.mm.script.dbcreate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMDi18nConstants;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
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
 * 业务需求要求必须支持交易类型必须有属性扩展的单据交易类型的bd_billtype2的classtype字段必须有值
 * 
 * @since 6.0
 * @version 2013-9-22 下午7:48:56
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.CREATESCRIPT, subCatalog = SubCatalogEnum.CS_UNIFORM, description = "业务需求要求必须支持交易类型必须有属性扩展的单据交易类型的bd_billtype2的classtype字段必须有值", specialParamDefine = {
    "领域编码"
}, memo = "请在参数中输入领域编码(如MMPAC)，多个参数请用,分开", solution = "1.首先根据参数中的单据类型在bd_billtype表中查询,并且istransaction的值为N，并且canextendtransaction为Y,得到支持交易类型的单据类型的编码pk_billtypeid。 2.然后根据bd_billtype2.pk_billtypeid=单据类型编码，在bd_billtype2表中查询出扩展类数据，并且查出的数据至少要有两条，且classtype不能重复，且要有两条分别为6和7的。", coder = "zhongcha", relatedIssueId = "116")
public class TestCase00116 extends AbstractScriptQueryRuleDefinition implements IGlobalRuleDefinition {

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] domainCodes = ruleExecContext.getParameterArray("领域编码");
        List<String> notDataDomainCodes = new LinkedList<String>();
        if (MMValueCheck.isNotEmpty(domainCodes)) {
            notDataDomainCodes.addAll(Arrays.asList(domainCodes));
        }
        else {
            Logger.warn("当前规则的参数为空，将检查所有领域！");
        }
        MapList<String, String> dupliError = new MapList<String, String>();
        MapList<String, String> not6Error = new MapList<String, String>();
        MapList<String, String> not7Error = new MapList<String, String>();
        // 该MapList的key为单据交易类型编码（即pk_billtypeid），value为其对应的classtype的值
        MapList<String, String> id2classtypes = new MapList<String, String>();
        Map<String, String> id2domaincode = new HashMap<String, String>();
        DataSet ds = this.executeQuery(ruleExecContext, this.getSql(domainCodes));
        for (DataRow dataRow : ds.getRows()) {
            String billtypeid = (String) dataRow.getValue("pk_billtypeid");
            String classtype = dataRow.getValue("classtype").toString();
            String domainCode = (String) dataRow.getValue("systemcode");
            if (MMValueCheck.isNotEmpty(domainCodes)) {
                notDataDomainCodes.remove(domainCode);
            }
            id2classtypes.put(billtypeid, classtype);
            id2domaincode.put(billtypeid, domainCode);
        }

        for (Entry<String, List<String>> id2classtype : id2classtypes.entrySet()) {
            String billtypeid = id2classtype.getKey();
            List<String> classtypes = id2classtype.getValue();
            Set<String> classtypeSet = new HashSet<String>();
            for (String ct : classtypes) {
                classtypeSet.add(ct);
            }
            // 存在重复
            if (classtypes.size() != classtypeSet.size()) {
                dupliError.put(id2domaincode.get(billtypeid), billtypeid);
            }
            else {
                // classtype中不存在值为6
                if (!classtypes.toString().contains("6")) {
                    not6Error.put(id2domaincode.get(billtypeid), billtypeid);
                }
                // classtype中不存在值为7
                if (!classtypes.toString().contains("7")) {
                    not7Error.put(id2domaincode.get(billtypeid), billtypeid);
                }
            }
        }
        Map<String, StringBuilder> allErrorMap = new HashMap<String, StringBuilder>();
        if (MMValueCheck.isNotEmpty(dupliError)) {
            for (Entry<String, List<String>> entry : dupliError.entrySet()) {
                String error =
                        "领域编码为[" + entry.getKey() + "]的单据类型编码为" + entry.getValue()
                                + "在表bd_billtype2的classtype字段的值存在重复。\n";
                this.putErrorContxt(allErrorMap, entry.getKey(), error);
            }
        }
        if (MMValueCheck.isNotEmpty(not6Error)) {
            for (Entry<String, List<String>> entry : not6Error.entrySet()) {
                String error =
                        "领域编码为[" + entry.getKey() + "]的单据类型编码为" + entry.getValue()
                                + "在表bd_billtype2的classtype字段不存在为[ 6 ]的值。\n";
                this.putErrorContxt(allErrorMap, entry.getKey(), error);
            }
        }
        if (MMValueCheck.isNotEmpty(not7Error)) {
            for (Entry<String, List<String>> entry : not7Error.entrySet()) {
                String error =
                        "领域编码为[" + entry.getKey() + "]的单据类型编码为" + entry.getValue()
                                + "在表bd_billtype2的classtype字段不存在为[ 7 ]的值。\n";
                this.putErrorContxt(allErrorMap, entry.getKey(), error);
            }
        }
        if (MMValueCheck.isNotEmpty(allErrorMap)) {
            for (Entry<String, StringBuilder> entry : allErrorMap.entrySet()) {
                result.addResultElement(entry.getKey(), entry.getValue().toString());
            }
        }
        if (MMValueCheck.isNotEmpty(notDataDomainCodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "领域编码为[" + notDataDomainCodes
                    + "]不存在所需检查的数据。\n");
        }
        return result;
    }

    /**
     * 根据领域编码获得bd_billtype2中pk_billtypeid和clsstye的值的sql语句
     * （即：业务需求要求必须支持交易类型必须有属性扩展的单据交易类型的bd_billtype2的classtype字段）
     * 
     * @param domainCode
     * @return
     */
    private String getSql(String[] domainCodes) {
        StringBuilder result = new StringBuilder();
        result.append("select p.pk_billtypeid,p.classtype,b.systemcode from bd_billtype b,bd_billtype2 p where b.istransaction='N'and b.canextendtransaction='Y'  and b.pk_billtypeid=p.pk_billtypeid  and isnull(b.dr,0)=0 and isnull(p.dr,0)=0 ");
        if (!MMValueCheck.isEmpty(domainCodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("b.systemcode", domainCodes));
        }
        return result.toString();
    }

    /**
     * 将错误信息整合，并根据功能节点将错误信息分类
     * 
     * @param allErrorMap
     * @param domainCode
     * @param error
     */
    private void putErrorContxt(Map<String, StringBuilder> allErrorMap, String domainCode, String error) {
        if (allErrorMap.entrySet().contains(domainCode)) {
            allErrorMap.get(domainCode).append(error);
        }
        else {
            StringBuilder errorContxt = new StringBuilder();
            errorContxt.append(error);
            allErrorMap.put(domainCode, errorContxt);
        }
    }
}
