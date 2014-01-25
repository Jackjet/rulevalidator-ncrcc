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
 * ����ģ���У�����λ��Ҫͨ�����ϣ���汾���ı༭������������������ϵļ��ع������д���cunitid=pk_measdoc
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, description = "����ģ���У�����λ��Ҫͨ�����ϣ���汾���ı༭������������������ϵļ��ع������д��ڡ�����λ�ֶ�����=pk_measdoc", relatedIssueId = "134", coder = "lijbe", solution = "����ģ���У�����λ��Ҫͨ�����ϣ���汾���ı༭������������������ϵļ��ع������д��ڡ�����λ�ֶ�����=pk_measdoc��������������λͳһ����Ϊcunitid��")
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
        // û��Ĭ�ϵ���ģ��Ľڵ�,���funnode
        List<String> noBillTempletNodes = new ArrayList<String>();

        // �е���ģ�壬������ģ��û���ֶΣ����ģ���ʶ��nodekey
        List<String> noItemBillTemplets = new ArrayList<String>();

        // �е���ģ�壬���ֶΣ����ǲ����Ϲ淶�����ģ���ʶ��nodekey
        Set<String> noRelationItems = new HashSet<String>();
        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "��������Ĺ��ܱ����Ƿ���ȷ��\n");
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
            // ����ģ��û���ֶ�
            if (!noItemBillTemplets.isEmpty()) {
                result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), String.format(
                        "���ܱ���Ϊ: %s,�ڵ��ʶnodekeyΪ��%s�ĵ���ģ��û���ֶ�. \n", xmlResource.getFuncNodeCode(), noItemBillTemplets));
            }
            // ����ģ���ֶ�û�м��ع�����
            if (!noRelationItems.isEmpty()) {
                result.addResultElement(
                        ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("���ܱ���Ϊ: %s,�ڵ��ʶnodekeyΪ��%s�ĵ���ģ���У�����λ��Ҫͨ�����ϣ���汾���ı༭�����������. \n",
                                xmlResource.getFuncNodeCode(), noRelationItems));
            }
        }
        // û�е���ģ��
        if (!noBillTempletNodes.isEmpty()) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("���ܱ���Ϊ: %s �Ľڵ�û�е���ģ��. \n", noBillTempletNodes));
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
            if (bodyVO.getReftype() != null && bodyVO.getReftype().startsWith("���ϣ���汾��")) {
                if (bodyVO.getMetadatarelation() == null || !bodyVO.getMetadatarelation().contains("=pk_measdoc")) {
                    noRelationItems.add(nodeKey);
                }
            }
            // if ("cunitid".equals(bodyVO.getItemkey())) {
            // if (bodyVO.getEditflag()) {
            // throw new BusinessException("nodekeyΪ[" + nodekey +
            // "]�ĵ���ģ�壬cunitid������λ��Ӧ�ò��ɱ༭");
            // }
            // }
        }
    }

}
