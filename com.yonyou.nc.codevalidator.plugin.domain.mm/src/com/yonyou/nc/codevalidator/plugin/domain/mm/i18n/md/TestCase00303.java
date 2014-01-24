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
 * 检测元数据名称是否做多语
 * 
 * @since 6.0
 * @version 2013-8-31 下午4:40:09
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.LANG, subCatalog = SubCatalogEnum.LANG_METADATA, description = "检测元数据名称是否做多语", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "添加该元数据名称多语，即对应md_component中resid不为空", coder = "zhongcha", relatedIssueId = "303")
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
            Logger.warn("当前规则功能节点参数为空，将检查所有功能节点！");
            // 获得元数据表中的所有数据
            String sql = "select  distinct resid,displayname,namespace,name  from md_component";
            DataSet ds = this.executeQuery(ruleExecContext, sql);
            // MapList<数据库中resid为空的元数据对应的路径，元数据的名称>
            MapList<String, String> notInDbMdclz2disNm = new MapList<String, String>();
            // MapList<多语文件中元数据多语的元数据对应的路径，元数据的名称>
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
            // 数据库没有resid，且又找不到该元数据对应的功能节点的元数据路径
            List<String> dbNotFunMdclz = new ArrayList<String>();
            dbNotFunMdclz.addAll(notInDbMdclz2disNm.keySet());
            // 多语文件中不存在，且又找不到该元数据对应的功能节点的元数据路径
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
                    // 去除找到功能节点的
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
                    // 去除找到功能节点的
                    fileNotFunMdclz.remove(metadataclass);
                }
            }
            if (MMValueCheck.isNotEmpty(dbNotFunMdclz)) {
                result.addResultElement(MmMDi18nConstants.ERROR_DATABASE_NULL, "元数据路径为" + dbNotFunMdclz + "的元数据未做多语。\n");
            }
            if (MMValueCheck.isNotEmpty(fileNotFunMdclz)) {
                result.addResultElement(MmMDi18nConstants.ERROR_LANGFILE_NULL, "元数据路径为" + fileNotFunMdclz
                        + "的元数据所属模块对应的多语jar中lang\\simpchn\\文件夹下元数据多语文件中不存在该元数据名称的多语！\n");
            }
        }
        if (MMValueCheck.isNotEmpty(notInDbErrorMapList)) {
            for (Map.Entry<String, List<String>> entry : notInDbErrorMapList.entrySet()) {
                result.addResultElement(entry.getKey() + MmMDi18nConstants.ERROR_DATABASE_NULL,
                        "名称为" + entry.getValue() + "的元数据未做多语。\n");
            }
        }
        if (MMValueCheck.isNotEmpty(notInFileErrorMapList)) {
            for (Map.Entry<String, List<String>> entry : notInFileErrorMapList.entrySet()) {
                result.addResultElement(entry.getKey() + MmMDi18nConstants.ERROR_LANGFILE_NULL,
                        "名称为" + entry.getValue() + "的元数据所属模块对应的多语jar中lang\\simpchn\\文件夹下元数据多语文件中不存在该元数据名称的多语！\n");
            }
        }
        if (MMValueCheck.isNotEmpty(notDataFunnodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "功能节点编码为" + notDataFunnodes
                    + "的节点不存在所需检查的数据。\n");
        }
        return result;
    }

    /**
     * 根据元数据的路径获取元数据的RESID的SQL
     * 
     * @param mdID
     * @return
     */
    private String getResidSql(String namespace, String name) {
        return "select  distinct resid,displayname  from md_component  where  isnull(dr,0)=0  and namespace='"
                + namespace + "' and name='" + name + "'";
    }

    /**
     * 用于根据功能节点编码获取元数据路径的sql
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
     * 用于根据元数据路径获取功能节点编码的sql
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
