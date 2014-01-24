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
 * ����ģ���У�����λ��Ҫͨ�����ϣ���汾���ı༭������������������ϵļ��ع������д���cunitid=pk_measdoc
 * 
 * @author lijbe
 * @since V1.0
 * @version 1.0.0.0
 */
@RuleDefinition(catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, specialParamDefine = "����λ�ֶ���", coder = "lijbe", solution = "����ģ���У�����λ�ֶβ��ɱ༭��������������λͳһ����Ϊcunitid,��������ͨ�����������ã����û�����ã���Ĭ��Ϊcunitid��", description = "����ģ���У�����λ�ֶβ��ɱ༭��������������λͳһ����Ϊcunitid,��������ͨ�����������ã����û�����ã���Ĭ��Ϊcunitid��", relatedIssueId = "864")
public class TestCase00864 extends AbstractXmlRuleDefinition {

    private static String PARAM_NAME = "����λ�ֶ���";

    /**
     * ����λ�ֶ���
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
        // û��Ĭ�ϵ���ģ��Ľڵ�,���funnode
        List<String> noBillTempletNodes = new ArrayList<String>();

        // �е���ģ�壬������ģ��û���ֶΣ����ģ���ʶ��nodekey
        List<String> noItemBillTemplets = new ArrayList<String>();

        // �е���ģ�壬���ֶΣ����ǲ����Ϲ淶�����ģ���ʶ��nodekey
        List<String> canEditItems = new ArrayList<String>();
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
            this.check(sysTemplateVOs, ruleExecContext, noItemBillTemplets, canEditItems);
            // ����ģ��û���ֶ�
            if (!noItemBillTemplets.isEmpty()) {
                result.addResultElement(
                        ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("���ܱ���Ϊ:[" + xmlResource.getFuncNodeCode() + "]�ڵ��ʶnodekeyΪ��"
                                + noItemBillTemplets.toString() + " �ĵ���ģ��û���ֶ�. \n"));
            }
            // ����λ���Ա༭�ĵ���ģ��
            if (!canEditItems.isEmpty()) {
                result.addResultElement(
                        ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("���ܱ���Ϊ:[" + xmlResource.getFuncNodeCode() + "]�ڵ��ʶnodekeyΪ��"
                                + canEditItems.toString() + "�ĵ���ģ���У�����λ�ɱ༭��Ӧ����Ϊ���ɱ༭. \n"));
            }
        }
        // û�е���ģ��
        if (!noBillTempletNodes.isEmpty()) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("���ܱ���Ϊ: " + noBillTempletNodes.toString() + " �Ľڵ�û�е���ģ��. \n"));
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
