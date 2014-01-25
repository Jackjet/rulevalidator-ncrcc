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
 * 脚本注册是否已经支持维护权限
 * (本case检查本单据的业务实体管理是否注册,以及是否注册了修改和删除操作。)
 * 
 * @since 6.0
 * @version 2013-9-23 下午6:54:10
 * @author zhongcha
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, catalog = CatalogEnum.PRESCRIPT, subCatalog = SubCatalogEnum.PS_CONTENTCHECK, description = "脚本注册是否已经支持维护权限.(本case检查本单据的业务实体管理是否注册,以及是否注册了修改和删除操作。)  ", specialParamDefine = {
    CommonRuleParams.FUNCNODEPARAM
            + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"
}, solution = "请在业务实体管理节点中添加该资源或者该操作  ", coder = "zhongcha", relatedIssueId = "325")
public class TestCase00325 extends AbstractScriptQueryRuleDefinition implements IGlobalRuleDefinition {
    private static final String DELETE = "删除";

    private static final String EDIT = "修改";

    /**
     * Map<功能节点编码, 主实体名称>，用于提示信息时，
     * 当方法getPriEntity2FunMap被调用时，该变量才被构造出来，
     * 在本TestCase运行过程中若发现某个功能节点对应的主实体已经注册了，则删除key该功能节点的数据
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
            Logger.warn("当前规则功能节点参数为空，将检查所有功能节点！");
        }
        // 将元数据主实体ID与功能节点编码对应上
        MapList<String, String> priEntity2FunMap = this.getPriEntity2FunMap(ruleExecContext, funnodes);
        if (MMValueCheck.isNotEmpty(funnodes)) {
            notDataFunnodes.remove(this.fun2PriEntityNm.keySet());
        }
        // MapList<功能节点编码, 资源名称>，没有删除操作的功能节点对应的资源名称
        MapList<String, String> notDelOptError = new MapList<String, String>();
        // MapList<功能节点编码, 资源名称>，没有修改操作的功能节点对应的资源名称
        MapList<String, String> notEditOptError = new MapList<String, String>();
        // MapList<资源id, 功能节点编码>
        MapList<String, String> resid2fun = new MapList<String, String>();
        // Map<资源id, 资源名称>
        Map<String, String> resid2resname = new HashMap<String, String>();
        if (MMValueCheck.isNotEmpty(priEntity2FunMap)) {
            DataSet resDs =
                    this.executeQuery(ruleExecContext,
                            this.getResSql(priEntity2FunMap.keySet().toArray(new String[] {})));
            for (DataRow dataRow : resDs.getRows()) {
                // 资源id
                String permissionResid = (String) dataRow.getValue("pk_permission_res");
                // 资源名称
                String resourcename = (String) dataRow.getValue("resourcename");
                // 主实体id
                String priEntityid = (String) dataRow.getValue("mdid");
                // 对应的功能节点
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
                // MapList<资源id, 操作名称>
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
                String error = "功能节点编码为[" + funnode + "]的单据对应的元数据主实体名为[" + entry.getValue() + "]未在业务实体管理中注册。\n";
                this.putErrorContxt(allErrorMap, funnode, error);
            }
        }
        if (MMValueCheck.isNotEmpty(notDelOptError)) {
            for (Entry<String, List<String>> entry : notDelOptError.entrySet()) {
                String funnode = entry.getKey();
                String error =
                        "功能节点编码为[" + funnode + "]所需的资源名称为" + entry.getValue() + "单据在业务实体管理中未注册[" + TestCase00325.DELETE
                                + "]操作。\n";
                this.putErrorContxt(allErrorMap, funnode, error);
            }
        }
        if (MMValueCheck.isNotEmpty(notEditOptError)) {
            for (Entry<String, List<String>> entry : notDelOptError.entrySet()) {
                String funnode = entry.getKey();
                String error =
                        "功能节点编码为[" + funnode + "]所需的资源名称为" + entry.getValue() + "单据在业务实体管理中未注册[" + TestCase00325.EDIT
                                + "]操作。\n";
                this.putErrorContxt(allErrorMap, funnode, error);
            }

        }
        if (MMValueCheck.isNotEmpty(allErrorMap)) {
            for (Entry<String, StringBuilder> entry : allErrorMap.entrySet()) {
                result.addResultElement(entry.getKey(), entry.getValue().toString());
            }
        }
        if (MMValueCheck.isNotEmpty(notDataFunnodes)) {
            result.addResultElement(MmMDi18nConstants.ERROR_CHECKDATA_NULL, "功能节点编码为" + notDataFunnodes
                    + "的节点不存在所需检查的数据。\n");
        }
        return result;
    }

    /**
     * 将错误信息整合，并根据功能节点将错误信息分类
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
     * 将元数据主实体ID与功能节点编码的对应上
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
            // 用于根据元数据namespace获取对应的元数据主实体的ID的SQL
            DataSet priEntityByNmspDs =
                    this.executeQuery(ruleExecuteContext,
                            this.getPriEntityByNmspSql(namespaceList.toArray(new String[] {})));
            // 用于根据元数据name获取对应的元数据主实体的ID的SQL
            DataSet priEntityByNmDs =
                    this.executeQuery(ruleExecuteContext, this.getPriEntityByNmSql(nameList.toArray(new String[] {})));

            // 获得二者的交集
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
        // Map<主实体id,相同主实体id的DataRow>
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
     * 用于根据功能节点编码获取元数据路径的sql
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
     * 用于根据元数据namespace获取对应的元数据主实体的ID的SQL
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
     * 用于根据元数据name获取对应的元数据主实体的ID的SQL
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
