package com.yonyou.nc.codevalidator.plugin.domain.mm.script.init;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.yonyou.nc.codevalidator.plugin.domain.mm.md.MmMDi18nConstants;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
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
 * �ű�ע���Ƿ��Ѿ�֧��ά��Ȩ��
 * (��case��鱾���ݵ�ҵ��ʵ������Ƿ�ע��,�Լ��Ƿ�ע�����޸ĺ�ɾ��������)
 * 
 * @since 6.0
 * @version 2013-9-23 ����6:54:10
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.PRESCRIPT, subCatalog = SubCatalogEnum.PS_CONTENTCHECK, description = "�ű�ע���Ƿ��Ѿ�֧��ά��Ȩ��.(��case��鱾���ݵ�ҵ��ʵ������Ƿ�ע��,�Լ��Ƿ�ע�����޸ĺ�ɾ��������)  ", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "����ҵ��ʵ�����ڵ�����Ӹ���Դ���߸ò���  ", coder = "zhongcha", relatedIssueId = "325")
public class TestCase00325 extends AbstractScriptQueryRuleDefinition implements IGlobalRuleDefinition {
    private static final String DELETE = "ɾ��";

    private static final String EDIT = "�޸�";

    /**
     * Map<���ܽڵ����, ��ʵ������>��������ʾ��Ϣʱ��
     * ������getPriEntity2FunMap������ʱ���ñ����ű����������
     * �ڱ�TestCase���й�����������ĳ�����ܽڵ��Ӧ����ʵ���Ѿ�ע���ˣ���ɾ��key�ù��ܽڵ������
     */
    private Map<String, String> fun2PriEntityNm = new HashMap<String, String>();

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
        // ��Ԫ������ʵ��ID�빦�ܽڵ�����Ӧ��
        MapList<String, String> priEntity2FunMap = this.getPriEntity2FunMap(ruleExecContext, funnodes);
        if (MMValueCheck.isNotEmpty(funnodes)) {
            notDataFunnodes.remove(this.fun2PriEntityNm.keySet());
        }
        // MapList<���ܽڵ����, ��Դ����>��û��ɾ�������Ĺ��ܽڵ��Ӧ����Դ����
        MapList<String, String> notDelOptError = new MapList<String, String>();
        // MapList<���ܽڵ����, ��Դ����>��û���޸Ĳ����Ĺ��ܽڵ��Ӧ����Դ����
        MapList<String, String> notEditOptError = new MapList<String, String>();
        // MapList<��Դid, ���ܽڵ����>
        MapList<String, String> resid2fun = new MapList<String, String>();
        // Map<��Դid, ��Դ����>
        Map<String, String> resid2resname = new HashMap<String, String>();
        if (MMValueCheck.isNotEmpty(priEntity2FunMap)) {
            DataSet resDs =
                    this.executeQuery(ruleExecContext,
                            this.getResSql(priEntity2FunMap.keySet().toArray(new String[] {})));
            for (DataRow dataRow : resDs.getRows()) {
                // ��Դid
                String permissionResid = (String) dataRow.getValue("pk_permission_res");
                // ��Դ����
                String resourcename = (String) dataRow.getValue("resourcename");
                // ��ʵ��id
                String priEntityid = (String) dataRow.getValue("mdid");
                // ��Ӧ�Ĺ��ܽڵ�
                List<String> funnodeList = priEntity2FunMap.get(priEntityid);
                for (String funnode : funnodeList) {
                    this.fun2PriEntityNm.remove(funnode);
                }
                resid2fun.putAll(permissionResid, funnodeList);
                resid2resname.put(permissionResid, resourcename);
            }

            if (MMValueCheck.isNotEmpty(resid2fun)) {
                DataSet operates =
                        this.executeQuery(ruleExecContext,
                                this.getOperateSql(resid2fun.keySet().toArray(new String[] {})));
                // MapList<��Դid, ��������>
                MapList<String, String> resid2operates = new MapList<String, String>();
                for (DataRow opDr : operates.getRows()) {
                    String operationname = (String) opDr.getValue("operationname");
                    String resid = (String) opDr.getValue("resourceid");
                    resid2operates.put(resid, operationname);
                }
                for (Entry<String, List<String>> entry : resid2operates.entrySet()) {
                    if (!entry.getValue().contains(TestCase00325.DELETE)) {
                        for (String funnode : resid2fun.get(entry.getKey())) {
                            notDelOptError.put(funnode, resid2resname.get(entry.getKey()));
                        }
                    }
                    if (!entry.getValue().contains(TestCase00325.EDIT)) {
                        for (String funnode : resid2fun.get(entry.getKey())) {
                            notEditOptError.put(funnode, resid2resname.get(entry.getKey()));
                        }
                    }
                }
            }
        }
        Map<String, StringBuilder> allErrorMap = new HashMap<String, StringBuilder>();

        if (MMValueCheck.isNotEmpty(this.fun2PriEntityNm)) {
            for (Entry<String, String> entry : this.fun2PriEntityNm.entrySet()) {
                String funnode = entry.getKey();
                String error = "���ܽڵ����Ϊ[" + funnode + "]�ĵ��ݶ�Ӧ��Ԫ������ʵ����Ϊ[" + entry.getValue() + "]δ��ҵ��ʵ�������ע�ᡣ\n";
                this.putErrorContxt(allErrorMap, funnode, error);
            }
        }
        if (MMValueCheck.isNotEmpty(notDelOptError)) {
            for (Entry<String, List<String>> entry : notDelOptError.entrySet()) {
                String funnode = entry.getKey();
                String error =
                        "���ܽڵ����Ϊ[" + funnode + "]�������Դ����Ϊ" + entry.getValue() + "������ҵ��ʵ�������δע��[" + TestCase00325.DELETE
                                + "]������\n";
                this.putErrorContxt(allErrorMap, funnode, error);
            }
        }
        if (MMValueCheck.isNotEmpty(notEditOptError)) {
            for (Entry<String, List<String>> entry : notDelOptError.entrySet()) {
                String funnode = entry.getKey();
                String error =
                        "���ܽڵ����Ϊ[" + funnode + "]�������Դ����Ϊ" + entry.getValue() + "������ҵ��ʵ�������δע��[" + TestCase00325.EDIT
                                + "]������\n";
                this.putErrorContxt(allErrorMap, funnode, error);
            }

        }
        if (MMValueCheck.isNotEmpty(allErrorMap)) {
            for (Entry<String, StringBuilder> entry : allErrorMap.entrySet()) {
                result.addResultElement(entry.getKey(), entry.getValue().toString());
            }
        }
        if (MMValueCheck.isNotEmpty(notDataFunnodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "���ܽڵ����Ϊ" + notDataFunnodes
                    + "�Ľڵ㲻��������������ݡ�\n");
        }
        return result;
    }

    /**
     * ��������Ϣ���ϣ������ݹ��ܽڵ㽫������Ϣ����
     * 
     * @param allErrorMap
     * @param funnode
     * @param error
     */
    private void putErrorContxt(Map<String, StringBuilder> allErrorMap, String funnode, String error) {
        if (allErrorMap.entrySet().contains(funnode)) {
            allErrorMap.get(funnode).append(error);
        }
        else {
            StringBuilder errorContxt = new StringBuilder();
            errorContxt.append(error);
            allErrorMap.put(funnode, errorContxt);
        }
    }

    private String getResSql(String[] priEntityIDs) {
        StringBuilder result = new StringBuilder();
        result.append("select sr.pk_permission_res,sr.resourcecode,sr.resourcename,sr.mdid from sm_permission_res sr  where  isnull(sr.dr,0)=0");
        result.append(" and ");
        result.append(SQLQueryExecuteUtils.buildSqlForIn("sr.mdid", priEntityIDs));
        return result.toString();
    }

    private String getOperateSql(String[] permissionResids) {
        StringBuilder result = new StringBuilder();
        result.append("select so.operationname,so.resourceid from sm_res_operation so  where  isnull(so.dr,0)=0");
        result.append(" and ");
        result.append(SQLQueryExecuteUtils.buildSqlForIn("so.resourceid", permissionResids));
        return result.toString();
    }

    /**
     * ��Ԫ������ʵ��ID�빦�ܽڵ����Ķ�Ӧ��
     * 
     * @param ruleExecuteContext
     * @param funnodes
     * @return
     * @throws RuleBaseException
     */
    private MapList<String, String> getPriEntity2FunMap(IRuleExecuteContext ruleExecuteContext, String[] funnodes)
            throws RuleBaseException {
        MapList<String, String> priEntity2FunMap = new MapList<String, String>();
        DataSet mdclzs = this.executeQuery(ruleExecuteContext, this.getMDClassSql(funnodes));
        List<String> namespaceList = new ArrayList<String>();
        List<String> nameList = new ArrayList<String>();
        MapList<String, String> metadata2fun = new MapList<String, String>();
        for (DataRow dataRow : mdclzs.getRows()) {
            String funnode = (String) dataRow.getValue("funnode");
            String metadataclass = (String) dataRow.getValue("metadataclass");
            if (MMValueCheck.isEmpty(metadataclass)) {
                continue;
            }
            String[] mdclz = metadataclass.split("\\.");
            namespaceList.add(mdclz[0]);
            nameList.add(mdclz[1]);
            metadata2fun.put(metadataclass, funnode);
        }
        if (MMValueCheck.isNotEmpty(namespaceList) && MMValueCheck.isNotEmpty(nameList)) {
            // ���ڸ���Ԫ����namespace��ȡ��Ӧ��Ԫ������ʵ���ID��SQL
            DataSet priEntityByNmspDs =
                    this.executeQuery(ruleExecuteContext,
                            this.getPriEntityByNmspSql(namespaceList.toArray(new String[] {})));
            // ���ڸ���Ԫ����name��ȡ��Ӧ��Ԫ������ʵ���ID��SQL
            DataSet priEntityByNmDs =
                    this.executeQuery(ruleExecuteContext, this.getPriEntityByNmSql(nameList.toArray(new String[] {})));

            // ��ö��ߵĽ���
            List<DataRow> dataRowList = this.getSameDataRow(priEntityByNmspDs, priEntityByNmDs);

            for (DataRow dr : dataRowList) {
                String metadataclass = (String) dr.getValue("namespace") + "." + (String) dr.getValue("name");
                String id = (String) dr.getValue("id");
                String displayname = (String) dr.getValue("displayname");
                if (MMValueCheck.isEmpty(metadata2fun.get(metadataclass))) {
                    continue;
                }
                for (String funnode : metadata2fun.get(metadataclass)) {
                    this.fun2PriEntityNm.put(funnode, displayname);
                    priEntity2FunMap.put(id, funnode);
                }
            }
        }
        return priEntity2FunMap;
    }

    private List<DataRow> getSameDataRow(DataSet priEntityByNmspDs, DataSet priEntityByNmDs) {
        List<DataRow> dataRowList = new ArrayList<DataRow>();
        // Map<��ʵ��id,��ͬ��ʵ��id��DataRow>
        Map<String, DataRow> priEntyId2DrByNmsp = new HashMap<String, DataRow>();
        List<String> priEntyIdByNm = new ArrayList<String>();
        for (DataRow priByNmspDr : priEntityByNmspDs.getRows()) {
            String id = (String) priByNmspDr.getValue("id");
            priEntyId2DrByNmsp.put(id, priByNmspDr);
        }
        for (DataRow priByNmDr : priEntityByNmDs.getRows()) {
            String id = (String) priByNmDr.getValue("id");
            priEntyIdByNm.add(id);
        }
        for (Entry<String, DataRow> entry : priEntyId2DrByNmsp.entrySet()) {
            if (priEntyIdByNm.contains(entry.getKey())) {
                dataRowList.add(entry.getValue());
            }
        }
        return dataRowList;
    }

    /**
     * ���ڸ��ݹ��ܽڵ�����ȡԪ����·����sql
     * 
     * @param funnodes
     * @return
     */
    private String getMDClassSql(String[] funnodes) {
        StringBuilder result = new StringBuilder();
        result.append("select distinct pt.metadataclass,pb.funnode from pub_billtemplet pt, pub_systemplate_base pb where pt.pk_billtemplet=pb.templateid and pb.tempstyle='0'  and isnull(pt.dr,0)=0  and isnull(pb.dr,0)=0");
        if (!MMValueCheck.isEmpty(funnodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("pb.funnode", funnodes));
        }
        return result.toString();
    }

    /**
     * ���ڸ���Ԫ����namespace��ȡ��Ӧ��Ԫ������ʵ���ID��SQL
     * 
     * @param funcode
     * @return
     */
    private String getPriEntityByNmspSql(String[] namespaces) {
        StringBuilder result = new StringBuilder();
        result.append("select mc.id,mc.displayname,mt.namespace,mt.name from md_class mc,md_component mt where mc.isprimary='Y' and mc.componentid=mt.id  and  isnull(mc.dr,0)=0  and  isnull(mt.dr,0)=0");
        result.append(" and ");
        result.append(SQLQueryExecuteUtils.buildSqlForIn("mt.namespace", namespaces));
        return result.toString();
    }

    /**
     * ���ڸ���Ԫ����name��ȡ��Ӧ��Ԫ������ʵ���ID��SQL
     * 
     * @param funcode
     * @return
     */
    private String getPriEntityByNmSql(String[] names) {
        StringBuilder result = new StringBuilder();
        result.append("select mc.id,mc.displayname,mt.namespace,mt.name from md_class mc,md_component mt where mc.isprimary='Y' and mc.componentid=mt.id  and  isnull(mc.dr,0)=0  and  isnull(mt.dr,0)=0  ");
        result.append(" and ");
        result.append(SQLQueryExecuteUtils.buildSqlForIn("mt.name", names));
        return result.toString();

    }
}
