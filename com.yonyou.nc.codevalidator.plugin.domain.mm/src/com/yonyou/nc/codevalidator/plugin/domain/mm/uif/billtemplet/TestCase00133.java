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
 * ���ϵ�֧�ֶ�汾�ĵ���������ģ���VID�ֶ�����Ҫͨ���༭�������OID������
 * 
 * @author gaojf
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, description = "���ϵ�֧�ֶ�汾�ĵ���������ģ���VID�ֶ�����Ҫͨ���༭�������OID������", relatedIssueId = "133", coder = "gaojf", solution = "���ϵ�֧�ֶ�汾�ĵ���������ģ���VID�ֶ�����Ҫͨ���༭�������OID��������vid�ֶΣ�pk_org_v��cmaterialvid��cdeptvid�������ԣ�Ԫ���ݱ༭����������Ե�����Ӧ�ð�����pk_org=pk_factory��cmaterialid=pk_source��cdeptid=pk_dept����")
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
        // û��Ĭ�ϵ���ģ��Ľڵ�,���funnode
        List<String> noBillTempletNodes = new ArrayList<String>();

        // �е���ģ�壬������ģ��û���ֶΣ����ģ���ʶ��nodekey
        List<String> noItemBillTemplets = new ArrayList<String>();

        // List<String> noRelationItems = new ArrayList<String>();
        // �е���ģ�壬���ֶΣ����ǲ����Ϲ淶�����ϺͲ��ŷֱ���ģ���ʶ��nodekey
        MapList<String, String> noRelationItemsMap = new MapList<String, String>();
        if (MMValueCheck.isEmpty(resources)) {
            result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    "��������Ĺ��ܱ����Ƿ���ȷ��\n");
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

            // ����ģ��û���ֶ�
            if (!noItemBillTemplets.isEmpty()) {

                result.addResultElement(ruleExecContext.getBusinessComponent().getDisplayBusiCompName(), String.format(
                        "���ܱ���Ϊ: %s,�ڵ��ʶnodekeyΪ��%s�ĵ���ģ��û���ֶ�. \n", xmlResource.getFuncNodeCode(), noItemBillTemplets));
            }
            // ����ģ���ֶ�û�м��ع�����
            if (noRelationItemsMap.size() > 0) {
                List<String> temp = noRelationItemsMap.get("pk_source");
                if (null != temp && !temp.isEmpty()) {
                    result.addResultElement(
                            ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                            String.format("���ܱ���Ϊ: %s���ڵ��ʶnodekeyΪ��%s�ĵ���ģ���У�����vid��Ҫͨ���༭�������OID������. \n",
                                    xmlResource.getFuncNodeCode(), this.ListToSet(temp)));
                }
                temp = noRelationItemsMap.get("pk_dept");
                if (null != temp && !temp.isEmpty()) {
                    result.addResultElement(
                            ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                            String.format("���ܱ���Ϊ: %s���ڵ��ʶnodekeyΪ��%s�ĵ���ģ���У�����vid��Ҫͨ���༭�������OID������. \n",
                                    xmlResource.getFuncNodeCode(), this.ListToSet(temp)));
                }
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

            if (bodyVO.getReftype() != null && bodyVO.getReftype().startsWith("���ϣ���汾��")) {
                if (bodyVO.getMetadatarelation() == null || !bodyVO.getMetadatarelation().contains("=pk_source")) {

                    noRelationItemsMap.put("pk_source", nodeKey);
                }
            }
            if (bodyVO.getReftype() != null && bodyVO.getReftype().startsWith("���Ű汾")) {
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
