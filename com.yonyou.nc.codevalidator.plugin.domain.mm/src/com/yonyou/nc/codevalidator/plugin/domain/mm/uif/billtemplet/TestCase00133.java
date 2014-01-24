package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.billtemplet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MmTempletQueryUtil;
import com.yonyou.nc.codevalidator.plugin.domain.mm.vo.MmBillTempletBodyVO;
import com.yonyou.nc.codevalidator.plugin.domain.mm.vo.MmSystemplateVO;
import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 物料等支持多版本的档案，单据模板的VID字段中需要通过编辑关联项把OID带出来
 * 
 * @author gaojf
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, description = "物料等支持多版本的档案，单据模板的VID字段中需要通过编辑关联项把OID带出来", relatedIssueId = "133", coder = "gaojf", solution = "物料等支持多版本的档案，单据模板的VID字段中需要通过编辑关联项把OID带出来【vid字段（pk_org_v、cmaterialvid、cdeptvid）的属性：元数据编辑关联项。该属性的内容应该包含（pk_org=pk_factory、cmaterialid=pk_source、cdeptid=pk_dept）】")
public class TestCase00133 extends AbstractXmlRuleDefinition {

    @Override
    protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        XmlResourceQuery xmlResQry = new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
        return xmlResQry;
    }

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
            throws RuleBaseException {
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        // 没有默认单据模板的节点,存放funnode
        List<String> noBillTempletNodes = new ArrayList<String>();

        // 有单据模板，但单据模板没有字段，存放模板标识，nodekey
        List<String> noItemBillTemplets = new ArrayList<String>();

        // List<String> noRelationItems = new ArrayList<String>();
        // 有单据模板，有字段，但是不符合规范，物料和部门分别存放模板标识，nodekey
        MapList<String, String> noRelationItemsMap = new MapList<String, String>();
        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "请检查输入的功能编码是否正确！\n");
            return result;
        }
        for (XmlResource xmlResource : resources) {
            List<MmSystemplateVO> sysTemplateVOs =
                    MmTempletQueryUtil.queryBillSystemplateVOList(xmlResource.getFuncNodeCode(), ruleExecContext);
            if (null == sysTemplateVOs || sysTemplateVOs.isEmpty()) {
                noBillTempletNodes.add(xmlResource.getFuncNodeCode());
                continue;
            }
            this.check(sysTemplateVOs, ruleExecContext, noItemBillTemplets, noRelationItemsMap);

            // 单据模板没有字段
            if (!noItemBillTemplets.isEmpty()) {

                result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), String.format(
                        "功能编码为: %s,节点标识nodekey为：%s的单据模板没有字段. \n", xmlResource.getFuncNodeCode(), noItemBillTemplets));
            }
            // 单据模板字段没有加载关联项
            if (noRelationItemsMap.size() > 0) {
                List<String> temp = noRelationItemsMap.get("pk_source");
                if (null != temp && !temp.isEmpty()) {
                    result.addResultElement(
                            ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                            String.format("功能编码为: %s，节点标识nodekey为：%s的单据模板中，物料vid需要通过编辑关联项把OID带出来. \n",
                                    xmlResource.getFuncNodeCode(), this.ListToSet(temp)));
                }
                temp = noRelationItemsMap.get("pk_dept");
                if (null != temp && !temp.isEmpty()) {
                    result.addResultElement(
                            ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                            String.format("功能编码为: %s，节点标识nodekey为：%s的单据模板中，部门vid需要通过编辑关联项把OID带出来. \n",
                                    xmlResource.getFuncNodeCode(), this.ListToSet(temp)));
                }
            }
        }
        // 没有单据模板
        if (!noBillTempletNodes.isEmpty()) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("功能编码为: %s 的节点没有单据模板. \n", noBillTempletNodes));
        }

        return result;
    }

    /**
     * 通过单据模板ID查询单据模板表体信息，并判断是否符合规范
     * 
     * @param sysTemplateVOs
     * @param ruleExecContext
     * @param noItemBillTemplets
     *            没有字段的单据模板
     * @param noRelationItems
     *            没有通过物料关联主单位的单据模板
     * @throws RuleBaseException
     */
    private void check(List<MmSystemplateVO> sysTemplateVOs, IRuleExecuteContext ruleExecContext,
            List<String> noItemBillTemplets, MapList<String, String> noRelationItemsMap) throws RuleBaseException {

        for (MmSystemplateVO mmSystemplateVO : sysTemplateVOs) {
            List<MmBillTempletBodyVO> billTempletBodyVOs =
                    MmTempletQueryUtil.queryBillTempletBodyVOList(mmSystemplateVO.getTemplateid(), ruleExecContext);
            if (null == billTempletBodyVOs || billTempletBodyVOs.isEmpty()) {
                noItemBillTemplets.add(mmSystemplateVO.getNodekey());
                continue;
            }
            this.checkItem(billTempletBodyVOs, mmSystemplateVO.getNodekey(), noRelationItemsMap);
        }

    }

    private void checkItem(List<MmBillTempletBodyVO> billTempletBodyVOs, String nodeKey,
            MapList<String, String> noRelationItemsMap) {

        for (MmBillTempletBodyVO bodyVO : billTempletBodyVOs) {

            if (bodyVO.getReftype() != null && bodyVO.getReftype().startsWith("物料（多版本）")) {
                if (bodyVO.getMetadatarelation() == null || !bodyVO.getMetadatarelation().contains("=pk_source")) {

                    noRelationItemsMap.put("pk_source", nodeKey);
                }
            }
            if (bodyVO.getReftype() != null && bodyVO.getReftype().startsWith("部门版本")) {
                if (bodyVO.getMetadatarelation() == null || !bodyVO.getMetadatarelation().contains("=pk_dept")) {
                    noRelationItemsMap.put("pk_dept", nodeKey);
                }
            }
        }
    }

    private Set<String> ListToSet(List<String> temp) {
        Set<String> result = new HashSet<String>();
        for (String o : temp) {
            result.add(o);
        }
        return result;
    }
}
