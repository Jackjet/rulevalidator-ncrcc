package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.billtemplet;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MmTempletQueryUtil;
import com.yonyou.nc.codevalidator.plugin.domain.mm.vo.MmSystemplateVO;
import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IGlobalRuleDefinition;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ����ģ���ͷ����β��Ϣ�������ϢҪ����UE�淶
 * 
 * @author gaojf
 */
@RuleDefinition(executeLayer = ExecuteLayer.GLOBAL, executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, description = "����ģ���ͷ����β��Ϣ�������ϢҪ����UE�淶", relatedIssueId = "128", coder = "gaojf", solution = "������б�βҳǩ�����������βtail��Ϣ�ֶΣ��Ƶ��ˡ��Ƶ����ڡ������ˡ��������ڣ������ҳǩ����������audit��Ϣ�ֶΣ������ˡ�����ʱ�䡢����޸��ˡ�����޸�ʱ�䣩��")
public class TestCase00128 extends AbstractXmlRuleDefinition implements IGlobalRuleDefinition {

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
            return result;
        }
        for (XmlResource xmlResource : resources) {
            List<MmSystemplateVO> sysTemplateVOs =
                    MmTempletQueryUtil.queryBillSystemplateVOList(xmlResource.getFuncNodeCode(), ruleExecContext);
            if (sysTemplateVOs.isEmpty()) {
                noBillTempletNodes.add(xmlResource.getFuncNodeCode());
                continue;
            }
            this.check(sysTemplateVOs, ruleExecContext, noItemBillTemplets, noRelationItemsMap);
            if (noRelationItemsMap.size() > 0) {
                this.returnResults(result, ruleExecContext, xmlResource, noRelationItemsMap);
                result.addResultElement(
                        ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                        String.format("���������ܶ࣬���鵥��ģ�����Ƿ񽫱�βҳǩ������Ϊ��audit�����ҳǩ��Ϊ��tail����βӦΪtail�����ӦΪaudit����",
                                xmlResource.getFuncNodeCode()));

            }
            // ���mapList��ֵ���洢�¸�ģ��ļ�¼
            noRelationItemsMap.toMap().clear();
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
            // ��ѯ��ģ���Ӧ�ı�βҳǩ
            MapList<String, String> tableCodeMap =
                    this.queryTailMessage(mmSystemplateVO.getTemplateid(), ruleExecContext);
            // ��ñ�βҳǩ
            List<String> keyItemSet = new ArrayList<String>();
            keyItemSet = tableCodeMap.get("tail");
            // ���û�б�βҳǩ������
            if (null != keyItemSet && !keyItemSet.isEmpty()) {
                if (!keyItemSet.contains("billmaker")) {
                    noRelationItemsMap.put("billmaker", mmSystemplateVO.getNodekey());
                }
                if (!keyItemSet.contains("dmakedate")) {
                    noRelationItemsMap.put("dmakedate", mmSystemplateVO.getNodekey());
                }
                if (!keyItemSet.contains("approver")) {
                    noRelationItemsMap.put("approver", mmSystemplateVO.getNodekey());
                }
                if (!keyItemSet.contains("taudittime")) {
                    noRelationItemsMap.put("taudittime", mmSystemplateVO.getNodekey());
                }
            }
            // ������ҳǩ
            keyItemSet = tableCodeMap.get("audit");
            if (null != keyItemSet && !keyItemSet.isEmpty()) {
                if (!keyItemSet.contains("creator")) {
                    noRelationItemsMap.put("creator", mmSystemplateVO.getNodekey());
                }
                if (!keyItemSet.contains("creationtime")) {
                    noRelationItemsMap.put("creationtime", mmSystemplateVO.getNodekey());
                }
                if (!keyItemSet.contains("modifier")) {
                    noRelationItemsMap.put("modifier", mmSystemplateVO.getNodekey());
                }
                if (!keyItemSet.contains("modifiedtime")) {
                    noRelationItemsMap.put("modifiedtime", mmSystemplateVO.getNodekey());
                }
            }
        }
    }

    /**
     * ���ݵ���ģ���ѯtail��auditҳǩ��ֵ������һ��MapList�У�keyֵΪtail��audit��ListΪItemkey�е�ֵ��
     * 
     * @param templateid
     * @return
     * @throws RuleBaseException
     * @throws BusinessException
     */
    private MapList<String, String> queryTailMessage(String templateid, IRuleExecuteContext ruleExecContext)
            throws RuleBaseException {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.itemkey,b.table_code from pub_billtemplet_b b where b.pk_billtemplet='");
        sql.append(templateid);
        sql.append("' and (b.table_code='tail' or b.table_code='audit') and b.dr is not null and b.dr=0");

        DataSet dataSet = null;
        dataSet = SQLQueryExecuteUtils.executeQuery(sql.toString(), ruleExecContext.getRuntimeContext());

        if (MMValueCheck.isEmpty(dataSet)) {
            return null;
        }

        MapList<String, String> tableCodeMap = new MapList<String, String>();
        for (DataRow dataRow : dataSet.getRows()) {
            tableCodeMap.put((String) dataRow.getValue("table_code"), (String) dataRow.getValue("itemkey"));
        }
        return tableCodeMap;
    }

    /**
     * ����ʾ��Ϣװ��result�����
     * 
     * @param result
     * @param ruleExecContext
     * @param xmlResource
     * @param noRelationItemsMap
     */
    private void returnResults(ResourceRuleExecuteResult result, IRuleExecuteContext ruleExecContext,
            XmlResource xmlResource, MapList<String, String> noRelationItemsMap) {
        List<String> noRelationItems = new ArrayList<String>();
        noRelationItems = noRelationItemsMap.get("billmaker");
        if (null != noRelationItems && !noRelationItems.isEmpty()) {
            result.addResultElement(
                    ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("���ܱ���Ϊ: %s,�ڵ��ʶnodekeyΪ��%s�ĵ���ģ��ı�βҳǩ�У�û���Ƶ���billmaker. \n",
                            xmlResource.getFuncNodeCode(), noRelationItems));
        }
        noRelationItems = noRelationItemsMap.get("dmakedate");
        if (null != noRelationItems && !noRelationItems.isEmpty()) {
            result.addResultElement(
                    ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("���ܱ���Ϊ: %s,�ڵ��ʶnodekeyΪ��%s�ĵ���ģ��ı�βҳǩ�У�û���Ƶ�����dmakedate. \n",
                            xmlResource.getFuncNodeCode(), noRelationItems));
        }
        noRelationItems = noRelationItemsMap.get("approver");
        if (null != noRelationItems && !noRelationItems.isEmpty()) {
            result.addResultElement(
                    ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("���ܱ���Ϊ: %s,�ڵ��ʶnodekeyΪ��%s�ĵ���ģ��ı�βҳǩ�У�û��������approver. \n",
                            xmlResource.getFuncNodeCode(), noRelationItems));
        }
        noRelationItems = noRelationItemsMap.get("taudittime");
        if (null != noRelationItems && !noRelationItems.isEmpty()) {
            result.addResultElement(
                    ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("���ܱ���Ϊ: %s,�ڵ��ʶnodekeyΪ��%s�ĵ���ģ��ı�βҳǩ�У�û����������taudittime. \n",
                            xmlResource.getFuncNodeCode(), noRelationItems));
        }
        noRelationItems = noRelationItemsMap.get("creator");
        if (null != noRelationItems && !noRelationItems.isEmpty()) {
            result.addResultElement(
                    ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("���ܱ���Ϊ: %s,�ڵ��ʶnodekeyΪ��%s�ĵ���ģ������ҳǩ�У�û�д�����creator. \n",
                            xmlResource.getFuncNodeCode(), noRelationItems));
        }
        noRelationItems = noRelationItemsMap.get("creationtime");
        if (null != noRelationItems && !noRelationItems.isEmpty()) {
            result.addResultElement(
                    ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("���ܱ���Ϊ: %s,�ڵ��ʶnodekeyΪ��%s�ĵ���ģ������ҳǩ�У�û�д���ʱ��creationtime. \n",
                            xmlResource.getFuncNodeCode(), noRelationItems));
        }
        noRelationItems = noRelationItemsMap.get("modifier");
        if (null != noRelationItems && !noRelationItems.isEmpty()) {
            result.addResultElement(
                    ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("���ܱ���Ϊ: %s,�ڵ��ʶnodekeyΪ��%s�ĵ���ģ������ҳǩ�У�û������޸���modifier. \n",
                            xmlResource.getFuncNodeCode(), noRelationItems));
        }
        noRelationItems = noRelationItemsMap.get("modifiedtime");
        if (null != noRelationItems && !noRelationItems.isEmpty()) {
            result.addResultElement(
                    ruleExecContext.getBusinessComponent().getDisplayBusiCompName(),
                    String.format("���ܱ���Ϊ: %s,�ڵ��ʶnodekeyΪ��%s�ĵ���ģ������ҳǩ�У�û������޸�ʱ��modifiedtime. \n",
                            xmlResource.getFuncNodeCode(), noRelationItems));
        }
    }

}
