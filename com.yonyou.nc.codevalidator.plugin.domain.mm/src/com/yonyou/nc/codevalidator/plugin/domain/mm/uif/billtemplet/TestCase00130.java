package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.billtemplet;

import java.util.ArrayList;
import java.util.List;

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
 * 单据模板上主组织字段卡片下不显示，列表下要显示,pk_org卡片列表都不显示，pk_org_v列表显示
 * 
 * @author gaojf
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, description = "单据模板上主组织字段卡片下不显示，列表下要显示,pk_org卡片列表都不显示，pk_org_v列表显示", relatedIssueId = "130", coder = "gaojf", solution = "pk_org卡片列表都不显示，pk_org_v列表显示")
public class TestCase00130 extends AbstractXmlRuleDefinition {

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

        // 有单据模板，有字段，但是不符合规范，物料和部门分别存放模板标识，nodekey
        MapList<String, String> noRelationItemsMap = new MapList<String, String>();

        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "请检查输入的功能编码是否正确！\n");
        }
        for (XmlResource xmlResource : resources) {
            List<MmSystemplateVO> sysTemplateVOs =
                    MmTempletQueryUtil.queryBillSystemplateVOList(xmlResource.getFuncNodeCode(), ruleExecContext);
            if (sysTemplateVOs.isEmpty()) {
                noBillTempletNodes.add(xmlResource.getFuncNodeCode());
                continue;
            }
            this.check(sysTemplateVOs, ruleExecContext, noItemBillTemplets, noRelationItemsMap);
            // 单据模板没有字段
            if (!noItemBillTemplets.isEmpty()) {
                result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), String.format(
                        "功能编码为: %s,节点标识nodekey为：%s的单据模板没有字段. \n", xmlResource.getFuncNodeCode(), noItemBillTemplets));
            }
            if (noRelationItemsMap.size() > 0) {
                List<String> temp = new ArrayList<String>();
                temp = noRelationItemsMap.get("orgShow");
                if (null != temp && !temp.isEmpty()) {
                    result.addResultElement(
                            ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                            String.format("功能编码为: %s,节点标识nodekey为：%s的单据模板中，表头的pk_org在卡片和列表都不应该显示. \n",
                                    xmlResource.getFuncNodeCode(), temp));
                }
                temp = noRelationItemsMap.get("org_vShow");
                if (null != temp && !temp.isEmpty()) {
                    result.addResultElement(
                            ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                            String.format("功能编码为: %s,节点标识nodekey为：%s的单据模板中，表头的pk_org_v在卡片不应该显示. \n",
                                    xmlResource.getFuncNodeCode(), temp));
                }
                temp = noRelationItemsMap.get("org_vListShow");
                if (null != temp && !temp.isEmpty()) {
                    result.addResultElement(
                            ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                            String.format("功能编码为: %s,节点标识nodekey为：%s的单据模板中，表头的pk_org_v在列表应该显示. \n",
                                    xmlResource.getFuncNodeCode(), temp));
                }
            }
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
            if (bodyVO.getPos() == 0 && "pk_org".equals(bodyVO.getItemkey())) {
                if (bodyVO.getShowflag() || bodyVO.getListshowflag()) {
                    noRelationItemsMap.put("orgShow", nodeKey);
                }
            }
            if (bodyVO.getPos() == 0 && "pk_org_v".equals(bodyVO.getItemkey())) {
                if (bodyVO.getShowflag()) {
                    noRelationItemsMap.put("org_vShow", nodeKey);
                }
                if (!bodyVO.getListshowflag()) {
                    noRelationItemsMap.put("org_vListShow", nodeKey);
                }
            }
        }
    }

}
