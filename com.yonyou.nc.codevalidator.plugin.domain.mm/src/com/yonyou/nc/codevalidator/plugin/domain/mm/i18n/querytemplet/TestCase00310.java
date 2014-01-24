package com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.querytemplet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
 * ����ѯģ���Ԫ�����ֶ����ƶ���
 * 
 * @since 6.0
 * @version 2013-8-31 ����9:10:28
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_QUERYTEMP, description = "����ѯģ���Ԫ�����ֶ����ƶ���", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "��Ӹò�ѯģ���Ԫ�����ֶ����ƶ������Ӧpub_query_condition��if_notmdcondition=Y���ֶζ�Ӧ��resid��Ϊ��", coder = "zhongcha", relatedIssueId = "310")
public class TestCase00310 extends AbstractLangRuleDefinition implements IGlobalRuleDefinition {

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] funnodes = ruleExecContext.getParameterArray(CommonRuleParams.FUNCNODEPARAM);
        List<String> notDataFunnodes = new LinkedList<String>();
        if (MMValueCheck.isNotEmpty(funnodes)) {
            notDataFunnodes.addAll(Arrays.asList(funnodes));
        }
        else {
            Logger.warn("��ǰ�����ܽڵ����Ϊ�գ���������й��ܽڵ㣡");
        }
        // ���residΪ�յĴ�����Ϣ Map<���ܽڵ����, MapList<��ѯģ������, ��Ԫ�����ֶ�>>
        Map<String, MapList<String, String>> notInDbErrorMap = new HashMap<String, MapList<String, String>>();
        // �����ļ��в����ڵĴ�����Ϣ Map<���ܽڵ����, MapList<��ѯģ������, ��Ԫ�����ֶ�>>
        Map<String, MapList<String, String>> notInFileErrorMap = new HashMap<String, MapList<String, String>>();
        DataSet ds = this.executeQuery(ruleExecContext, this.getSql(funnodes));
        for (DataRow dataRow : ds.getRows()) {
            String queryTempName = (String) dataRow.getValue(MmMDi18nConstants.MODEL_NAME);
            String field = (String) dataRow.getValue("field_name");
            String funnode = (String) dataRow.getValue("funnode");
            if (MMValueCheck.isNotEmpty(funnodes)) {
                notDataFunnodes.remove(funnode);
            }
            if (MMValueCheck.isEmpty(field)) {
                field = (String) dataRow.getValue("field_code");
            }
            if (MMValueCheck.isEmpty(dataRow.getValue(MmMDi18nConstants.RESID))) {
                // ��Ӵ�����Ϣ
                if (notInDbErrorMap.keySet().contains(funnode)
                        && notInDbErrorMap.get(funnode).keySet().contains(queryTempName)) {
                    notInDbErrorMap.get(funnode).put(queryTempName, field);
                }
                else {
                    MapList<String, String> templet2items = new MapList<String, String>();
                    templet2items.put(queryTempName, field);
                    notInDbErrorMap.put(funnode, templet2items);
                }
            }
            else if (QueryLangRuleValidatorUtil.notInLRV((String) dataRow.getValue(MmMDi18nConstants.RESID),
                    ruleExecContext.getRuntimeContext())) {
                // ��Ӵ�����Ϣ
                if (notInFileErrorMap.keySet().contains(funnode)
                        && notInFileErrorMap.get(funnode).keySet().contains(queryTempName)) {
                    notInFileErrorMap.get(funnode).put(queryTempName, field);
                }
                else {
                    MapList<String, String> templet2items = new MapList<String, String>();
                    templet2items.put(queryTempName, field);
                    notInFileErrorMap.put(funnode, templet2items);
                }
            }
        }

        if (MMValueCheck.isNotEmpty(notInDbErrorMap)) {
            for (Entry<String, MapList<String, String>> outEntry : notInDbErrorMap.entrySet()) {
                StringBuilder errorContxt1 = new StringBuilder();
                for (Entry<String, List<String>> inEntry : outEntry.getValue().entrySet()) {
                    errorContxt1.append("��ѯģ������[" + inEntry.getKey() + "]�ķ�Ԫ�����ֶΣ�" + inEntry.getValue() + "δ�����\n");
                }
                result.addResultElement(outEntry.getKey() + MmMDi18nConstants.ERROR_DATABASE_NULL,
                        errorContxt1.toString());
            }
        }
        if (MMValueCheck.isNotEmpty(notInFileErrorMap)) {
            for (Entry<String, MapList<String, String>> outEntry : notInFileErrorMap.entrySet()) {
                StringBuilder errorContxt1 = new StringBuilder();
                for (Entry<String, List<String>> inEntry : outEntry.getValue().entrySet()) {
                    errorContxt1.append("��ѯģ������[" + inEntry.getKey() + "]�ķ�Ԫ�����ֶΣ�" + inEntry.getValue()
                            + "������ģ���Ӧ�Ķ���jar��lang\\simpchn\\�ļ����¶����ļ��в����ڸò�ѯģ����Щ�ֶ����ƵĶ��\n");
                }
                result.addResultElement(outEntry.getKey() + MmMDi18nConstants.ERROR_DATABASE_NULL,
                        errorContxt1.toString());
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
        result.append("select distinct pc.resid,pc.field_code,pc.field_name,pt.model_name,pb.funnode from pub_query_condition pc,pub_query_templet pt, pub_systemplate_base pb  where pt.id=pc.pk_templet and pc.if_notmdcondition='Y' and pc.pk_templet=pb.templateid and pb.tempstyle='1' ");
        if (!MMValueCheck.isEmpty(funnodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("pb.funnode", funnodes));
        }
        return result.toString();
    }

}
