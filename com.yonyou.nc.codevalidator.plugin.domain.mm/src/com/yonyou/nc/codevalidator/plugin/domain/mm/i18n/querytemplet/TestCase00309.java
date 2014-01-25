package com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.querytemplet;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.util.QueryLangRuleValidatorUtil;
import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMDi18nConstants;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractLangRuleDefinition;
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
 * ����ѯģ�������Ƿ���ڶ���
 * 
 * @since 6.0
 * @version 2013-8-31 ����7:45:18
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_QUERYTEMP, description = "����ѯģ�������Ƿ���ڶ���", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "��Ӹò�ѯģ�����ƶ������Ӧpub_query_templet��resid��Ϊ�� ", coder = "zhongcha", relatedIssueId = "309")
public class TestCase00309 extends AbstractLangRuleDefinition implements IGlobalRuleDefinition {

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] funnodes = ruleExecContext.getParameterArray(CommonRuleParams.FUNCNODEPARAM);
        List<String> notDataFunnodes = new LinkedList<String>();
        if (MMValueCheck.isNotEmpty(funnodes)) {
            notDataFunnodes.addAll(Arrays.asList(funnodes));
            // notDataFunnodes = new LinkedList<String>();
        }
        else {
            Logger.warn("��ǰ�����ܽڵ����Ϊ�գ���������й��ܽڵ㣡");
        }
        MapList<String, String> fun2qTemp1 = new MapList<String, String>();
        MapList<String, String> fun2qTemp2 = new MapList<String, String>();
        DataSet ds = this.executeQuery(ruleExecContext, this.getSql(funnodes));
        for (DataRow dataRow : ds.getRows()) {
            String queryTempName = (String) dataRow.getValue(MmMDi18nConstants.MODEL_NAME);
            String funnode = (String) dataRow.getValue("funnode");
            if (MMValueCheck.isNotEmpty(funnodes)) {
                notDataFunnodes.remove(funnode);
            }
            if (MMValueCheck.isEmpty(dataRow.getValue(MmMDi18nConstants.RESID))) {
                fun2qTemp1.put(funnode, queryTempName);
            }
            else if (QueryLangRuleValidatorUtil.notInLRV((String) dataRow.getValue(MmMDi18nConstants.RESID),
                    ruleExecContext.getRuntimeContext())) {
                fun2qTemp2.put(funnode, queryTempName);
            }
        }

        if (MMValueCheck.isNotEmpty(fun2qTemp1)) {
            for (String funnode : fun2qTemp1.keySet()) {
                result.addResultElement(funnode + MmMDi18nConstants.ERROR_DATABASE_NULL, "���ܽڵ����Ϊ[" + funnode
                        + "]�Ĳ�ѯģ������" + fun2qTemp1.get(funnode) + "δ�����\n");

            }
        }
        if (MMValueCheck.isNotEmpty(fun2qTemp2)) {
            for (String funnode : fun2qTemp2.keySet()) {
                result.addResultElement(funnode + MmMDi18nConstants.ERROR_LANGFILE_NULL, "���ܽڵ����Ϊ[" + funnode
                        + "]�Ĳ�ѯģ������" + fun2qTemp2.get(funnode)
                        + "������ģ���Ӧ�Ķ���jar��lang\\simpchn\\�ļ����¶����ļ��в����ڸò�ѯģ�����ƵĶ��\n");

            }
        }

        if (MMValueCheck.isNotEmpty(notDataFunnodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "���ܽڵ����Ϊ" + notDataFunnodes
                    + "�Ľڵ㲻��������������ݡ�\n");
        }
        return result;
    }

    private String getSql(String[] funnodes) {
        StringBuilder result = new StringBuilder();
        result.append("select distinct pt.resid,pt.model_name,pb.funnode from pub_query_templet pt, pub_systemplate_base pb where pt.id=pb.templateid and pb.tempstyle='1'");
        if (!MMValueCheck.isEmpty(funnodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("pb.funnode", funnodes));
        }
        return result.toString();
    }

}
