package com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMDi18nConstants;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
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
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * ��鵥���������ƶ����Ƿ�������
 * 
 * @since 6.0
 * @version 2013-9-1 ����9:54:13
 * @author zhongcha
 */

@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_COMMON, description = "��鵥���������ƶ����Ƿ�������", specialParamDefine = {
    CommonRuleParams.NODECODEPARAM
}, solution = "��Ӹõ����������ƶ����bd_billtype��billtypename�ֶβ�Ϊ�գ��� istransaction='N' ", coder = "zhongcha", relatedIssueId = "313", memo = "�����������,�������ڵ���뼴NODECODE��")
public class TestCase00313 extends AbstractScriptQueryRuleDefinition implements IGlobalRuleDefinition {

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] nodecodes = ruleExecContext.getParameterArray(CommonRuleParams.NODECODEPARAM);
        List<String> notDataNodecodes = new LinkedList<String>();
        if (MMValueCheck.isNotEmpty(nodecodes)) {
            notDataNodecodes.addAll(Arrays.asList(nodecodes));
        }
        else {
            Logger.warn("��ǰ�����ܽڵ����Ϊ�գ���������нڵ㣡");
        }
        DataSet ds = this.executeQuery(ruleExecContext, this.getSql(nodecodes));
        MapList<String, String> errorMapList = new MapList<String, String>();
        List<String> errorList = new ArrayList<String>();
        for (DataRow dataRow : ds.getRows()) {
            String nodecode = (String) dataRow.getValue("nodecode");
            if (MMValueCheck.isNotEmpty(nodecodes)) {
                notDataNodecodes.remove(nodecode);
            }
            String billtypecode = (String) dataRow.getValue("pk_billtypecode");
            String billtypename = (String) dataRow.getValue("billtypename");
            if (MMValueCheck.isEmpty(billtypename)) {
                if (MMValueCheck.isNotEmpty(nodecodes)) {
                    errorMapList.put(nodecode, billtypecode);
                }
                else {
                    errorList.add(billtypecode);
                }
            }
        }
        if (MMValueCheck.isNotEmpty(errorMapList)) {
            for (Map.Entry<String, List<String>> entry : errorMapList.entrySet()) {
                result.addResultElement(entry.getKey() + MmMDi18nConstants.ERROR_DATABASE_NULL,
                        "�ڵ����Ϊ[" + entry.getKey() + "]�ĵ������ͱ���Ϊ" + entry.getValue() + "�ĵ�������δ�����\n");
            }
        }
        if (MMValueCheck.isNotEmpty(errorList)) {
            result.addResultElement(MmMDi18nConstants.ERROR_DATABASE_NULL, "�������ͱ���Ϊ" + errorList
                    + "�ĵ�������δ������(ע��δ�ҵ���Ӧ�Ľڵ����)��\n");

        }
        if (MMValueCheck.isNotEmpty(notDataNodecodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "�ڵ����Ϊ" + notDataNodecodes
                    + "�Ľڵ㲻��������������ݡ�\n");
        }
        return result;
    }

    private String getSql(String[] nodecodes) {
        StringBuilder result = new StringBuilder();
        result.append("select billtypename,pk_billtypecode,nodecode from bd_billtype where istransaction='N' and isnull(dr,0)=0 ");
        if (!MMValueCheck.isEmpty(nodecodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("nodecode", nodecodes));
        }
        return result.toString();
    }
}
