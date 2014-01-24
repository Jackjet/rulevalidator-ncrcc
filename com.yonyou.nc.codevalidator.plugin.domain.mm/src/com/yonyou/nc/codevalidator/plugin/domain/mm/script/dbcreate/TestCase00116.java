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
 * ҵ������Ҫ�����֧�ֽ������ͱ�����������չ�ĵ��ݽ������͵�bd_billtype2��classtype�ֶα�����ֵ
 * 
 * @since 6.0
 * @version 2013-9-22 ����7:48:56
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.CREATESCRIPT, subCatalog = SubCatalogEnum.CS_UNIFORM, description = "ҵ������Ҫ�����֧�ֽ������ͱ�����������չ�ĵ��ݽ������͵�bd_billtype2��classtype�ֶα�����ֵ", specialParamDefine = {
    "�������"
}, memo = "���ڲ����������������(��MMPAC)�������������,�ֿ�", solution = "1.���ȸ��ݲ����еĵ���������bd_billtype���в�ѯ,����istransaction��ֵΪN������canextendtransactionΪY,�õ�֧�ֽ������͵ĵ������͵ı���pk_billtypeid�� 2.Ȼ�����bd_billtype2.pk_billtypeid=�������ͱ��룬��bd_billtype2���в�ѯ����չ�����ݣ����Ҳ������������Ҫ����������classtype�����ظ�����Ҫ�������ֱ�Ϊ6��7�ġ�", coder = "zhongcha", relatedIssueId = "116")
public class TestCase00116 extends AbstractScriptQueryRuleDefinition implements IGlobalRuleDefinition {

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] domainCodes = ruleExecContext.getParameterArray("�������");
        List<String> notDataDomainCodes = new LinkedList<String>();
        if (MMValueCheck.isNotEmpty(domainCodes)) {
            notDataDomainCodes.addAll(Arrays.asList(domainCodes));
        }
        else {
            Logger.warn("��ǰ����Ĳ���Ϊ�գ��������������");
        }
        MapList<String, String> dupliError = new MapList<String, String>();
        MapList<String, String> not6Error = new MapList<String, String>();
        MapList<String, String> not7Error = new MapList<String, String>();
        // ��MapList��keyΪ���ݽ������ͱ��루��pk_billtypeid����valueΪ���Ӧ��classtype��ֵ
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
            // �����ظ�
            if (classtypes.size() != classtypeSet.size()) {
                dupliError.put(id2domaincode.get(billtypeid), billtypeid);
            }
            else {
                // classtype�в�����ֵΪ6
                if (!classtypes.toString().contains("6")) {
                    not6Error.put(id2domaincode.get(billtypeid), billtypeid);
                }
                // classtype�в�����ֵΪ7
                if (!classtypes.toString().contains("7")) {
                    not7Error.put(id2domaincode.get(billtypeid), billtypeid);
                }
            }
        }
        Map<String, StringBuilder> allErrorMap = new HashMap<String, StringBuilder>();
        if (MMValueCheck.isNotEmpty(dupliError)) {
            for (Entry<String, List<String>> entry : dupliError.entrySet()) {
                String error =
                        "�������Ϊ[" + entry.getKey() + "]�ĵ������ͱ���Ϊ" + entry.getValue()
                                + "�ڱ�bd_billtype2��classtype�ֶε�ֵ�����ظ���\n";
                this.putErrorContxt(allErrorMap, entry.getKey(), error);
            }
        }
        if (MMValueCheck.isNotEmpty(not6Error)) {
            for (Entry<String, List<String>> entry : not6Error.entrySet()) {
                String error =
                        "�������Ϊ[" + entry.getKey() + "]�ĵ������ͱ���Ϊ" + entry.getValue()
                                + "�ڱ�bd_billtype2��classtype�ֶβ�����Ϊ[ 6 ]��ֵ��\n";
                this.putErrorContxt(allErrorMap, entry.getKey(), error);
            }
        }
        if (MMValueCheck.isNotEmpty(not7Error)) {
            for (Entry<String, List<String>> entry : not7Error.entrySet()) {
                String error =
                        "�������Ϊ[" + entry.getKey() + "]�ĵ������ͱ���Ϊ" + entry.getValue()
                                + "�ڱ�bd_billtype2��classtype�ֶβ�����Ϊ[ 7 ]��ֵ��\n";
                this.putErrorContxt(allErrorMap, entry.getKey(), error);
            }
        }
        if (MMValueCheck.isNotEmpty(allErrorMap)) {
            for (Entry<String, StringBuilder> entry : allErrorMap.entrySet()) {
                result.addResultElement(entry.getKey(), entry.getValue().toString());
            }
        }
        if (MMValueCheck.isNotEmpty(notDataDomainCodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "�������Ϊ[" + notDataDomainCodes
                    + "]����������������ݡ�\n");
        }
        return result;
    }

    /**
     * �������������bd_billtype2��pk_billtypeid��clsstye��ֵ��sql���
     * ������ҵ������Ҫ�����֧�ֽ������ͱ�����������չ�ĵ��ݽ������͵�bd_billtype2��classtype�ֶΣ�
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
     * ��������Ϣ���ϣ������ݹ��ܽڵ㽫������Ϣ����
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
