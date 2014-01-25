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
 * ���Ԫ�����ֶ��Ƿ�������Ĺ���
 * 
 * @since 6.0
 * @version 2013-8-31 ����4:45:35
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_METADATA, description = "���Ԫ�����ֶ������Ƿ�������", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "��Ӹ�Ԫ�����ֶ����ƶ������Ӧmd_property��resid��Ϊ�� ", coder = "zhongcha", relatedIssueId = "305")
public class TestCase00305 extends AbstractLangRuleDefinition implements IGlobalRuleDefinition {

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
        MapList<String, String> mdClzID2Funs = this.getMDClzID2Funs(ruleExecContext, funnodes);
        if (MMValueCheck.isNotEmpty(mdClzID2Funs)) {
            // ����Ԫ����ʵ��id��ø��ֶε�resid��displayname
            DataSet ds =
                    this.executeQuery(ruleExecContext, this.getSql(mdClzID2Funs.keySet().toArray(new String[] {})));
            // residΪ�յ���ʾ��Ϣ
            MapList<String, String> notInDbClzid2Fun = new MapList<String, String>();
            Map<String, String> notInDbClzid2name = new HashMap<String, String>();
            MapList<String, String> notInDbClzid2colNm = new MapList<String, String>();
            // �����ļ���Ϊ�յ���ʾ��Ϣ
            MapList<String, String> notInFileClzid2Fun = new MapList<String, String>();
            Map<String, String> notInFileClzid2name = new HashMap<String, String>();
            MapList<String, String> notInFileClzid2colNm = new MapList<String, String>();
            if (MMValueCheck.isNotEmpty(ds)) {
                for (DataRow dataRow : ds.getRows()) {
                    // ���Ԫ����ʵ������
                    String entityName = (String) dataRow.getValue("1_displayname");
                    // ����ֶε�����
                    String colName = (String) dataRow.getValue(MmMDi18nConstants.DISPLAY_NAME);
                    String classid = (String) dataRow.getValue("classid");
                    if (MMValueCheck.isNotEmpty(notDataFunnodes)) {
                        notDataFunnodes.removeAll(mdClzID2Funs.get(classid));
                    }
                    if (MMValueCheck.isEmpty(dataRow.getValue(MmMDi18nConstants.RESID))) {
                        notInDbClzid2Fun.putAll(classid, mdClzID2Funs.get(classid));
                        notInDbClzid2name.put(classid, entityName);
                        notInDbClzid2colNm.put(classid, colName);
                    }
                    else if (QueryLangRuleValidatorUtil.notInLRV((String) dataRow.getValue(MmMDi18nConstants.RESID),
                            ruleExecContext.getRuntimeContext())) {
                        notInFileClzid2Fun.putAll(classid, mdClzID2Funs.get(classid));
                        notInFileClzid2name.put(classid, entityName);
                        notInFileClzid2colNm.put(classid, colName);
                    }
                }
            }

            Map<String, MapList<String, String>> notInDbErrorMap =
                    this.createErrorMap(notInDbClzid2Fun, notInDbClzid2name, notInDbClzid2colNm);
            Map<String, MapList<String, String>> notInFileErrorMap =
                    this.createErrorMap(notInFileClzid2Fun, notInFileClzid2name, notInFileClzid2colNm);
            if (MMValueCheck.isNotEmpty(notInDbErrorMap)) {
                for (Entry<String, MapList<String, String>> outEntry : notInDbErrorMap.entrySet()) {
                    StringBuilder errorContxt1 = new StringBuilder();
                    for (Entry<String, List<String>> inEntry : outEntry.getValue().entrySet()) {
                        errorContxt1.append("����Ϊ[" + inEntry.getKey() + "]��ʵ��������Ϊ" + inEntry.getValue() + "���ֶ�δ�����\n");
                    }
                    result.addResultElement(outEntry.getKey() + MmMDi18nConstants.ERROR_DATABASE_NULL,
                            errorContxt1.toString());
                }
            }

            if (MMValueCheck.isNotEmpty(notInFileErrorMap)) {
                for (Entry<String, MapList<String, String>> outEntry : notInFileErrorMap.entrySet()) {
                    StringBuilder errorContxt2 = new StringBuilder();
                    for (Entry<String, List<String>> inEntry : outEntry.getValue().entrySet()) {
                        errorContxt2.append("����Ϊ[" + inEntry.getKey() + "]��ʵ��������Ϊ" + inEntry.getValue()
                                + "���ֶ�������ģ���Ӧ�Ķ���jar��lang\\simpchn\\�ļ�����Ԫ���ݶ����ļ��в�������Щ�ֶεĶ��\n");
                    }
                    result.addResultElement(outEntry.getKey() + MmMDi18nConstants.ERROR_LANGFILE_NULL,
                            errorContxt2.toString());
                }
            }
        }
        if (MMValueCheck.isNotEmpty(notDataFunnodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "���ܽڵ����Ϊ" + notDataFunnodes
                    + "�Ľڵ㲻��������������ݡ�\n");
        }
        return result;
    }

    /**
     * ��������Ϣ�����Map<���ܽڵ����, MapList<Ԫ����ʵ������, �ֶ�>>
     * 
     * @param clzid2Fun
     * @param clzid2name
     * @param clzid2colNm
     * @return
     */
    private Map<String, MapList<String, String>> createErrorMap(MapList<String, String> clzid2Fun,
            Map<String, String> clzid2name, MapList<String, String> clzid2colNm) {
        Map<String, MapList<String, String>> errorMap = new HashMap<String, MapList<String, String>>();
        for (Entry<String, List<String>> entry : clzid2Fun.entrySet()) {
            MapList<String, String> clz2cols = new MapList<String, String>();
            clz2cols.putAll(clzid2name.get(entry.getKey()), clzid2colNm.get(entry.getKey()));
            for (String funnode : entry.getValue()) {
                errorMap.put(funnode, clz2cols);
            }
        }
        return errorMap;
    }

    /**
     * ��ȡ�������й��ܽڵ���MapList<Ԫ����ʵ��id,���ܽڵ����>
     * 
     * @param ruleExecuteContext
     * @param funnodes
     * @return
     * @throws RuleBaseException
     */
    private MapList<String, String> getMDClzID2Funs(IRuleExecuteContext ruleExecuteContext, String[] funnodes)
            throws RuleBaseException {
        // MapList<Ԫ����ʵ��id,���ܽڵ����>
        MapList<String, String> mdClzID2Funs = new MapList<String, String>();
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
            DataSet componentDs = this.executeQuery(ruleExecuteContext, this.getMDClzIDsSql(mdclz[0], mdclz[1]));
            for (DataRow dataRow : componentDs.getRows()) {
                mdClzID2Funs.put((String) dataRow.getValue("id"), entry.getKey());
            }
        }
        return mdClzID2Funs;
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
     * ����Ԫ���ݵ�·����ȡԪ����ʵ��ID��SQL
     * 
     * @param funcode
     * @return
     */
    private String getMDClzIDsSql(String namespace, String name) {
        return "select mc.id from md_class mc,md_component mt where  isnull(mc.dr,0)=0 and isnull(mt.dr,0)=0 and mc.isprimary='Y' and mc.componentid=mt.id and  mt.namespace='"
                + namespace + "' and mt.name='" + name + "'";
    }

    /**
     * ����Ԫ����ʵ��id��ø��ֶε�resid��displayname��sql���
     * 
     * @param classid
     * @return
     */
    private String getSql(String[] classids) {
        StringBuilder result = new StringBuilder();
        result.append("select distinct mp.resid,mp.displayname,mc.displayname,mp.classid from md_property mp,md_class mc where  mc.id=mp.classid  and isnull(mp.dr,0)=0 and isnull(mc.dr,0)=0");
        result.append(" and ");
        result.append(SQLQueryExecuteUtils.buildSqlForIn("mp.classid", classids));
        return result.toString();
    }

}
