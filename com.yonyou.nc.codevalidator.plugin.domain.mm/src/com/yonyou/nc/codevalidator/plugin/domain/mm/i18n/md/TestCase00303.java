package com.yonyou.nc.codevalidator.plugin.domain.mm.i18n.md;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
 * ���Ԫ���������Ƿ�������
 * 
 * @since 6.0
 * @version 2013-8-31 ����4:40:09
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_METADATA, description = "���Ԫ���������Ƿ�������", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "��Ӹ�Ԫ�������ƶ������Ӧmd_component��resid��Ϊ��", coder = "zhongcha", relatedIssueId = "303")
public class TestCase00303 extends AbstractLangRuleDefinition implements IGlobalRuleDefinition {

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        String[] funnodes = ruleExecContext.getParameterArray(CommonRuleParams.FUNCNODEPARAM);
        List<String> notDataFunnodes = new LinkedList<String>();
        MapList<String, String> notInDbErrorMapList = new MapList<String, String>();
        MapList<String, String> notInFileErrorMapList = new MapList<String, String>();
        if (MMValueCheck.isNotEmpty(funnodes)) {
            notDataFunnodes.addAll(Arrays.asList(funnodes));
            // notDataFunnodes = new LinkedList<String>();
            DataSet mdclzs = this.executeQuery(ruleExecContext, this.getMDClassSql(funnodes));
            if (MMValueCheck.isNotEmpty(mdclzs)) {
                for (DataRow dataRow : mdclzs.getRows()) {
                    String funnode = (String) dataRow.getValue("funnode");
                    String metadataclass = (String) dataRow.getValue("metadataclass");
                    if (MMValueCheck.isEmpty(metadataclass)) {
                        continue;
                    }
                    String[] mdclz = metadataclass.split("\\.");
                    DataSet ds = this.executeQuery(ruleExecContext, this.getResidSql(mdclz[0], mdclz[1]));
                    for (DataRow dr : ds.getRows()) {
                        if (MMValueCheck.isNotEmpty(notDataFunnodes)) {
                            notDataFunnodes.remove(funnode);
                        }
                        String displayName = (String) dr.getValue(MmMDi18nConstants.DISPLAY_NAME);
                        if (MMValueCheck.isEmpty(dr.getValue(MmMDi18nConstants.RESID))) {
                            notInDbErrorMapList.put(funnode, displayName);
                        }
                        else if (QueryLangRuleValidatorUtil.notInLRV((String) dr.getValue(MmMDi18nConstants.RESID),
                                ruleExecContext.getRuntimeContext())) {
                            notInFileErrorMapList.put(funnode, displayName);
                        }
                    }
                }

            }

        }
        else {
            Logger.warn("��ǰ�����ܽڵ����Ϊ�գ���������й��ܽڵ㣡");
            // ���Ԫ���ݱ��е���������
            String sql = "select  distinct resid,displayname,namespace,name  from md_component";
            DataSet ds = this.executeQuery(ruleExecContext, sql);
            // MapList<���ݿ���residΪ�յ�Ԫ���ݶ�Ӧ��·����Ԫ���ݵ�����>
            MapList<String, String> notInDbMdclz2disNm = new MapList<String, String>();
            // MapList<�����ļ���Ԫ���ݶ����Ԫ���ݶ�Ӧ��·����Ԫ���ݵ�����>
            MapList<String, String> notInFileMdclz2disNm = new MapList<String, String>();
            for (DataRow dataRow : ds.getRows()) {
                String displayName = (String) dataRow.getValue(MmMDi18nConstants.DISPLAY_NAME);
                String namespace = (String) dataRow.getValue("namespace");
                String name = (String) dataRow.getValue("name");
                if (MMValueCheck.isEmpty(dataRow.getValue(MmMDi18nConstants.RESID))) {
                    if (MMValueCheck.isEmpty(namespace)) {
                        notInDbMdclz2disNm.put(name, displayName);
                    }
                    else {
                        notInDbMdclz2disNm.put(namespace + "." + name, displayName);
                    }
                }
                else if (QueryLangRuleValidatorUtil.notInLRV((String) dataRow.getValue(MmMDi18nConstants.RESID),
                        ruleExecContext.getRuntimeContext())) {
                    if (MMValueCheck.isEmpty(namespace)) {
                        notInFileMdclz2disNm.put(name, displayName);
                    }
                    else {
                        notInFileMdclz2disNm.put(namespace + "." + name, displayName);
                    }
                }
            }
            // ���ݿ�û��resid�������Ҳ�����Ԫ���ݶ�Ӧ�Ĺ��ܽڵ��Ԫ����·��
            List<String> dbNotFunMdclz = new ArrayList<String>();
            dbNotFunMdclz.addAll(notInDbMdclz2disNm.keySet());
            // �����ļ��в����ڣ������Ҳ�����Ԫ���ݶ�Ӧ�Ĺ��ܽڵ��Ԫ����·��
            List<String> fileNotFunMdclz = new ArrayList<String>();
            fileNotFunMdclz.addAll(notInFileMdclz2disNm.keySet());
            if (MMValueCheck.isNotEmpty(notInDbMdclz2disNm)) {
                DataSet notInDbFunDs =
                        this.executeQuery(ruleExecContext,
                                this.getFunnodeSql(notInDbMdclz2disNm.keySet().toArray(new String[] {})));

                for (DataRow dataRow : notInDbFunDs.getRows()) {
                    String funnode = (String) dataRow.getValue("funnode");
                    String metadataclass = (String) dataRow.getValue("metadataclass");
                    notInDbErrorMapList.putAll(funnode, notInDbMdclz2disNm.get(metadataclass));
                    // ȥ���ҵ����ܽڵ��
                    dbNotFunMdclz.remove(metadataclass);
                }
            }
            if (MMValueCheck.isNotEmpty(notInFileMdclz2disNm)) {
                DataSet notInFileFunDs =
                        this.executeQuery(ruleExecContext,
                                this.getFunnodeSql(notInFileMdclz2disNm.keySet().toArray(new String[] {})));

                for (DataRow dataRow : notInFileFunDs.getRows()) {
                    String funnode = (String) dataRow.getValue("funnode");
                    String metadataclass = (String) dataRow.getValue("metadataclass");
                    notInFileErrorMapList.putAll(funnode, notInFileMdclz2disNm.get(metadataclass));
                    // ȥ���ҵ����ܽڵ��
                    fileNotFunMdclz.remove(metadataclass);
                }
            }
            if (MMValueCheck.isNotEmpty(dbNotFunMdclz)) {
                result.addResultElement(MmMDi18nConstants.ERROR_DATABASE_NULL, "Ԫ����·��Ϊ" + dbNotFunMdclz + "��Ԫ����δ�����\n");
            }
            if (MMValueCheck.isNotEmpty(fileNotFunMdclz)) {
                result.addResultElement(MmMDi18nConstants.ERROR_LANGFILE_NULL, "Ԫ����·��Ϊ" + fileNotFunMdclz
                        + "��Ԫ��������ģ���Ӧ�Ķ���jar��lang\\simpchn\\�ļ�����Ԫ���ݶ����ļ��в����ڸ�Ԫ�������ƵĶ��\n");
            }
        }
        if (MMValueCheck.isNotEmpty(notInDbErrorMapList)) {
            for (Map.Entry<String, List<String>> entry : notInDbErrorMapList.entrySet()) {
                result.addResultElement(entry.getKey() + MmMDi18nConstants.ERROR_DATABASE_NULL,
                        "����Ϊ" + entry.getValue() + "��Ԫ����δ�����\n");
            }
        }
        if (MMValueCheck.isNotEmpty(notInFileErrorMapList)) {
            for (Map.Entry<String, List<String>> entry : notInFileErrorMapList.entrySet()) {
                result.addResultElement(entry.getKey() + MmMDi18nConstants.ERROR_LANGFILE_NULL,
                        "����Ϊ" + entry.getValue() + "��Ԫ��������ģ���Ӧ�Ķ���jar��lang\\simpchn\\�ļ�����Ԫ���ݶ����ļ��в����ڸ�Ԫ�������ƵĶ��\n");
            }
        }
        if (MMValueCheck.isNotEmpty(notDataFunnodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "���ܽڵ����Ϊ" + notDataFunnodes
                    + "�Ľڵ㲻��������������ݡ�\n");
        }
        return result;
    }

    /**
     * ����Ԫ���ݵ�·����ȡԪ���ݵ�RESID��SQL
     * 
     * @param mdID
     * @return
     */
    private String getResidSql(String namespace, String name) {
        return "select  distinct resid,displayname  from md_component  where  isnull(dr,0)=0  and namespace='"
                + namespace + "' and name='" + name + "'";
    }

    /**
     * ���ڸ��ݹ��ܽڵ�����ȡԪ����·����sql
     * 
     * @param funcode
     * @return
     */
    private String getMDClassSql(String[] funnodes) {
        StringBuilder result = new StringBuilder();
        result.append("select distinct pt.metadataclass,pb.funnode from pub_billtemplet pt,pub_systemplate_base pb where pt.pk_billtemplet=pb.templateid  and  pb.tempstyle='0' and isnull(pt.dr,0)=0 and isnull(pb.dr,0)=0");
        if (!MMValueCheck.isEmpty(funnodes)) {
            result.append(" and ");
            result.append(SQLQueryExecuteUtils.buildSqlForIn("pb.funnode", funnodes));
        }
        return result.toString();
    }

    /**
     * ���ڸ���Ԫ����·����ȡ���ܽڵ�����sql
     * 
     * @param funcode
     * @return
     */
    private String getFunnodeSql(String[] mdClzs) {
        StringBuilder result = new StringBuilder();
        result.append("select distinct pt.metadataclass,pb.funnode from pub_billtemplet pt,pub_systemplate_base pb where pt.pk_billtemplet=pb.templateid  and  pb.tempstyle='0' and isnull(pt.dr,0)=0 and isnull(pb.dr,0)=0");
        result.append(" and ");
        result.append(SQLQueryExecuteUtils.buildSqlForIn("pt.metadataclass", mdClzs));
        return result.toString();
    }
}
