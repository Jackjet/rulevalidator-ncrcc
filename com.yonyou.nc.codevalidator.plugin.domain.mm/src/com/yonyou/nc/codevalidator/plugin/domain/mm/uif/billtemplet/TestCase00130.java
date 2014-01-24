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
 * ����ģ��������֯�ֶο�Ƭ�²���ʾ���б���Ҫ��ʾ,pk_org��Ƭ�б�����ʾ��pk_org_v�б���ʾ
 * 
 * @author gaojf
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, description = "����ģ��������֯�ֶο�Ƭ�²���ʾ���б���Ҫ��ʾ,pk_org��Ƭ�б�����ʾ��pk_org_v�б���ʾ", relatedIssueId = "130", coder = "gaojf", solution = "pk_org��Ƭ�б�����ʾ��pk_org_v�б���ʾ")
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
        // û��Ĭ�ϵ���ģ��Ľڵ�,���funnode
        List<String> noBillTempletNodes = new ArrayList<String>();

        // �е���ģ�壬������ģ��û���ֶΣ����ģ���ʶ��nodekey
        List<String> noItemBillTemplets = new ArrayList<String>();

        // �е���ģ�壬���ֶΣ����ǲ����Ϲ淶�����ϺͲ��ŷֱ���ģ���ʶ��nodekey
        MapList<String, String> noRelationItemsMap = new MapList<String, String>();

        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "��������Ĺ��ܱ����Ƿ���ȷ��\n");
        }
        for (XmlResource xmlResource : resources) {
            List<MmSystemplateVO> sysTemplateVOs =
                    MmTempletQueryUtil.queryBillSystemplateVOList(xmlResource.getFuncNodeCode(), ruleExecContext);
            if (sysTemplateVOs.isEmpty()) {
                noBillTempletNodes.add(xmlResource.getFuncNodeCode());
                continue;
            }
            this.check(sysTemplateVOs, ruleExecContext, noItemBillTemplets, noRelationItemsMap);
            // ����ģ��û���ֶ�
            if (!noItemBillTemplets.isEmpty()) {
                result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), String.format(
                        "���ܱ���Ϊ: %s,�ڵ��ʶnodekeyΪ��%s�ĵ���ģ��û���ֶ�. \n", xmlResource.getFuncNodeCode(), noItemBillTemplets));
            }
            if (noRelationItemsMap.size() > 0) {
                List<String> temp = new ArrayList<String>();
                temp = noRelationItemsMap.get("orgShow");
                if (null != temp && !temp.isEmpty()) {
                    result.addResultElement(
                            ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                            String.format("���ܱ���Ϊ: %s,�ڵ��ʶnodekeyΪ��%s�ĵ���ģ���У���ͷ��pk_org�ڿ�Ƭ���б���Ӧ����ʾ. \n",
                                    xmlResource.getFuncNodeCode(), temp));
                }
                temp = noRelationItemsMap.get("org_vShow");
                if (null != temp && !temp.isEmpty()) {
                    result.addResultElement(
                            ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                            String.format("���ܱ���Ϊ: %s,�ڵ��ʶnodekeyΪ��%s�ĵ���ģ���У���ͷ��pk_org_v�ڿ�Ƭ��Ӧ����ʾ. \n",
                                    xmlResource.getFuncNodeCode(), temp));
                }
                temp = noRelationItemsMap.get("org_vListShow");
                if (null != temp && !temp.isEmpty()) {
                    result.addResultElement(
                            ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                            String.format("���ܱ���Ϊ: %s,�ڵ��ʶnodekeyΪ��%s�ĵ���ģ���У���ͷ��pk_org_v���б�Ӧ����ʾ. \n",
                                    xmlResource.getFuncNodeCode(), temp));
                }
            }
        }
        return result;
    }

    /**
     * ͨ������ģ��ID��ѯ����ģ�������Ϣ�����ж��Ƿ���Ϲ淶
     * 
     * @param sysTemplateVOs
     * @param ruleExecContext
     * @param noItemBillTemplets
     *            û���ֶεĵ���ģ��
     * @param noRelationItems
     *            û��ͨ�����Ϲ�������λ�ĵ���ģ��
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
