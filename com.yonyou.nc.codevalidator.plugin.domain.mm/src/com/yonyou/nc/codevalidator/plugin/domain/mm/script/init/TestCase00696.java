package com.yonyou.nc.codevalidator.plugin.domain.mm.script.init;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMDi18nConstants;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
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
 * ��ⵥ��ģ�塢��ѯģ�塢��ӡģ���layer�ֶ��Ƿ�Ϊˮƽ��Ʒ
 * 
 * @since 6.0
 * @version 2013-9-25 ����9:38:16
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.PRESCRIPT, subCatalog = SubCatalogEnum.PS_UNIFORM, description = "��ⵥ��ģ�塢��ѯģ�塢��ӡģ���layer�ֶ��Ƿ�Ϊˮƽ��Ʒ ", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "�޸ĸõ���ģ�塢��ѯģ�塢��ӡģ���layer�ֶ�Ϊˮƽ��Ʒ,��0  ", coder = "zhongcha", relatedIssueId = "696")
public class TestCase00696 extends AbstractScriptQueryRuleDefinition implements IGlobalRuleDefinition {
    private static final String BILL_TEMPLET = "����ģ��";

    private static final String QUERY_TEMPLET = "��ѯģ��";

    private static final String PRINT_TEMPLET = "��ӡģ��";

    @Override
    public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] funnodes = ruleExecContext.getParameterArray(CommonRuleParams.FUNCNODEPARAM);
        List<String> notDataFunnodes = new LinkedList<String>();
        if (MMValueCheck.isNotEmpty(funnodes)) {
            notDataFunnodes.addAll(Arrays.asList(funnodes));
        }
        else {
            Logger.warn("��ǰ�����ܽڵ����Ϊ�գ���������й��ܽڵ㣡");
        }
        // �ڱ�pub_systemplate_base�д������Ϣ��Map<���ܽڵ����, MapList<ģ������, ģ������>>
        Map<String, MapList<String, String>> notInSysErrorMap = new HashMap<String, MapList<String, String>>();
        // �ڸ���ģ����д������Ϣ��Map<���ܽڵ����, MapList<ģ������, ģ������>>
        Map<String, MapList<String, String>> notInTemplErrorMap = new HashMap<String, MapList<String, String>>();
        // ��鵥��ģ��
        DataSet billtempletds = this.executeQuery(ruleExecContext, this.getBillTempletSql(funnodes));
        for (int i = 0; i < billtempletds.getRowCount(); i++) {
            // layer1Ϊpub_systemplet_base���е�layer�ֶε�ֵ��layer2Ϊ��������ģ���ڸ���ģ�����layer�ֶε�ֵ����ͬ
            Integer layer1 = (Integer) billtempletds.getValue(i, "layer");
            Integer layer2 = (Integer) billtempletds.getValue(i, "1_layer");
            String btempletname = (String) billtempletds.getValue(i, "bill_templetcaption");
            String funnode = (String) billtempletds.getValue(i, "funnode");
            // ��û���ҵ�������ȡ���Ѿ��ҵ����ݵĹ��ܽڵ�
            if (MMValueCheck.isNotEmpty(funnodes)) {
                notDataFunnodes.remove(funnode);
            }
            if (!Integer.valueOf(0).equals(layer1)) {
                this.putErrorContxt(notInSysErrorMap, funnode, TestCase00696.BILL_TEMPLET, btempletname);
            }
            if (!Integer.valueOf(0).equals(layer2)) {
                this.putErrorContxt(notInTemplErrorMap, funnode, TestCase00696.BILL_TEMPLET, btempletname);
            }
        }
        // ����ѯģ��
        DataSet querytempletds = this.executeQuery(ruleExecContext, this.getQueryTempletSql(funnodes));
        for (int i = 0; i < querytempletds.getRowCount(); i++) {
            Integer layer1 = (Integer) querytempletds.getValue(i, "layer");
            Integer layer2 = (Integer) querytempletds.getValue(i, "1_layer");
            String qtempletname = (String) querytempletds.getValue(i, "model_name");
            String funnode = (String) querytempletds.getValue(i, "funnode");
            // ��û���ҵ�������ȡ���Ѿ��ҵ����ݵĹ��ܽڵ�
            if (MMValueCheck.isNotEmpty(funnodes)) {
                notDataFunnodes.remove(funnode);
            }
            if (!Integer.valueOf(0).equals(layer1)) {
                this.putErrorContxt(notInSysErrorMap, funnode, TestCase00696.QUERY_TEMPLET, qtempletname);
            }
            if (!Integer.valueOf(0).equals(layer2)) {
                this.putErrorContxt(notInTemplErrorMap, funnode, TestCase00696.QUERY_TEMPLET, qtempletname);
            }
        }
        // ����ӡģ��
        DataSet ptempletds = this.executeQuery(ruleExecContext, this.getPrintTempletSql(funnodes));
        for (int i = 0; i < ptempletds.getRowCount(); i++) {
            Integer layer1 = (Integer) ptempletds.getValue(i, "layer");
            Integer layer2 = (Integer) ptempletds.getValue(i, "1_layer");
            String ptempletname = (String) ptempletds.getValue(i, "vtemplatename");
            String funnode = (String) ptempletds.getValue(i, "funnode");
            // ��û���ҵ�������ȡ���Ѿ��ҵ����ݵĹ��ܽڵ�
            if (MMValueCheck.isNotEmpty(funnodes)) {
                notDataFunnodes.remove(funnode);
            }
            if (!Integer.valueOf(0).equals(layer1)) {
                this.putErrorContxt(notInSysErrorMap, funnode, TestCase00696.PRINT_TEMPLET, ptempletname);
            }
            if (!Integer.valueOf(0).equals(layer2)) {
                this.putErrorContxt(notInTemplErrorMap, funnode, TestCase00696.PRINT_TEMPLET, ptempletname);
            }
        }
        if (MMValueCheck.isNotEmpty(notInSysErrorMap)) {
            for (Entry<String, MapList<String, String>> outEntry : notInSysErrorMap.entrySet()) {
                StringBuilder errorContxt = new StringBuilder();
                for (Entry<String, List<String>> inEntry : outEntry.getValue().entrySet()) {
                    errorContxt.append("����Ϊ" + inEntry.getValue() + "��" + inEntry.getKey()
                            + "�ڱ�pub_systemplate_base��layer�ֶβ���ˮƽ��Ʒ����layer��ֵ��Ϊ0��\n");
                }
                result.addResultElement("��������1�����ܽڵ����Ϊ[" + outEntry.getKey() + "]", errorContxt.toString());
            }
        }

        if (MMValueCheck.isNotEmpty(notInTemplErrorMap)) {
            // ���ڱ�������ģ���Ӧ�ı���
            Map<String, String> templetTable = new HashMap<String, String>();
            templetTable.put(TestCase00696.BILL_TEMPLET, "pub_billtemplet");
            templetTable.put(TestCase00696.QUERY_TEMPLET, "pub_query_templet");
            templetTable.put(TestCase00696.PRINT_TEMPLET, "pub_print_template");
            for (Entry<String, MapList<String, String>> outEntry : notInTemplErrorMap.entrySet()) {
                StringBuilder errorContxt = new StringBuilder();
                for (Entry<String, List<String>> inEntry : outEntry.getValue().entrySet()) {
                    errorContxt.append("����Ϊ" + inEntry.getValue() + "��" + inEntry.getKey() + "�ڱ�"
                            + templetTable.get(inEntry.getKey()) + "��layer�ֶβ���ˮƽ��Ʒ����layer��ֵ��Ϊ0��\n");
                }
                result.addResultElement("��������2�����ܽڵ����Ϊ[" + outEntry.getKey() + "]", errorContxt.toString());
            }
        }
        if (MMValueCheck.isNotEmpty(notDataFunnodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "���ܽڵ����Ϊ" + notDataFunnodes
                    + "�Ľڵ㲻��������������ݡ�\n");
        }
        return result;
    }

    /**
     * ��������Ϣ����map��
     * 
     * @param errorMap��������Ϣmap
     * @param funnode�����ܽڵ����
     * @param templetType��ģ������
     * @param templetName��ģ������
     */
    private void putErrorContxt(Map<String, MapList<String, String>> errorMap, String funnode, String templetType,
            String templetName) {
        // ��Ӵ�����Ϣ
        if (errorMap.keySet().contains(funnode) && errorMap.get(funnode).keySet().contains(templetType)) {
            errorMap.get(funnode).put(templetType, templetName);
        }
        else {
            MapList<String, String> templetType2Name = new MapList<String, String>();
            templetType2Name.put(templetType, templetName);
            errorMap.put(funnode, templetType2Name);
        }
    }

    /**
     * ���ݹ��ܽڵ�����ѯϵͳĬ�ϵ���ģ���sql
     * 
     * @param funcode
     * @return
     */
    private String getBillTempletSql(String[] funnodes) {
        StringBuilder result = new StringBuilder();
        result.append("select ps.layer,pb.layer,pb.bill_templetcaption,ps.funnode from pub_systemplate_base ps,pub_billtemplet pb where ps.templateid=pb.pk_billtemplet and ps.tempstyle=0  and isnull(ps.dr,0)=0  and isnull(pb.dr,0)=0 ");
        if (!MMValueCheck.isEmpty(funnodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("ps.funnode", funnodes));
        }
        return result.toString();
    }

    /**
     * ���ݹ��ܽڵ�����ѯϵͳĬ�ϲ�ѯģ���sql
     * 
     * @param funcode
     * @return
     */
    private String getQueryTempletSql(String[] funnodes) {
        StringBuilder result = new StringBuilder();
        result.append("select ps.layer,pt.layer,pt.model_name,ps.funnode from pub_systemplate_base ps,pub_query_templet pt where ps.templateid=pt.id and ps.tempstyle=1   and isnull(ps.dr,0)=0  and isnull(pt.dr,0)=0 ");
        if (!MMValueCheck.isEmpty(funnodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("ps.funnode", funnodes));
        }
        return result.toString();
    }

    /**
     * ���ݹ��ܽڵ�����ѯϵͳĬ�ϴ�ӡģ���sql
     * 
     * @param funcode
     * @return
     */
    private String getPrintTempletSql(String[] funnodes) {
        StringBuilder result = new StringBuilder();
        result.append("select ps.layer,pp.layer,pp.vtemplatename,ps.funnode from pub_systemplate_base ps,pub_print_template pp where ps.templateid=pp.ctemplateid and ps.tempstyle=3   and isnull(ps.dr,0)=0  and isnull(pp.dr,0)=0 ");
        if (!MMValueCheck.isEmpty(funnodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("ps.funnode", funnodes));
        }
        return result.toString();
    }

}
