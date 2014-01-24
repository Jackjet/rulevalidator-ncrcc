package com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMDi18nConstants;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
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
 * ���ְ�����ƵĶ��ﴦ��
 * 
 * @since 6.0
 * @version 2013-9-1 ����12:35:00
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_COMMON, description = "���ְ�����ƵĶ��ﴦ��", specialParamDefine = {
    "ְ�����"
}, solution = "���ְ�����ƶ����sm_responsibility��name�ǿ�  ", coder = "zhongcha", relatedIssueId = "319", memo = "�����������,��������ְ����룺�������code��Ϊ��mmppp, mmpmp, mmptp��")
public class TestCase00319 extends AbstractScriptQueryRuleDefinition implements IGlobalRuleDefinition {
    /**
     * ְ����룺�������code��Ϊ��mmppp, mmpmp, mmptp
     */
    private final static String RESP_CODE = "ְ�����";

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] respCodes = ruleExecContext.getParameterArray(TestCase00319.RESP_CODE);
        List<String> notDataRespcodes = new LinkedList<String>();
        if (MMValueCheck.isNotEmpty(respCodes)) {
            notDataRespcodes.addAll(Arrays.asList(respCodes));
        }
        else {
            Logger.warn("��ǰ�����ܽڵ����Ϊ�գ����������ְ��");
        }
        DataSet ds = this.executeQuery(ruleExecContext, this.getSql(respCodes));
        List<String> errorList = new ArrayList<String>();
        for (int i = 0; i < ds.getRowCount(); i++) {

            String code = (String) ds.getValue(i, "code");
            if (MMValueCheck.isNotEmpty(respCodes)) {
                notDataRespcodes.remove(code);
            }
            if (MMValueCheck.isEmpty(ds.getValue(i, "name"))) {
                errorList.add(code);
            }
        }
        if (MMValueCheck.isNotEmpty(errorList)) {
            result.addResultElement(MmMDi18nConstants.ERROR_DATABASE_NULL, "����Ϊ" + errorList + "��ְ�����δ������ġ�\n");
        }
        if (MMValueCheck.isNotEmpty(notDataRespcodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "ְ�����Ϊ" + notDataRespcodes
                    + "����������������ݡ�\n");
        }
        return result;
    }

    private String getSql(String[] respCodes) {
        StringBuilder result = new StringBuilder();
        result.append("select sr.name,sr.code from sm_responsibility sr  where  isnull(sr.dr,0)=0 ");
        if (!MMValueCheck.isEmpty(respCodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("sr.code", respCodes));
        }
        return result.toString();
    }
}
