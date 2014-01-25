package com.yonyou.nc.codevalidator.plugin.domain.mm.script.init;

import com.yonyou.nc.codevalidator.plugin.domain.mm.uif.util.SQLBuilder;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.IGlobalRuleDefinition;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * VO����ʱӦ���Զ����������νӿڶ�Ӧ��ϵ����Ŀ�ĵ��ݵĽ�������
 * 
 * @since 6.0
 * @version 2013-11-14 ����2:46:37
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.PRESCRIPT, subCatalog = SubCatalogEnum.PS_CONTENTCHECK, description = "VO����ʱӦ���Զ����������νӿڶ�Ӧ��ϵ����Ŀ�ĵ��ݵĽ�������", specialParamDefine = {
    "��Դ��������", "Ŀ�ĵ�������"
}, solution = "��Ӹõ����������ƶ����bd_billtype��billtypename�ֶβ�Ϊ�գ��� istransaction='N' ", coder = "zhongcha", relatedIssueId = "254", memo = "�����ɿգ���������ֵ��Ϊ����Ĭ�ϼ�����е����ݡ�����������ã�����")
public class TestCase00254 extends AbstractScriptQueryRuleDefinition implements IGlobalRuleDefinition {

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] srcbilltypes = ruleExecContext.getParameterArray("��Դ��������");
        String[] destbilltypes = ruleExecContext.getParameterArray("Ŀ�ĵ�������");
        if (MMValueCheck.isEmpty(srcbilltypes) || srcbilltypes[0].trim().equals("")) {
            srcbilltypes = null;
        }
        if (MMValueCheck.isEmpty(destbilltypes) || destbilltypes[0].trim().equals("")) {
            destbilltypes = null;
        }
        DataSet ds = this.executeQuery(ruleExecContext, this.getSql(srcbilltypes, destbilltypes));
        MapList<String, String> errorMapList = new MapList<String, String>();
        for (int i = 0; i < ds.getRowCount(); i++) {
            String srcbilltype = (String) ds.getValue(i, "src_billtype");
            String destbilltype = (String) ds.getValue(i, "dest_billtype");
            if (MMValueCheck.isEmpty(ds.getValue(i, "dest_transtype"))) {
                errorMapList.put(srcbilltype, destbilltype);
            }
        }
        StringBuilder errorContxt = new StringBuilder();
        if (MMValueCheck.isNotEmpty(errorMapList)) {
            for (String srcbilltype : errorMapList.keySet()) {
                errorContxt.append("��Դ��������Ϊ[ " + srcbilltype + " ]Ŀ�ĵ�������Ϊ" + errorMapList.get(srcbilltype)
                        + "�ڵ��ݽӿڶ�����û������Ĭ��Ŀ�ĵ��ݵĽ������͡�\n");
            }
        }
        if (MMValueCheck.isNotEmpty(errorContxt)) {
            result.addResultElement("����", errorContxt.toString());
        }
        return result;
    }

    private String getSql(String[] srcbilltypes, String[] destbilltypes) {
        SQLBuilder sql = new SQLBuilder();
        sql.append("select distinct src_billtype,dest_billtype,dest_transtype from pub_billitfdef  where   isnull(dr,0)=0");
        if (MMValueCheck.isNotEmpty(srcbilltypes) || MMValueCheck.isNotEmpty(destbilltypes)) {
            sql.append(" and ");
        }
        if (MMValueCheck.isNotEmpty(srcbilltypes)) {
            sql.append("src_billtype", srcbilltypes);
        }
        if (MMValueCheck.isNotEmpty(srcbilltypes) && MMValueCheck.isNotEmpty(destbilltypes)) {
            sql.append(" and ");
        }
        if (MMValueCheck.isNotEmpty(destbilltypes)) {
            sql.append("dest_billtype", destbilltypes);
        }
        return sql.toString();
    }
}
