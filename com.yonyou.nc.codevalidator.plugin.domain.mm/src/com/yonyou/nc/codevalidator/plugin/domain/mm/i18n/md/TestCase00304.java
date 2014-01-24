package com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.md;

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
 * ���Ԫ����ʵ�������Ƿ����������
 * 
 * @since 6.0
 * @version 2013-8-31 ����4:44:24
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_METADATA, description = "���Ԫ����ʵ�������Ƿ�������", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "��Ӹ�Ԫ����ʵ�����ƶ���� ��Ӧmd_class��resid��Ϊ�� ", coder = "zhongcha", relatedIssueId = "304")
public class TestCase00304 extends AbstractLangRuleDefinition implements IGlobalRuleDefinition {

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
        MapList<String, String> mdId2Funnodes = this.getMdId2Funnodes(ruleExecContext, funnodes);
        // ���Ԫ����ʵ�����Ƶ�resid����Ϣ
        DataSet ds = this.executeQuery(ruleExecContext, this.getSql(mdId2Funnodes.keySet().toArray(new String[] {})));
        MapList<String, String> notInDbErrorMapList = new MapList<String, String>();
        MapList<String, String> notInFileErrorMapList = new MapList<String, String>();
        for (DataRow dataRow : ds.getRows()) {
            String entityName = (String) dataRow.getValue(MmMDi18nConstants.DISPLAY_NAME);
            String componentid = (String) dataRow.getValue("componentid");
            if (MMValueCheck.isNotEmpty(notDataFunnodes)) {
                notDataFunnodes.removeAll(mdId2Funnodes.get(componentid));
            }
            if (MMValueCheck.isEmpty(dataRow.getValue(MmMDi18nConstants.RESID))) {
                for (String funnode : mdId2Funnodes.get(componentid)) {
                    notInDbErrorMapList.put(funnode, entityName);
                }
            }
            else if (QueryLangRuleValidatorUtil.notInLRV((String) dataRow.getValue(MmMDi18nConstants.RESID),
                    ruleExecContext.getRuntimeContext())) {
                for (String funnode : mdId2Funnodes.get(componentid)) {
                    notInFileErrorMapList.put(funnode, entityName);
                }
            }
        }

        if (MMValueCheck.isNotEmpty(notInDbErrorMapList)) {
            for (Map.Entry<String, List<String>> entry : notInDbErrorMapList.entrySet()) {
                result.addResultElement(entry.getKey() + MmMDi18nConstants.ERROR_DATABASE_NULL,
                        "����Ϊ" + entry.getValue() + "��Ԫ����ʵ��δ�����\n");
            }
        }
        if (MMValueCheck.isNotEmpty(notInFileErrorMapList)) {
            for (Map.Entry<String, List<String>> entry : notInFileErrorMapList.entrySet()) {
                result.addResultElement(entry.getKey() + MmMDi18nConstants.ERROR_LANGFILE_NULL,
                        "����Ϊ" + entry.getValue() + "��Ԫ����ʵ������ģ���Ӧ�Ķ���jar��lang\\simpchn\\�ļ�����Ԫ���ݶ����ļ��в�����Ԫ����ʵ�����ƵĶ��");
            }
        }
        if (MMValueCheck.isNotEmpty(notDataFunnodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "���ܽڵ����Ϊ" + notDataFunnodes
                    + "�Ľڵ㲻��������������ݡ�\n");
        }
        return result;
    }

    private String getSql(String[] mdIDs) {
        StringBuilder result = new StringBuilder();
        result.append("select distinct d.resid,d.displayname,d.componentid from md_class d where ");
        result.append(SQLQueryExecuteUtils.buildSqlForIn("d.componentid", mdIDs));
        return result.toString();
    }

    /**
     * ��ȡ�������й��ܽڵ��Ӧ��Ԫ����ID����ȥ�أ�
     * 
     * @param ruleExecuteContext
     * @param funnodes
     * @return
     * @throws RuleBaseException
     */
    private MapList<String, String> getMdId2Funnodes(IRuleExecuteContext ruleExecuteContext, String[] funnodes)
            throws RuleBaseException {
        // MapList<Ԫ����id,���ܽڵ����>
        MapList<String, String> mdId2Funnodes = new MapList<String, String>();
        // MapList<���ܽڵ����,Ԫ����·��>
        Map<String, String> funnode2mdclz = new HashMap<String, String>();
        DataSet mdclzs = this.executeQuery(ruleExecuteContext, this.getMDClassSql(funnodes));
        for (DataRow dataRow : mdclzs.getRows()) {
            String funnode = (String) dataRow.getValue("funnode");
            String metadataclass = (String) dataRow.getValue("metadataclass");
            if (MMValueCheck.isNotEmpty(metadataclass)) {
                funnode2mdclz.put(funnode, metadataclass);
            }
        }
        for (Entry<String, String> entry : funnode2mdclz.entrySet()) {
            String[] mdclz = entry.getValue().split("\\.");
            DataSet componentDs = this.executeQuery(ruleExecuteContext, this.getMDIDsSql(mdclz[0], mdclz[1]));
            for (DataRow dataRow : componentDs.getRows()) {
                mdId2Funnodes.put((String) dataRow.getValue("id"), entry.getKey());
            }
        }
        return mdId2Funnodes;
    }

    /**
     * ���ڸ��ݹ��ܽڵ�����ȡԪ����·����sql
     * 
     * @param funcode
     * @return
     */
    private String getMDClassSql(String[] funnodes) {
        StringBuilder result = new StringBuilder();
        result.append("select distinct pt.metadataclass,pb.funnode from pub_billtemplet pt,pub_systemplate_base pb where pt.pk_billtemplet=pb.templateid  and  pb.tempstyle='0'  and isnull(pt.dr,0)=0 and isnull(pb.dr,0)=0");
        if (!MMValueCheck.isEmpty(funnodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("pb.funnode", funnodes));
        }
        return result.toString();
    }

    /**
     * ����Ԫ���ݵ�·����ȡԪ����ID��SQL
     * 
     * @param funcode
     * @return
     */
    private String getMDIDsSql(String namespace, String name) {
        return "select  id  from md_component  where   isnull(dr,0)=0  and namespace='" + namespace + "' and name='"
                + name + "'";
    }
}
