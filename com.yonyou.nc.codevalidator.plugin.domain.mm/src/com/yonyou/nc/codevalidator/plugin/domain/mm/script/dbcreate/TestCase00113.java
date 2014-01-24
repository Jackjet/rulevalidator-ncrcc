package com.yonyou.nc.codevalidator.plugin.domain.mm.script.dbcreate;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

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
 * �������͵�pk_billtypeidҪ��pk_billtypecode����һ��
 * 
 * @since 6.0
 * @version 2013-9-22 ����7:44:45
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.CREATESCRIPT, subCatalog = SubCatalogEnum.CS_UNIFORM, description = "�������͵�pk_billtypeidҪ��pk_billtypecode����һ�� ", specialParamDefine = {
    "�������"
}, memo = "���ڲ����������������(��MMPAC)�������������,�ֿ�", solution = "�޸ĸõ������͵�pk_billtypeidʹ����pk_billtypecode����һ��  ", coder = "zhongcha", relatedIssueId = "113")
public class TestCase00113 extends AbstractScriptQueryRuleDefinition implements IGlobalRuleDefinition {

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
        DataSet ds = this.executeQuery(ruleExecContext, this.getSql(domainCodes));
        MapList<String, String> errorMapList = new MapList<String, String>();
        for (DataRow dataRow : ds.getRows()) {
            String billtypecode = (String) dataRow.getValue("pk_billtypecode");
            String billtypeid = (String) dataRow.getValue("pk_billtypeid");
            String systemcode = (String) dataRow.getValue("systemcode");
            if (MMValueCheck.isNotEmpty(domainCodes)) {
                notDataDomainCodes.remove(systemcode);
            }
            if (!billtypecode.equals(billtypeid)) {
                errorMapList.put(systemcode, billtypecode);
            }
        }
        if (MMValueCheck.isNotEmpty(errorMapList)) {
            for (Entry<String, List<String>> entry : errorMapList.entrySet()) {
                result.addResultElement(entry.getKey(), "�������Ϊ[" + entry.getKey() + "]�ĵ������ͱ���Ϊ" + entry.getValue()
                        + "�����Ӧ��pk_billtypeid��һ�¡�\n");
            }
        }
        if (MMValueCheck.isNotEmpty(notDataDomainCodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "�������Ϊ[" + notDataDomainCodes
                    + "]����������������ݡ�\n");
        }
        return result;
    }

    private String getSql(String[] domainCodes) {
        StringBuilder result = new StringBuilder();
        result.append("select b.pk_billtypecode,b.pk_billtypeid,b.systemcode from bd_billtype b where b.istransaction='N' and isnull(b.dr,0)=0 ");
        if (!MMValueCheck.isEmpty(domainCodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("b.systemcode", domainCodes));
        }
        return result.toString();
    }
}
