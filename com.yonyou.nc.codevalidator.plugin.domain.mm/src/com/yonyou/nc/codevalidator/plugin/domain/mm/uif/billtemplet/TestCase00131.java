package com.yonyou.nc.codevalidator.plugin.domain.mm.uif.billtemplet;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MMValueCheck;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MapList;
import com.yonyou.nc.codevalidator.plugin.domain.mm.util.MmTempletQueryUtil;
import com.yonyou.nc.codevalidator.plugin.domain.mm.vo.MmBillTempletBodyVO;
import com.yonyou.nc.codevalidator.plugin.domain.mm.vo.MmSystemplateVO;
import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractXmlRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ResourceRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.log.Logger;

/**
 * ����ģ���ϳ�����֯���֧�ֶ�汾���ֶ���ʾVID�ֶΣ�������OID�ֶ�
 * 
 * @author gaojf
 * 
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, description = "����ģ���ϳ�����֯���֧�ֶ�汾���ֶ���ʾVID�ֶΣ�������OID�ֶ�", relatedIssueId = "131", coder = "gaojf", solution = "����ģ���ϡ�����,���š��ֶ���ʾVID�ֶΣ�������OID�ֶ�")
public class TestCase00131 extends AbstractXmlRuleDefinition {

	@Override
	protected XmlResourceQuery getXmlResourceQuery(String[] functionNodes,
			IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		XmlResourceQuery xmlResQry = new XmlResourceQuery(functionNodes,
				ruleExecContext.getBusinessComponent());
		return xmlResQry;
	}

	@Override
	protected IRuleExecuteResult processScriptRules(
			IRuleExecuteContext ruleExecContext, List<XmlResource> resources)
			throws RuleBaseException {
		ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		// û��Ĭ�ϵ���ģ��Ľڵ�,���funnode
		List<String> noBillTempletNodes = new ArrayList<String>();

		// �е���ģ�壬������ģ��û���ֶΣ����ģ���ʶ��nodekey
		List<String> noItemBillTemplets = new ArrayList<String>();

		// �е���ģ�壬���ֶΣ����ǲ����Ϲ淶�����ϺͲ��ŷֱ���ģ���ʶ��nodekey
		MapList<String, String> noRelationItemsMap = new MapList<String, String>();
		if (MMValueCheck.isEmpty(resources)) {
			result.addResultElement(ruleExecContext.getBusinessComponent()
					.getDisplayBusiCompName(), "��������Ĺ��ܱ����Ƿ���ȷ��\n");
			return result;
		}

		for (XmlResource xmlResource : resources) {
			List<MmSystemplateVO> sysTemplateVOs = MmTempletQueryUtil
					.queryBillSystemplateVOList(xmlResource.getFuncNodeCode(),
							ruleExecContext);
			if (sysTemplateVOs.isEmpty()) {
				noBillTempletNodes.add(xmlResource.getFuncNodeCode());
				continue;
			}
			this.check(sysTemplateVOs, ruleExecContext, noItemBillTemplets,
					noRelationItemsMap);
			// ����ģ��û���ֶ�
			if (!noItemBillTemplets.isEmpty()) {
				result.addResultElement(ruleExecContext.getBusinessComponent()
						.getDisplayBusiCompName(), String.format(
						"���ܱ���Ϊ: %s,nodekeyΪ��%s�ĵ���ģ��û���ֶ�. \n",
						xmlResource.getFuncNodeCode(), noItemBillTemplets));
			}
			// ����ģ���ֶ�û�м��ع�����
			if (noRelationItemsMap.size() > 0) {
				List<String> temp = noRelationItemsMap.get("pk_source");
				if (null != temp && !temp.isEmpty()) {

					result.addResultElement(
							ruleExecContext.getBusinessComponent()
									.getDisplayBusiCompName(),
							String.format(
									"���ܱ���Ϊ: %s,��������Ϊ��%s�ĵ���ģ���У�����vid�ڿ�Ƭ���б�Ӧ����ʾ. \n",
									xmlResource.getFuncNodeCode(), temp));

				}
				temp = noRelationItemsMap.get("material_code");
				temp = new ArrayList<String>();
				if (null != temp && !temp.isEmpty()) {
					result.addResultElement(
							ruleExecContext.getBusinessComponent()
									.getDisplayBusiCompName(),
							String.format(
									"���ܱ���Ϊ: %s,��������Ϊ��%s�ĵ���ģ���У�����oid�ڿ�Ƭ���б���Ӧ����ʾ. \n",
									xmlResource.getFuncNodeCode(), temp));
				}
				temp = noRelationItemsMap.get("pk_dept");
				if (null != temp) {
					result.addResultElement(
							ruleExecContext.getBusinessComponent()
									.getDisplayBusiCompName(),
							String.format(
									"���ܱ���Ϊ: %s,��������Ϊ��%s�ĵ���ģ���У�����vid�ڿ�Ƭ���б�Ӧ����ʾ. \n",
									xmlResource.getFuncNodeCode(), temp));
				}
				temp = noRelationItemsMap.get("dept_code");
				if (null != temp) {
					result.addResultElement(
							ruleExecContext.getBusinessComponent()
									.getDisplayBusiCompName(),
							String.format(
									"���ܱ���Ϊ: %s,��������Ϊ��%s�ĵ���ģ���У�����oid�ڿ�Ƭ���б�Ӧ�ò���ʾ. \n",
									xmlResource.getFuncNodeCode(), temp));
				}
			}
		}
		// û�е���ģ��
		if (!noBillTempletNodes.isEmpty()) {
			result.addResultElement(ruleExecContext.getBusinessComponent()
					.getDisplayBusiCompName(), String.format(
					"���ܱ���Ϊ: %s �Ľڵ�û�е���ģ��. \n", noBillTempletNodes));
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
	private void check(List<MmSystemplateVO> sysTemplateVOs,
			IRuleExecuteContext ruleExecContext,
			List<String> noItemBillTemplets,
			MapList<String, String> noRelationItemsMap)
			throws RuleBaseException {

		for (MmSystemplateVO mmSystemplateVO : sysTemplateVOs) {
			DataSet dataSet = this.getSqlResults(
					mmSystemplateVO.getTemplateid(), ruleExecContext);

			if (dataSet == null) {
				noItemBillTemplets.add(mmSystemplateVO.getNodekey());
				continue;
			}
			this.checkItemShowFlag(dataSet, noRelationItemsMap);
		}
	}

	private void checkItemShowFlag(DataSet dataSet,
			MapList<String, String> noRelationItemsMap) {

		for (DataRow dataRow : dataSet.getRows()) {
			boolean showFlag;
			boolean listShowFlag;
			if (dataRow != null && dataRow.getValue("reftype") != null) {
				if (((String) dataRow.getValue("reftype"))
						.startsWith("���ϣ���汾��")) {
					showFlag = ((Integer) dataRow
							.getValue(MmBillTempletBodyVO.SHOWFLAG)) == 1;
					listShowFlag = ((String) dataRow
							.getValue(MmBillTempletBodyVO.LISTSHOWFLAG))
							.equals("Y");
					if (!showFlag || !listShowFlag) {
						noRelationItemsMap.put("pk_source", (String) dataRow
								.getValue("bill_templetcaption"));
					}
				}

				if (((String) dataRow.getValue("reftype"))
						.startsWith("����,code")
						|| ((String) dataRow.getValue("reftype")).equals("����")) {
					showFlag = ((Integer) dataRow
							.getValue(MmBillTempletBodyVO.SHOWFLAG)) == 1;
					listShowFlag = ((String) dataRow
							.getValue(MmBillTempletBodyVO.LISTSHOWFLAG))
							.equals("Y");
					if (!showFlag || !listShowFlag) {
						noRelationItemsMap.put("material_code",
								(String) dataRow
										.getValue("bill_templetcaption"));
					}
				}

				if (((String) dataRow.getValue("reftype")).startsWith("���Ű汾")) {
					showFlag = ((Integer) dataRow
							.getValue(MmBillTempletBodyVO.SHOWFLAG)) == 1;
					listShowFlag = ((String) dataRow
							.getValue(MmBillTempletBodyVO.LISTSHOWFLAG))
							.equals("Y");
					if (!showFlag || !listShowFlag) {
						noRelationItemsMap.put("pk_dept", (String) dataRow
								.getValue("bill_templetcaption"));
					}
				}

				if (((String) dataRow.getValue("reftype"))
						.startsWith("����,code")
						|| ((String) dataRow.getValue("reftype")).equals("����")) {
					showFlag = ((Integer) dataRow
							.getValue(MmBillTempletBodyVO.SHOWFLAG)) == 1;
					listShowFlag = ((String) dataRow
							.getValue(MmBillTempletBodyVO.LISTSHOWFLAG))
							.equals("Y");
					if (!showFlag || !listShowFlag) {
						noRelationItemsMap.put("pk_dept", (String) dataRow
								.getValue("bill_templetcaption"));
					}
				}
			}
		}
	}

	/**
	 * ����ϵͳģ���е�ģ��id��ѯ������ģ�����Ϳ�Ƭ�б��Ƿ���ʾ����
	 * 
	 * @param templateid
	 * @param ruleExecContext
	 * @throws RuleBaseException
	 */
	private DataSet getSqlResults(String templateid,
			IRuleExecuteContext ruleExecContext) {
		StringBuilder sql = new StringBuilder();
		sql.append("select pt.bill_templetcaption,pub.listshowflag,pub.showflag,pub.reftype from pub_billtemplet pt,pub_billtemplet_b pub where pt.pk_billtemplet= '");
		sql.append(templateid + "'");
		sql.append("and pub.pk_billtemplet = ");
		sql.append("'" + templateid + "'");
		DataSet dataSet = null;
		try {
			dataSet = SQLQueryExecuteUtils.executeQuery(sql.toString(),
					ruleExecContext.getRuntimeContext());
		} catch (RuleBaseException e) {
			Logger.error("����ִ���쳣" + e);

		}
		return dataSet;
	}
}
