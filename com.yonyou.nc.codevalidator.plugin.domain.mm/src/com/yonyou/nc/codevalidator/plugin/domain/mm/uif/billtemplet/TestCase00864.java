package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.billtemplet;

import java.util.ArrayList;
import java.util.List;

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
@RuleDefinition(catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, specialParamDefine = "主单位字段名", coder = "lijbe", solution = "单据模板中，主单位字段不可编辑【制造领域主单位统一命名为cunitid,其他领域通过参数来配置，如果没有配置，那默认为cunitid】", description = "单据模板中，主单位字段不可编辑【制造领域主单位统一命名为cunitid,其他领域通过参数来配置，如果没有配置，那默认为cunitid】", relatedIssueId = "864")
public class TestCase00864 extends AbstractXmlRuleDefinition {

    private static String PARAM_NAME = "主单位字段名";

    /**
     * 主单位字段名
     */
    private String cunitid = "cunitid";

    @Override
    protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {

        XmlResourceQuery xmlResQry = new XmlResourceQuery(functionNodes, ruleExecContext.getBusinessComponent());
        return xmlResQry;
    }

    @Override
    protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
            throws RuleBaseException {
        String defUnitid = ruleExecContext.getParameter(TestCase00864.PARAM_NAME);
        if (MMValueCheck.isNotEmpty(defUnitid)) {
            this.cunitid = defUnitid;
        }
        ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
        // 没有默认单据模板的节点,存放funnode
        List<String> noBillTempletNodes = new ArrayList<String>();

        // 有单据模板，但单据模板没有字段，存放模板标识，nodekey
        List<String> noItemBillTemplets = new ArrayList<String>();

        // 有单据模板，有字段，但是不符合规范，存放模板标识，nodekey
        List<String> canEditItems = new ArrayList<String>();
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
            this.check(sysTemplateVOs, ruleExecContext, noItemBillTemplets, canEditItems);
            // 单据模板没有字段
            if (!noItemBillTemplets.isEmpty()) {
                result.addResultElement(
                        ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("功能编码为:[" + xmlResource.getFuncNodeCode() + "]节点标识nodekey为："
                                + noItemBillTemplets.toString() + " 的单据模板没有字段. \n"));
            }
            // 主单位可以编辑的单据模板
            if (!canEditItems.isEmpty()) {
                result.addResultElement(
                        ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("功能编码为:[" + xmlResource.getFuncNodeCode() + "]节点标识nodekey为："
                                + canEditItems.toString() + "的单据模板中，主单位可编辑，应该设为不可编辑. \n"));
            }
        }
        // 没有单据模板
        if (!noBillTempletNodes.isEmpty()) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("功能编码为: " + noBillTempletNodes.toString() + " 的节点没有单据模板. \n"));
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
            List<String> noItemBillTemplets, List<String> noRelationItems) throws RuleBaseException {

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

    private void checkItem(List<MmBillTempletBodyVO> billTempletBodyVOs, String nodeKey, List<String> canEditItems) {

        for (MmBillTempletBodyVO bodyVO : billTempletBodyVOs) {
            if (this.cunitid.equals(bodyVO.getItemkey())) {
                if (bodyVO.getEditflag().booleanValue()) {
                    canEditItems.add(nodeKey);
                }
            }
        }
    }

}
