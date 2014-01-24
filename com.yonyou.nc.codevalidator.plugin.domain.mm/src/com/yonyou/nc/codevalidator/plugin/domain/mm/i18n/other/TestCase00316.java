package com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.other;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
 * ���ҵ�������Ƿ������ﴦ��
 * 
 * @since 6.0
 * @version 2013-9-1 ����12:19:20
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_COMMON, description = "���ҵ�������Ƿ������ﴦ��", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "��Ӹ�ҵ�����ƶ�� ��sm_busiactivereg��resid�ǿ� ", coder = "zhongcha", relatedIssueId = "316")
public class TestCase00316 extends AbstractLangRuleDefinition implements IGlobalRuleDefinition {

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] funcodes = ruleExecContext.getParameterArray(CommonRuleParams.FUNCNODEPARAM);
        List<String> notDataFuncodes = new LinkedList<String>();
        if (MMValueCheck.isNotEmpty(funcodes)) {
            notDataFuncodes.addAll(Arrays.asList(funcodes));
        }
        else {
            Logger.warn("��ǰ�����ܽڵ����Ϊ�գ���������й��ܽڵ㣡");
        }
        DataSet ds = this.executeQuery(ruleExecContext, this.getSql(funcodes));

        MapList<String, String> errorMapList1 = new MapList<String, String>();
        MapList<String, String> errorMapList2 = new MapList<String, String>();
        for (DataRow dataRow : ds.getRows()) {
            String funcode = (String) dataRow.getValue("funcode");
            if (MMValueCheck.isNotEmpty(funcodes)) {
                notDataFuncodes.remove(funcode);
            }
            String activeName = (String) dataRow.getValue("name");

            if (MMValueCheck.isEmpty(dataRow.getValue(MmMDi18nConstants.RESID))) {
                errorMapList1.put(funcode, activeName);
            }
            else if (QueryLangRuleValidatorUtil.notInLRV((String) dataRow.getValue(MmMDi18nConstants.RESID),
                    ruleExecContext.getRuntimeContext())) {
                errorMapList2.put(funcode, activeName);
            }
        }
        if (MMValueCheck.isNotEmpty(errorMapList1)) {
            for (Map.Entry<String, List<String>> entry : errorMapList1.entrySet()) {
                result.addResultElement(entry.getKey() + MmMDi18nConstants.ERROR_DATABASE_NULL,
                        "���ܽڵ����Ϊ[" + entry.getKey() + "]��ҵ������Ϊ" + entry.getValue() + "δ�����\n");
            }
        }
        if (MMValueCheck.isNotEmpty(errorMapList2)) {
            for (Map.Entry<String, List<String>> entry : errorMapList1.entrySet()) {
                result.addResultElement(entry.getKey() + MmMDi18nConstants.ERROR_LANGFILE_NULL,
                        "���ܽڵ����Ϊ[" + entry.getKey() + "]��ҵ������Ϊ" + entry.getValue()
                                + "����ģ���Ӧ�Ķ���jar��lang\\simpchn\\�ļ�����Ԫ���ݶ����ļ��в����ڸ�ҵ�����ƵĶ��\n");
            }
        }
        if (MMValueCheck.isNotEmpty(notDataFuncodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "���ܽڵ����Ϊ" + notDataFuncodes
                    + "�Ľڵ㲻��������������ݡ�\n");
        }
        return result;
    }

    private String getSql(String[] funcodes) {
        StringBuilder result = new StringBuilder();
        result.append("select distinct sb.resid,sb.name,sf.funcode from sm_busiactivereg sb,sm_funcregister sf where sf.cfunid=sb.parent_id and isnull(sb.dr,0)=0  and isnull(sf.dr,0)=0");
        if (!MMValueCheck.isEmpty(funcodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("sf.funcode", funcodes));
        }
        return result.toString();
    }

}
