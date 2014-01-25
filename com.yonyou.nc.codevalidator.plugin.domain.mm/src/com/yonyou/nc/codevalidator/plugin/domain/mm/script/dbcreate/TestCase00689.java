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
 * �ڽ���ű�Ԥ���У��絥�ݺţ�40�������ʱ�䣨19��������������20���ȳ����ǹ̶�������ͳһУ��
 * 
 * @since 6.0
 * @version 2013-9-23 ����1:30:08
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.CREATESCRIPT, subCatalog = SubCatalogEnum.CS_UNIFORM, description = "�ڽ���ű�Ԥ���У��絥�ݺţ�40�������ʱ�䣨19��������������20���ȳ����ǹ̶�������ͳһУ��  ", specialParamDefine = {
    "�������", "�ֶ�&����"
}, memo = "���ڲ����������������(��mmpac)�������������,�ֿ���Ĭ��ֻ�������е������ֶΣ���������������ֶΣ����ڡ��ֶ�&���ȡ����������ֶμ���淶����(�磺vbillcode&40)���������������ֶΣ��ò�����Ϊ�ա���ע����TestCase�е����������ȡ�԰��������Զ�ǿ��תΪСд��", solution = "�޸ĸõ������͵�pk_billtypeidʹ����pk_billtypecode����һ��  ", coder = "zhongcha", relatedIssueId = "689")
public class TestCase00689 extends AbstractDbcreateRuleDefinition implements IGlobalRuleDefinition {
    // ���ڱ�����������ֶμ���淶����
    private Map<String, Integer> item2length = new HashMap<String, Integer>();

    /**
     * ��ʼ���������Ĭ���ֶ�
     */
    private void initItemMap() {
        this.item2length.put("vbillcode", Integer.valueOf(40));
        this.item2length.put("approvertime", Integer.valueOf(19));
    }

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] domainCodes = ruleExecContext.getParameterArray("�������");
        // ��TestCase�е����������ȡ�԰��������Զ�ΪСд
        domainCodes = this.toLowerCase(domainCodes);
        List<String> notDataDomainCodes = new LinkedList<String>();
        if (MMValueCheck.isNotEmpty(domainCodes)) {
            notDataDomainCodes.addAll(Arrays.asList(domainCodes));
        }
        else {
            Logger.warn("��ǰ����Ĳ���Ϊ�գ��������������");
        }
        String[] itemAndlengths = ruleExecContext.getParameterArray("�ֶ�&����");
        this.initItemMap();
        // ��Ӵ������Ҫ�����ֶμ��䳤��
        if (!(MMValueCheck.isEmpty(itemAndlengths) || itemAndlengths[0].trim().equals(""))) {
            for (String param : itemAndlengths) {
                String[] params = this.getSplitParameter(param);
                this.item2length.put(params[0], Integer.valueOf(params[1]));
            }
        }
        // ���ڱ����������ͱ������ֶ����Ķ�Ӧ��ϵ��Map<�������,MapList<����, �ֶ���>>
        Map<String, MapList<String, String>> errorMap = new HashMap<String, MapList<String, String>>();
        // ����������и������������
        DataSet pkDs = this.executeDbcreateTableQuery(this.getPrimarykeySql(domainCodes), ruleExecContext);
        for (DataRow pkdr : pkDs.getRows()) {
            // ���������������Ƿ���Ϲ淶
            String tablename = (String) pkdr.getValue("tablename");
            String fieldname = (String) pkdr.getValue("fieldname");
            Integer fieldlength = (Integer) pkdr.getValue("fieldlength");
            String domainCode = (String) pkdr.getValue("businesscomponent");
            if (MMValueCheck.isNotEmpty(domainCodes)) {
                notDataDomainCodes.remove(domainCode);
            }
            if (fieldlength.intValue() != 20) {
                // ��Ӵ�����Ϣ
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
        // ����������������ֶεĳ���
        DataSet itemsDs =
                this.executeDbcreateTableQuery(
                        this.getItemnamesSql(domainCodes, this.item2length.keySet().toArray(new String[] {})),
                        ruleExecContext);
        for (DataRow itemsdr : itemsDs.getRows()) {
            // �����ֶγ����Ƿ���Ϲ淶
            String tablename = (String) itemsdr.getValue("tablename");
            String fieldname = (String) itemsdr.getValue("fieldname");
            Integer fieldlength = (Integer) itemsdr.getValue("fieldlength");
            String domainCode = (String) itemsdr.getValue("businesscomponent");
            if (MMValueCheck.isNotEmpty(domainCodes)) {
                notDataDomainCodes.remove(domainCode);
            }
            if (fieldlength.intValue() != this.item2length.get(fieldname).intValue()) {
                // ��Ӵ�����Ϣ
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
                    errorContxt.append("�������Ϊ[" + outEntry.getKey() + "]�б���Ϊ" + inEntry.getKey() + "���ֶ�"
                            + inEntry.getValue() + "�ĳ��Ȳ����Ϲ淶��\n");
                }
                result.addResultElement(outEntry.getKey(), errorContxt.toString());
            }
        }
        if (MMValueCheck.isNotEmpty(notDataDomainCodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "�������Ϊ[" + notDataDomainCodes
                    + "]����������������ݡ�\n");
        }
        return result;
    }

    /**
     * ���������ַ���ת����Сд
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
     * ������������б���������䳤�ȵ�sql
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
     * ������������б����Ҫ����ֶμ��䳤�ȵ�sql
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
     * ��������&�ֳ�����ֵ��String����
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
