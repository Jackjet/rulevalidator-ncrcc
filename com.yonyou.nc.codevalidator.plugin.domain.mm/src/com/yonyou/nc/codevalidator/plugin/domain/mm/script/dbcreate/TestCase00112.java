package com.yonyou.nc.codevalidator.plugin.domain.mm.script.dbcreate;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ע�ᵥ�����ͣ��������ͱ���Ҫ���ϱ�����ı������
 * 
 * @since 6.0
 * @version 2013-9-22 ����7:15:47
 * @author zhongcha
 */
@RuleDefinition(catalog = CatalogEnum.CREATESCRIPT, subCatalog = SubCatalogEnum.CS_UNIFORM, description = "ע�ᵥ�����ͣ��������ͱ���Ҫ���ϱ�����ı������ ", specialParamDefine = {
    "�������&�淶ǰ׺"
}, memo = "���ڲ����������������͹淶ǰ׺,��ʽ��MMPAC&55(����MMPACΪ������룬55Ϊ����MMPAC�Ĺ淶ǰ׺)�������������,�ֿ�", solution = "�޸ĸõ������ͱ���ʹ�����������ı������  ", coder = "zhongcha", relatedIssueId = "112")
public class TestCase00112 extends AbstractScriptQueryRuleDefinition {

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] parameters = ruleExecContext.getParameterArray("�������&�淶ǰ׺");
        if (MMValueCheck.isEmpty(parameters) || parameters[0].trim().equals("")) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), "����Ϊ�գ������������\n");
            return result;
        }
        int errorNo = 0;
        for (String parameter : parameters) {
            String[] params = this.getSplitParameter(parameter);
            errorNo++;
            if (MMValueCheck.isEmpty(params)) {
                result.addResultElement("��" + errorNo + "������", "���������������޸Ĳ�����");
            }
            else {
                DataSet ds = this.executeQuery(ruleExecContext, this.getSql(params[0]));
                List<String> errorList = new ArrayList<String>();
                for (int i = 0; i < ds.getRowCount(); i++) {
                    String billtypecode = (String) ds.getValue(i, "pk_billtypecode");
                    if (billtypecode.length() <= params[1].length()) {
                        errorList.add(billtypecode);
                    }
                    else {
                        String prefix = billtypecode.substring(0, params[1].length());
                        if (!prefix.equals(params[1])) {
                            errorList.add(billtypecode);
                        }
                    }
                }
                if (MMValueCheck.isNotEmpty(errorList)) {
                    result.addResultElement(params[0], "�������Ϊ[" + params[0] + "]�ĵ������ͱ���Ϊ" + errorList
                            + "�ı��벻���ϸ�����ı������\n");
                }
            }
        }
        return result;
    }

    private String getSql(String domainCode) {
        return "select b.pk_billtypecode from bd_billtype b where b.istransaction='N' and isnull(b.dr,0)=0 and b.systemcode='"
                + domainCode + "'";
    }

    /**
     * ��������&�ֳ�����ֵ��String����
     * 
     * @param parameter
     * @return
     */
    public String[] getSplitParameter(String parameter) {
        if (MMValueCheck.isEmpty(parameter) || !parameter.contains("&") || parameter.indexOf("&") <= 0) {
            return null;
        }
        return parameter.split("&");
    }
}
