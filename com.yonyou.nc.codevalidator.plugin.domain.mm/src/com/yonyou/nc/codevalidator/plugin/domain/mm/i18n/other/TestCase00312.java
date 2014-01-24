package com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
 * ���˵������Ƿ������ﴦ��
 * 
 * @since 6.0
 * @version 2013-9-1 ����9:38:28
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_COMMON, description = "���˵������Ƿ������ﴦ��", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "��Ӹò˵����ƶ���� sm_menuitemreg��Ӧ��resid��Ϊ�� ", coder = "zhongcha", relatedIssueId = "312")
public class TestCase00312 extends AbstractLangRuleDefinition implements IGlobalRuleDefinition {

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
        // ���ݿ���resid�����ڵ�MapList<���ܽڵ�,�˵���>
        MapList<String, String> notInDbFun2menus = new MapList<String, String>();
        // �����ļ��в����ڶ����MapList<���ܽڵ�,�˵���>
        MapList<String, String> notInFileFun2menus = new MapList<String, String>();
        // ���ݿ���resid�����ڵĲ˵���������û�ж�Ӧ�Ĺ��ܽڵ�
        List<String> notInDbMenuList = new ArrayList<String>();
        // �����ļ��в����ڶ���Ĳ˵���������û�ж�Ӧ�Ĺ��ܽڵ�
        List<String> notInFileMenuList = new ArrayList<String>();
        for (DataRow dataRow : ds.getRows()) {
            String funcode = (String) dataRow.getValue("funcode");
            String menuItemName = (String) dataRow.getValue("menuitemname");
            if (MMValueCheck.isNotEmpty(funcodes)) {
                notDataFuncodes.remove(funcode);
            }
            if (MMValueCheck.isEmpty((String) dataRow.getValue(MmMDi18nConstants.RESID))) {
                if (MMValueCheck.isNotEmpty(funcodes)) {
                    notInDbFun2menus.put(funcode, menuItemName);
                }
                else {
                    notInDbMenuList.add(menuItemName);
                }
            }
            else if (QueryLangRuleValidatorUtil.notInLRV((String) dataRow.getValue(MmMDi18nConstants.RESID),
                    ruleExecContext.getRuntimeContext())) {
                if (MMValueCheck.isNotEmpty(funcodes)) {
                    notInFileFun2menus.put(funcode, menuItemName);
                }
                else {
                    notInFileMenuList.add(menuItemName);
                }
            }
        }
        if (MMValueCheck.isNotEmpty(notInDbFun2menus)) {
            for (Entry<String, List<String>> entry : notInDbFun2menus.entrySet()) {
                result.addResultElement(entry.getKey() + MmMDi18nConstants.ERROR_DATABASE_NULL,
                        "���ܽڵ��Ϊ[" + entry.getKey() + "]�Ĳ˵�[" + entry.getValue() + "]δ�����\n");
            }
        }
        if (MMValueCheck.isNotEmpty(notInFileFun2menus)) {
            for (Entry<String, List<String>> entry : notInFileFun2menus.entrySet()) {
                result.addResultElement(entry.getKey() + MmMDi18nConstants.ERROR_LANGFILE_NULL,
                        "���ܽڵ��Ϊ[" + entry.getKey() + "]�Ĳ˵�[" + entry.getValue()
                                + "]������ģ���Ӧ�Ķ���jar��lang\\simpchn\\�ļ�����Ԫ���ݶ����ļ��в����ڸ�Ԫ�������ƶ��\n");
            }
        }
        if (MMValueCheck.isNotEmpty(notInDbMenuList)) {
            result.addResultElement(MmMDi18nConstants.ERROR_DATABASE_NULL, "�˵���Ϊ" + notInDbMenuList + "δ�����\n");

        }
        if (MMValueCheck.isNotEmpty(notInFileMenuList)) {
            result.addResultElement(MmMDi18nConstants.ERROR_LANGFILE_NULL, "�˵���Ϊ" + notInFileMenuList
                    + "������ģ���Ӧ�Ķ���jar��lang\\simpchn\\�ļ�����Ԫ���ݶ����ļ��в����ڸ�Ԫ�������ƶ��\n");

        }
        if (MMValueCheck.isNotEmpty(notDataFuncodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "���ܽڵ����Ϊ" + notDataFuncodes
                    + "�Ľڵ㲻��������������ݡ�\n");
        }
        return result;
    }

    private String getSql(String[] funcodes) {
        StringBuilder result = new StringBuilder();
        result.append("select distinct resid,menuitemname,funcode from sm_menuitemreg  where  isnull(dr,0)=0 ");
        if (!MMValueCheck.isEmpty(funcodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("funcode", funcodes));
        }
        return result.toString();
    }

}
