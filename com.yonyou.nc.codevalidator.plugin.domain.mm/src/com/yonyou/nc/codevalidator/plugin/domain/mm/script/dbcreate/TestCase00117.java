package com.yonyou.nc.codevalidator.plugin.domain.mm.script.dbcreate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

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
 * Ԥ�ý������͵�transtype_class�ֶα������뽻�����͵�VO���Ƿ����
 * 
 * @since 6.0
 * @version 2013-9-22 ����7:48:48
 * @author zhongcha
 */
@RuleDefinition(catalog = CatalogEnum.CREATESCRIPT, subCatalog = SubCatalogEnum.CS_UNIFORM, description = "Ԥ�ý������͵�transtype_class�ֶα������뽻�����͵�VO���Ƿ���� ", specialParamDefine = {
    "�������"
}, memo = "���ڲ����������������(��MMPAC)�������������,�ֿ�", solution = "����Ӹ�Ԥ�ý������͵�transtype_class�ֶα������뽻�����͵�VO�� ", coder = "zhongcha", relatedIssueId = "117")
public class TestCase00117 extends AbstractScriptQueryRuleDefinition {

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] domainCodes = ruleExecContext.getParameterArray("�������");
        if (MMValueCheck.isEmpty(domainCodes) || domainCodes[0].trim().equals("")) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), "����Ϊ�գ������������\n");
            return result;
        }
        for (String domainCode : domainCodes) {
            DataSet ds = this.executeQuery(ruleExecContext, this.getSql(domainCode));
            if (MMValueCheck.isEmpty(ds)) {
                // ���ٴ���һ��Ĭ�ϵĽ�������
                result.addResultElement(domainCode, "�������Ϊ[" + domainCode + "]�Ĳ�����Ĭ�Ͻ�������");
                continue;
            }
            List<String> errorList1 = new ArrayList<String>();
            List<String> errorList2 = new ArrayList<String>();
            // ��MapList��keyΪ���ݽ������͵�VO�ࣨ��transtype_class����valueΪ���Ӧ�Ľ������ͱ��루��pk_billtypecode����ֵ
            MapList<String, String> class2codes = new MapList<String, String>();
            for (int i = 0; i < ds.getRowCount(); i++) {
                String transtypeclass = (String) ds.getValue(i, "transtype_class");
                String billtypecode = (String) ds.getValue(i, "pk_billtypecode");
                class2codes.put(transtypeclass, billtypecode);
            }
            for (Entry<String, List<String>> class2code : class2codes.entrySet()) {
                // ��¼���ݽ������͵�VO��Ϊ�յĽ������ͱ���
                if (MMValueCheck.isEmpty(class2code.getKey())) {
                    errorList1 = class2code.getValue();
                }
                else {
                    // ��¼�������޷����ص�VO�ࣨ���catch���쳣˵�������ڸ�VO�ࣩ
                    try {
                        ClassLoaderUtilsFactory.getClassLoaderUtils().loadClass(
                                ruleExecContext.getBusinessComponent().getProjectName(), class2code.getKey());
                    }
                    catch (RuleClassLoadException e) {
                        errorList2.add(class2code.getKey());
                    }
                }
            }
            StringBuilder errorContxt = new StringBuilder();
            if (MMValueCheck.isNotEmpty(errorList1)) {
                errorContxt.append("�������Ϊ[" + domainCode + "]�Ľ������ͱ���Ϊ" + errorList1 + "�����Ӧ�Ľ������͵�VO���ֵΪ�ա�\n");

            }
            if (MMValueCheck.isNotEmpty(errorList2)) {
                for (String voclass : errorList2) {
                    errorContxt.append("�������Ϊ[" + domainCode + "]�Ľ������ͱ���Ϊ" + class2codes.get(voclass) + "�����Ӧ�Ľ������͵�VO��"
                            + voclass + "�ڴ����в����ڡ�\n");
                }
            }
            if (MMValueCheck.isNotEmpty(errorContxt)) {
                result.addResultElement(domainCode, errorContxt.toString());
            }
        }
        return result;
    }

    private String getSql(String domainCode) {
        return "select distinct t.transtype_class,t.pk_billtypecode from bd_billtype t where  t.pk_group='global00000000000000'  and isnull(t.dr,0)=0  and t.parentbilltype in (select b.pk_billtypeid from bd_billtype b where b.istransaction='N'and b.canextendtransaction='Y' and isnull(b.dr,0)=0  and b.systemcode='"
                + domainCode + "' )";
    }

}
