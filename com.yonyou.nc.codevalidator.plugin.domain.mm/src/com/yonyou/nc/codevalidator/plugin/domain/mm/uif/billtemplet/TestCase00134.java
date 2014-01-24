package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.billtemplet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
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
 * 单据模板中，主单位需要通过物料（多版本）的编辑关联项带出来，及物料的加载关联性中存在cunitid=pk_measdoc
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, description = "单据模板中，主单位需要通过物料（多版本）的编辑关联项带出来，及物料的加载关联性中存在【主单位字段名】=pk_measdoc", relatedIssueId = "134", coder = "lijbe", solution = "单据模板中，主单位需要通过物料（多版本）的编辑关联项带出来，及物料的加载关联性中存在【主单位字段名】=pk_measdoc【制造领域主单位统一命名为cunitid】")
public class TestCase00134 extends AbstractXmlRuleDefinition {

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

        // 有单据模板，有字段，但是不符合规范，存放模板标识，nodekey
        Set<String> noRelationItems = new HashSet<String>();
        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "请检查输入的功能编码是否正确！\n");
            return result;
        }
        for (XmlResource xmlResource : resources) {
            List<MmSystemplateVO> sysTemplateVOs =
                    MmTempletQueryUtil.queryBillSystemplateVOList(xmlResource.getFuncNodeCode(), ruleExecContext);
            if (sysTemplateVOs.isEmpty()) {
                noBillTempletNodes.add(xmlResource.getFuncNodeCode());
                continue;
            }
            this.check(sysTemplateVOs, ruleExecContext, noItemBillTemplets, noRelationItems);
            // 单据模板没有字段
            if (!noItemBillTemplets.isEmpty()) {
                result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), String.format(
                        "功能编码为: %s,节点标识nodekey为：%s的单据模板没有字段. \n", xmlResource.getFuncNodeCode(), noItemBillTemplets));
            }
            // 单据模板字段没有加载关联项
            if (!noRelationItems.isEmpty()) {
                result.addResultElement(
                        ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("功能编码为: %s,节点标识nodekey为：%s的单据模板中，主单位需要通过物料（多版本）的编辑关联项带出来. \n",
                                xmlResource.getFuncNodeCode(), noRelationItems));
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
            List<String> noItemBillTemplets, Set<String> noRelationItems) throws RuleBaseException {

        for (MmSystemplateVO mmSystemplateVO : sysTemplateVOs) {
            List<MmBillTempletBodyVO> billTempletBodyVOs =
                    MmTempletQueryUtil.queryBillTempletBodyVOList(mmSystemplateVO.getTemplateid(), ruleExecContext);
            if (null == billTempletBodyVOs || billTempletBodyVOs.isEmpty()) {
                noItemBillTemplets.add(mmSystemplateVO.getNodekey());
                continue;
            }
            this.checkItem(billTempletBodyVOs, mmSystemplateVO.getNodekey(), noRelationItems);
        }
    }

    private void checkItem(List<MmBillTempletBodyVO> billTempletBodyVOs, String nodeKey, Set<String> noRelationItems) {

        for (MmBillTempletBodyVO bodyVO : billTempletBodyVOs) {
            if (bodyVO.getReftype() != null && bodyVO.getReftype().startsWith("物料（多版本）")) {
                if (bodyVO.getMetadatarelation() == null || !bodyVO.getMetadatarelation().contains("=pk_measdoc")) {
                    noRelationItems.add(nodeKey);
                }
            }
            // if ("cunitid".equals(bodyVO.getItemkey())) {
            // if (bodyVO.getEditflag()) {
            // throw new BusinessException("nodekey为[" + nodekey +
            // "]的单据模板，cunitid（主单位）应该不可编辑");
            // }
            // }
        }
    }

}
