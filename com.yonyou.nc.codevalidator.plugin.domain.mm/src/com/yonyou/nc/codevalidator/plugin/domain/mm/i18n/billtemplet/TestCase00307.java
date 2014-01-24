package com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.billtemplet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.util.QueryLangRuleValidatorUtil;
import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMDi18nConstants;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
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

/**
 * �����ӱ�ҳǩ���ƶ��ﴦ��
 * 
 * @author qiaoyang
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_COMMON, description = "�����ӱ�ҳǩ���ƶ��ﴦ��", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "", coder = "qiaoyang", relatedIssueId = "307")
public class TestCase00307 extends AbstractLangRuleDefinition implements IGlobalRuleDefinition {

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        StringBuffer noteBuilder = new StringBuffer();
        String[] funcNodes = ruleExecContext.getParameterArray(CommonRuleParams.FUNCNODEPARAM);
        List<String> notDataFunnodes = new ArrayList<String>();
        if (MMValueCheck.isNotEmpty(funcNodes)) {
            List<String> funcnodes = Arrays.asList(funcNodes);
            DataSet ds = SQLQueryExecuteUtils.executeQuery(this.getSql(funcNodes), ruleExecContext.getRuntimeContext());
            for (DataRow dataRow : ds.getRows()) {
                String funnode = (String) dataRow.getValue("funnode");
                String templetName = (String) dataRow.getValue(MmMDi18nConstants.BILL_TEMPLETCAPTION);
                String tabName = (String) dataRow.getValue(MmMDi18nConstants.BILL_TABNAME);
                String resId = (String) dataRow.getValue(MmMDi18nConstants.RESID);
                if (resId == null) {
                    noteBuilder.append(templetName + "(" + funnode + ")�е�" + tabName + "ҳǩ����δ������\n");
                }
                else if (QueryLangRuleValidatorUtil.notInLRV(resId, ruleExecContext.getRuntimeContext())) {
                    noteBuilder.append(templetName + "(" + funnode + ")�е�" + tabName + "ҳǩ������Դ(" + resId
                            + ")�������ڶ�Ӧ�Ļ��������ļ���\n");
                }
                if (!funcnodes.contains(funnode)) {
                    notDataFunnodes.add(funnode);
                }
            }
            if (notDataFunnodes.size() > 0) {
                noteBuilder.append(notDataFunnodes.toString() + "δ�ҵ�����ģ��ҳǩ\n");
            }
        }
        else {
            DataSet ds = SQLQueryExecuteUtils.executeQuery(this.getSql(funcNodes), ruleExecContext.getRuntimeContext());
            for (DataRow dataRow : ds.getRows()) {
                String funnode = (String) dataRow.getValue("funnode");
                String templetName = (String) dataRow.getValue(MmMDi18nConstants.BILL_TEMPLETCAPTION);
                String tabName = (String) dataRow.getValue(MmMDi18nConstants.BILL_TABNAME);
                String resId = (String) dataRow.getValue(MmMDi18nConstants.RESID);
                if (resId == null) {
                    noteBuilder.append(templetName + "(" + funnode + ")�е�" + tabName + "ҳǩ����δ������\n");
                }
                else if (QueryLangRuleValidatorUtil.notInLRV(resId, ruleExecContext.getRuntimeContext())) {
                    noteBuilder.append(templetName + "(" + funnode + ")�е�" + tabName + "ҳǩ������Դ(" + resId
                            + ")�������ڶ�Ӧ�Ļ��������ļ���\n");
                }
            }
        }
        if (noteBuilder.length() > 0) {
            result.addResultElement("ҳǩ���ƶ���", noteBuilder.toString());
        }

        return result;
    }

    private String getSql(String[] funnodes) {
        StringBuilder result = new StringBuilder();
        result.append("select distinct pt.resid,p.bill_templetcaption,pt.tabname,pb.funnode from pub_billtemplet_t pt ,pub_systemplate_base pb, pub_billtemplet p where pt.pk_billtemplet=pb.templateid and p.pk_billtemplet = pt.pk_billtemplet and p.pk_billtemplet=pb.templateid  and pb.tempstyle='0' ");
        if (!MMValueCheck.isEmpty(funnodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("pb.funnode", funnodes));
        }
        result.append(" order by pb.funnode");
        return result.toString();
    }

}
