package com.yonyou.nc.codevalidator.plugin.domain.mm.script.init;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMDi18nConstants;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.rule.ClassLoaderUtilsFactory;
import com.yonyou.nc.codevalidator.sdk.rule.RuleClassLoadException;

/**
 * ����������չ����ֵ���ڴ��������ڶ�Ӧ����
 * 
 * @since 6.0
 * @version 2013-9-23 ����4:51:06
 * @author zhongcha
 */
@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, subCatalog = SubCatalogEnum.PS_CONTENTCHECK, description = "����������չ����ֵ���ڴ��������ڶ�Ӧ���� ", specialParamDefine = {
    "�������"
}, memo = "���ڲ����������������(��MMPAC)�������������,�ֿ�", solution = "���ڴ�������ӽ���������չ����ֵ��Ӧ���� ", coder = "zhongcha", relatedIssueId = "694")
public class TestCase00694 extends AbstractScriptQueryRuleDefinition {

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] domainCodes = ruleExecContext.getParameterArray("�������");
        if (MMValueCheck.isEmpty(domainCodes) || domainCodes[0].trim().equals("")) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), "����Ϊ�գ������������\n");
            return result;
        }
        List<String> notDataDomainCodes = new LinkedList<String>();
        notDataDomainCodes.addAll(Arrays.asList(domainCodes));
        for (String domainCode : domainCodes) {
            DataSet ds = this.executeQuery(ruleExecContext, this.getSql(domainCode));
            List<String> errorList = new ArrayList<String>();
            // ��MapList��keyΪ����������չ����ֵ��Ӧ���ࣨ��classname����valueΪ���Ӧ�Ľ������ͱ���id����pk_billtypeid����ֵ
            MapList<String, String> class2ids = new MapList<String, String>();
            for (int i = 0; i < ds.getRowCount(); i++) {
                notDataDomainCodes.remove(domainCode);
                String classname = (String) ds.getValue(i, "classname");
                String billtypeid = (String) ds.getValue(i, "pk_billtypeid");
                class2ids.put(classname, billtypeid);
            }
            if (MMValueCheck.isEmpty(class2ids)) {
                continue;
            }
            for (Entry<String, List<String>> class2code : class2ids.entrySet()) {
                // ��¼�������޷����ص��ࣨ���catch���쳣˵�������ڸ��ࣩ
                try {
                    ClassLoaderUtilsFactory.getClassLoaderUtils().loadClass(
                            ruleExecContext.getBusinessComponent().getProjectName(), class2code.getKey());
                }
                catch (RuleClassLoadException e) {
                    errorList.add(class2code.getKey());
                }

            }
            StringBuilder errorContxt = new StringBuilder();
            if (MMValueCheck.isNotEmpty(errorList)) {
                for (String classname : errorList) {
                    errorContxt.append("�������Ϊ[" + domainCode + "]�Ľ������ͱ���Ϊ" + class2ids.get(classname) + "�����Ӧ����"
                            + classname + "�ڴ����в����ڡ�\n");
                }
            }
            if (MMValueCheck.isNotEmpty(errorContxt)) {
                result.addResultElement(domainCode, errorContxt.toString());
            }
        }
        if (MMValueCheck.isNotEmpty(notDataDomainCodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "�������Ϊ" + notDataDomainCodes
                    + "����������������ݡ�\n");
        }
        return result;
    }

    private String getSql(String domainCode) {
        return "select b2.classname,b2.pk_billtypeid from bd_billtype2 b2, bd_billtype b where b2.pk_billtypeid=b.pk_billtypeid and b.istransaction='N' and b.canextendtransaction='Y' and isnull(b2.dr,0)=0  and isnull(b.dr,0)=0  and (b.systemcode='"
                + domainCode.toLowerCase() + "'  or  " + "b.systemcode='" + domainCode.toUpperCase() + "')";
    }

}
