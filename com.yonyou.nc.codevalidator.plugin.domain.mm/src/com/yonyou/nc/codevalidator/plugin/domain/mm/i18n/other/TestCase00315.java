package com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMDi18nConstants;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
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
 * ��鵥�ݺŹ��������Ƿ������ﴦ��
 * 
 * @since 6.0
 * @version 2013-9-1 ����11:15:28
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_COMMON, description = "��鵥�ݺŹ��������Ƿ������ﴦ��", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "��ӵ��ݺŹ������ƶ���� pub_bcr_rulebase��rulename�ֶηǿ�  ", coder = "zhongcha", relatedIssueId = "315")
public class TestCase00315 extends AbstractScriptQueryRuleDefinition implements IGlobalRuleDefinition {

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
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

        for (DataRow dataRow : ds.getRows()) {
            String funcode = (String) dataRow.getValue("funcode");
            if (MMValueCheck.isNotEmpty(notDataFuncodes)) {
                notDataFuncodes.remove(funcode);
            }
            List<String> errorList = new ArrayList<String>();
            String entityName = (String) dataRow.getValue(MmMDi18nConstants.DISPLAY_NAME);
            if (MMValueCheck.isEmpty(dataRow.getValue("rulename"))) {
                errorList.add(entityName);
            }

            if (MMValueCheck.isNotEmpty(errorList)) {
                result.addResultElement(funcode + MmMDi18nConstants.ERROR_DATABASE_NULL, "�ڵ��Ӧ������Ϊ" + errorList
                        + "��Ԫ����ʵ������Ӧ�ĵ��ݺŹ���δ�����\n");
            }
        }
        if (MMValueCheck.isNotEmpty(notDataFuncodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "���ܽڵ����Ϊ" + notDataFuncodes
                    + "�Ľڵ㲻��������������ݡ�\n");
        }
        return result;
    }

    private String getSql(String[] funnodes) {
        // "select pr.rulename,mc.displayname from pub_bcr_rulebase pr,md_class mc,pub_bcr_nbcr pn where mc.id=pn.metaid and pn.code=pr.nbcrcode and mc.id in(select distinct mc.id from md_class mc "
        // "where mc.componentid in (select distinct mc.componentid from md_class mc ,sm_funcregister sf where mc.id=sf.mdid  and sf.funcode in ('10140BOMM','10140RT')"
        // ������sql������������ƣ���ͬ������:������Ϊ�˵õ����ܽڵ㣬��������/
        StringBuilder result = new StringBuilder();
        result.append("select distinct pr.rulename,mc.displayname,sf.funcode from pub_bcr_rulebase pr,md_class mc,pub_bcr_nbcr pn,md_class ml,md_class ms ,sm_funcregister sf where mc.id=pn.metaid and pn.code=pr.nbcrcode and mc.id=ml.id and ml.componentid=ms.componentid  and ms.id=sf.mdid ");
        result.append(" and isnull(pr.dr,0)=0  and isnull(mc.dr,0)=0 and isnull(pn.dr,0)=0 and isnull(ml.dr,0)=0 and isnull(ms.dr,0)=0 and isnull(sf.dr,0)=0");
        if (!MMValueCheck.isEmpty(funnodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("sf.funcode", funnodes));
        }
        return result.toString();
    }
}
