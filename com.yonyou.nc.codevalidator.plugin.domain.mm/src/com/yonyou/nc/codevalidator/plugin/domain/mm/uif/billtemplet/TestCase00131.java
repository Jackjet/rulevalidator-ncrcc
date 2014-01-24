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
 * 单据模板上除主组织外的支持多版本的字段显示VID字段，而不是OID字段
 * 
 * @author gaojf
 * 
 */
@RuleDefinition(executePeriod = ExecutePeriod.DEPLOY, catalog = CatalogEnum.UIFACTORY, subCatalog = SubCatalogEnum.UF_MODELAPIORTEMPLATE, description = "单据模板上除主组织外的支持多版本的字段显示VID字段，而不是OID字段", relatedIssueId = "131", coder = "gaojf", solution = "单据模板上【物料,部门】字段显示VID字段，而不是OID字段")
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
		// 没有默认单据模板的节点,存放funnode
		List<String> noBillTempletNodes = new ArrayList<String>();

		// 有单据模板，但单据模板没有字段，存放模板标识，nodekey
		List<String> noItemBillTemplets = new ArrayList<String>();

		// 有单据模板，有字段，但是不符合规范，物料和部门分别存放模板标识，nodekey
		MapList<String, String> noRelationItemsMap = new MapList<String, String>();
		if (MMValueCheck.isEmpty(resources)) {
			result.addResultElement(ruleExecContext.getBusinessComponent()
					.getDisplayBusiCompName(), "请检查输入的功能编码是否正确！\n");
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
			// 单据模板没有字段
			if (!noItemBillTemplets.isEmpty()) {
				result.addResultElement(ruleExecContext.getBusinessComponent()
						.getDisplayBusiCompName(), String.format(
						"功能编码为: %s,nodekey为：%s的单据模板没有字段. \n",
						xmlResource.getFuncNodeCode(), noItemBillTemplets));
			}
			// 单据模板字段没有加载关联项
			if (noRelationItemsMap.size() > 0) {
				List<String> temp = noRelationItemsMap.get("pk_source");
				if (null != temp && !temp.isEmpty()) {

					result.addResultElement(
							ruleExecContext.getBusinessComponent()
									.getDisplayBusiCompName(),
							String.format(
									"功能编码为: %s,单据名称为：%s的单据模板中，物料vid在卡片和列表都应该显示. \n",
									xmlResource.getFuncNodeCode(), temp));

				}
				temp = noRelationItemsMap.get("material_code");
				temp = new ArrayList<String>();
				if (null != temp && !temp.isEmpty()) {
					result.addResultElement(
							ruleExecContext.getBusinessComponent()
									.getDisplayBusiCompName(),
							String.format(
									"功能编码为: %s,单据名称为：%s的单据模板中，物料oid在卡片和列表都不应该显示. \n",
									xmlResource.getFuncNodeCode(), temp));
				}
				temp = noRelationItemsMap.get("pk_dept");
				if (null != temp) {
					result.addResultElement(
							ruleExecContext.getBusinessComponent()
									.getDisplayBusiCompName(),
							String.format(
									"功能编码为: %s,单据名称为：%s的单据模板中，部门vid在卡片和列表都应该显示. \n",
									xmlResource.getFuncNodeCode(), temp));
				}
				temp = noRelationItemsMap.get("dept_code");
				if (null != temp) {
					result.addResultElement(
							ruleExecContext.getBusinessComponent()
									.getDisplayBusiCompName(),
							String.format(
									"功能编码为: %s,单据名称为：%s的单据模板中，部门oid在卡片和列表都应该不显示. \n",
									xmlResource.getFuncNodeCode(), temp));
				}
			}
		}
		// 没有单据模板
		if (!noBillTempletNodes.isEmpty()) {
			result.addResultElement(ruleExecContext.getBusinessComponent()
					.getDisplayBusiCompName(), String.format(
					"功能编码为: %s 的节点没有单据模板. \n", noBillTempletNodes));
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
						.startsWith("物料（多版本）")) {
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
						.startsWith("物料,code")
						|| ((String) dataRow.getValue("reftype")).equals("物料")) {
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

				if (((String) dataRow.getValue("reftype")).startsWith("部门版本")) {
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
						.startsWith("部门,code")
						|| ((String) dataRow.getValue("reftype")).equals("部门")) {
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
	 * 根据系统模板中的模板id查询出单据模板名和卡片列表是否显示属性
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
			Logger.error("规则执行异常" + e);

		}
		return dataSet;
	}
}
