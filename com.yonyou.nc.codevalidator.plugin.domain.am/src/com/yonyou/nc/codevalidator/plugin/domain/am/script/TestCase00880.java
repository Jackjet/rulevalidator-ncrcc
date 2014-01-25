package com.yonyou.nc.codevalidator.plugin.domain.am.script;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ErrorRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.executeresult.SuccessRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "编码规则定义中已定义编码对象却未定义默认编码规则检查--集团级", relatedIssueId = "880",
		subCatalog = SubCatalogEnum.PS_CONTENTCHECK, coder = "xiepch", executeLayer = ExecuteLayer.MODULE,
		executePeriod = ExecutePeriod.DEPLOY)
public class TestCase00880 extends AbstractScriptQueryRuleDefinition {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		String moduleName = ruleExecContext.getBusinessComponent().getModule();

		// 查询出所有需要注册集团级编码规则的编码对象
		String sqlBcrGroup = "Select bn.Code,bb.billtypename From pub_bcr_nbcr bn ,Bd_Billtype bb Where "
				+ " bn.codescope='group' and bn.Code = bb.pk_billtypecode and bb.Istransaction = 'N'  And bb.Systemcode = '"
				+ moduleName.toUpperCase() + "'";
		// 查询出当前模块下所有的编码规则
		String sqlRuleBase = "select nbcrcode,pk_group from pub_bcr_rulebase where nbcrcode in ("
				+ " Select Pk_Billtypecode From Bd_Billtype Where Istransaction = 'N'  And Systemcode = '"
				+ moduleName.toUpperCase() + "')";
		// 当前环境所有集团
		String sqlGroup = " select pk_group,name from org_group where dr = 0 ";

		DataSet dataBcrGroup = executeQuery(ruleExecContext, sqlBcrGroup);

		DataSet dataRuleBase = executeQuery(ruleExecContext, sqlRuleBase);

		DataSet dataGroup = executeQuery(ruleExecContext, sqlGroup);
		StringBuffer noteString = new StringBuffer();
		if (!dataRuleBase.isEmpty() && !dataGroup.isEmpty()) {
			// key:单据号 value:集团列 默认编码规则信息
			Map<String, List<String>> mapRuleBase = new HashMap<String, List<String>>();
			// 所有的集团信息 key:主键 value:名称
			Map<String, String> orgGroups = new HashMap<String, String>();
			// 已注册编码对象的所有单据号 key:单据号 value：单据名称
			// 集团级：已注册编码对象的所有单据号 key:单据号 value：单据名称
			Map<String, String> bcrCodesGroup = new HashMap<String, String>();
			
			for (DataRow row : dataRuleBase.getRows()) {
				String code = row.getValue("nbcrcode").toString();
				String group = row.getValue("pk_group").toString();
				if (mapRuleBase.containsKey(code)) {
					mapRuleBase.get(code).add(group);
				} else {
					List<String> groups = new ArrayList<String>();
					groups.add(group);
					mapRuleBase.put(code, groups);
				}
			}
			// 所有已注册编码对象的单据号 集团级
			for (DataRow row : dataBcrGroup.getRows()) {
				bcrCodesGroup.put(row.getValue("code").toString(), row.getValue("billtypename").toString());
			}
	
			// 当前环境集团
			for (DataRow row : dataGroup.getRows()) {
				orgGroups.put(row.getValue("pk_group").toString(), row.getValue("name").toString());
			}
			orgGroups.put("presetdatagrouppk000", "预置数据");
			Iterator<String> iteratorCodesGroup = bcrCodesGroup.keySet().iterator();
			// 集团级：判断已注册的编码对象是否注册编码规则
			while (iteratorCodesGroup.hasNext()) {
				String code = iteratorCodesGroup.next();
				if (!mapRuleBase.containsKey(code)) {
					// 没有注册编码对象的code
					noteString.append("单据：" + code + bcrCodesGroup.get(code) + "在所有集团都没有注册默认编码规则         \n");
				} else {

					if (mapRuleBase.get(code).size() < orgGroups.size()) {
						noteString.append("单据：" + code + "-" + bcrCodesGroup.get(code) + ":在以下集团 ");
						Iterator<String> orgGroupsPk = orgGroups.keySet().iterator();
						while (orgGroupsPk.hasNext()) {
							String groupPk = orgGroupsPk.next();
							if (!mapRuleBase.get(code).contains(groupPk)) {
								// code已注册编码对象，但是在group集团没有注册编码规则
								noteString.append(groupPk + "-" + orgGroups.get(groupPk) + ";");
							}
						}

						noteString.append("没有注册默认编码规则           \n");
					}
				}
			}
		}

		if (noteString.length() > 0) {
			return new ErrorRuleExecuteResult(getIdentifier(), noteString.toString());
		}
		return new SuccessRuleExecuteResult(getIdentifier());
	}

}
