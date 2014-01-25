package com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.billtemplet;

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
 * ��鵥��ģ���з�Ԫ�����ֶεĶ���
 * 
 * @since 6.0
 * @version 2013-8-31 ����5:45:19
 * @author zhongcha
 */
@RuleDefinition(catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_BILLTEMP, description = "��鵥��ģ���з�Ԫ�����ֶεĶ���", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "��Ӹõ���ģ���ֶεĶ������Ӧpub_billtemplet_b��metadataproperty is null���ֶζ�Ӧ��resid��Ϊ�� ", coder = "zhongcha", relatedIssueId = "308", executeLayer = ExecuteLayer.GLOBAL)
public class TestCase00308 extends AbstractLangRuleDefinition implements IGlobalRuleDefinition {

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
        // ���residΪ�յĴ�����Ϣ Map<���ܽڵ����, MapList<����ģ������, ��Ԫ�����ֶ�>>
        Map<String, MapList<String, String>> notInDbErrorMap = new HashMap<String, MapList<String, String>>();
        // �����ļ��в����ڵĴ�����Ϣ Map<���ܽڵ����, MapList<����ģ������, ��Ԫ�����ֶ�>>
        Map<String, MapList<String, String>> notInFileErrorMap = new HashMap<String, MapList<String, String>>();
        DataSet ds = this.executeQuery(ruleExecContext, this.getSql(funnodes));
        for (DataRow dataRow : ds.getRows()) {
            String templetName = (String) dataRow.getValue(MmMDi18nConstants.BILL_TEMPLETCAPTION);
            String itemName = (String) dataRow.getValue(MmMDi18nConstants.DEFAULT_SHOW_NAME);
            if (MMValueCheck.isEmpty(itemName)) {
                itemName = (String) dataRow.getValue("itemkey");
            }
            String funnode = (String) dataRow.getValue("funnode");
            if (MMValueCheck.isNotEmpty(funnodes)) {
                notDataFunnodes.remove(funnode);
            }
            if (MMValueCheck.isEmpty(dataRow.getValue(MmMDi18nConstants.RESID))) {
                // ��Ӵ�����Ϣ
                if (notInDbErrorMap.keySet().contains(funnode)
                        && notInDbErrorMap.get(funnode).keySet().contains(templetName)) {
                    notInDbErrorMap.get(funnode).put(templetName, itemName);
                }
                else {
                    MapList<String, String> templet2items = new MapList<String, String>();
                    templet2items.put(templetName, itemName);
                    notInDbErrorMap.put(funnode, templet2items);
                }
            }
            else if (QueryLangRuleValidatorUtil.notInLRV((String) dataRow.getValue(MmMDi18nConstants.RESID),
                    ruleExecContext.getRuntimeContext())) {
                // ��Ӵ�����Ϣ
                if (notInFileErrorMap.keySet().contains(funnode)
                        && notInFileErrorMap.get(funnode).keySet().contains(templetName)) {
                    notInFileErrorMap.get(funnode).put(templetName, itemName);
                }
                else {
                    MapList<String, String> templet2items = new MapList<String, String>();
                    templet2items.put(templetName, itemName);
                    notInFileErrorMap.put(funnode, templet2items);
                }
            }
        }
        if (MMValueCheck.isNotEmpty(notInDbErrorMap)) {
            for (Entry<String, MapList<String, String>> outEntry : notInDbErrorMap.entrySet()) {
                StringBuilder errorContxt1 = new StringBuilder();
                for (Entry<String, List<String>> inEntry : outEntry.getValue().entrySet()) {
                    errorContxt1.append("����ģ������Ϊ[" + inEntry.getKey() + "]���ֶ���Ϊ" + inEntry.getValue() + "δ�����\n");
                }
                result.addResultElement(outEntry.getKey() + MmMDi18nConstants.ERROR_DATABASE_NULL,
                        errorContxt1.toString());
            }
        }
        if (MMValueCheck.isNotEmpty(notInFileErrorMap)) {
            for (Entry<String, MapList<String, String>> outEntry : notInFileErrorMap.entrySet()) {
                StringBuilder errorContxt2 = new StringBuilder();
                for (Entry<String, List<String>> inEntry : outEntry.getValue().entrySet()) {
                    errorContxt2.append("����ģ������Ϊ[" + inEntry.getKey() + "]���ֶ���Ϊ" + inEntry.getValue()
                            + "������ģ���Ӧ�Ķ���jar��lang\\simpchn\\�ļ����¶����ļ��в�������Щ�ֶ����ƵĶ��\n");
                }
                result.addResultElement(outEntry.getKey() + MmMDi18nConstants.ERROR_LANGFILE_NULL,
                        errorContxt2.toString());
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
        result.append("select distinct b.resid,b.defaultshowname,pt.bill_templetcaption,pb.funnode,b.itemkey from pub_billtemplet pt,pub_billtemplet_b b, pub_systemplate_base pb where b.metadataproperty is null and pt.pk_billtemplet=b.pk_billtemplet and pt.pk_billtemplet=pb.templateid and pb.tempstyle='0'  and isnull(pt.dr,0)=0  and isnull(b.dr,0)=0 and isnull(pb.dr,0)=0");
        if (!MMValueCheck.isEmpty(funnodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("pb.funnode", funnodes));
        }
        return result.toString();
    }
}
