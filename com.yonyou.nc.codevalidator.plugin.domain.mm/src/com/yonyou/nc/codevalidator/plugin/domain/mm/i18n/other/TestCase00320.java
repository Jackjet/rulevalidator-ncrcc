package com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.other;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.util.QueryLangRuleValidatorUtil;
import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMDi18nConstants;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractLangRuleDefinition;
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
 * ���֧������Ȩ�޺�ҵ��ʵ������в������ƶ��ﴦ��
 * 
 * @since 6.0
 * @version 2013-9-1 ����12:49:01
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_COMMON, description = "���֧������Ȩ�޺�ҵ��ʵ������в������ƶ��ﴦ��", specialParamDefine = {
    "ģ�����"
}, solution = "���֧������Ȩ�޺�ҵ��ʵ������в������ƶ����sm_res_operation��Ӧ��resid�ǿ�  ", coder = "zhongcha", relatedIssueId = "320", memo = "�����������,����")
public class TestCase00320 extends AbstractLangRuleDefinition implements IGlobalRuleDefinition {

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] modules = ruleExecContext.getParameterArray(MmMDi18nConstants.MODULE_CODE);
        List<String> notDataModules = new LinkedList<String>();
        if (MMValueCheck.isNotEmpty(modules)) {
            notDataModules.addAll(Arrays.asList(modules));
        }
        else {
            Logger.warn("��ǰ�����ܽڵ����Ϊ�գ����������ģ�飡");
        }
        DataSet ds = this.executeQuery(ruleExecContext, this.getSql(modules));
        MapList<String, String> errorMapList1 = new MapList<String, String>();
        MapList<String, String> errorMapList2 = new MapList<String, String>();
        for (int i = 0; i < ds.getRowCount(); i++) {
            String operationName = (String) ds.getValue(i, "operationname");
            String ownmodule = (String) ds.getValue(i, "ownmodule");
            if (MMValueCheck.isNotEmpty(modules)) {
                notDataModules.remove(ownmodule);
            }
            if (MMValueCheck.isEmpty(ds.getValue(i, MmMDi18nConstants.RESID))) {
                errorMapList1.put((String) ds.getValue(i, "ownmodule"), operationName);
            }
            else if (QueryLangRuleValidatorUtil.notInLRV((String) ds.getValue(i, MmMDi18nConstants.RESID),
                    ruleExecContext.getRuntimeContext())) {
                errorMapList2.put((String) ds.getValue(i, "ownmodule"), operationName);
            }
        }
        if (MMValueCheck.isNotEmpty(errorMapList1)) {
            for (Map.Entry<String, List<String>> entry : errorMapList1.entrySet()) {
                result.addResultElement(entry.getKey() + MmMDi18nConstants.ERROR_DATABASE_NULL,
                        "ģ�����Ϊ[" + entry.getKey() + "]֧������Ȩ�޺�ҵ��ʵ������в�������Ϊ" + entry.getValue() + "δ�����\n");
            }
        }
        if (MMValueCheck.isNotEmpty(errorMapList2)) {
            for (Map.Entry<String, List<String>> entry : errorMapList1.entrySet()) {
                result.addResultElement(entry.getKey() + MmMDi18nConstants.ERROR_LANGFILE_NULL,
                        "ģ�����Ϊ[" + entry.getKey() + "]֧������Ȩ�޺�ҵ��ʵ������в�������Ϊ" + entry.getValue()
                                + "������ģ���Ӧ�Ķ���jar��lang\\simpchn\\�ļ�����Ԫ���ݶ����ļ��в����ڸ�Ԫ�������ƶ��\n");
            }
        }

        if (MMValueCheck.isNotEmpty(notDataModules)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "ģ�����Ϊ" + notDataModules + "����������������ݡ�\n");
        }
        return result;
    }

    private String getSql(String[] modules) {
        StringBuilder result = new StringBuilder();
        result.append("select distinct resid,operationname,ownmodule from sm_res_operation where  isnull(dr,0)=0 ");
        if (!MMValueCheck.isEmpty(modules)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("ownmodule", modules));
        }
        return result.toString();
    }
}
