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

@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "编码规则定义中已定义编码对象却未定义默认编码规则检查--全局级", relatedIssueId = "888",
		subCatalog = SubCatalogEnum.PS_CONTENTCHECK, coder = "xiepch", executeLayer = ExecuteLayer.MODULE,
		executePeriod = ExecutePeriod.DEPLOY)
public class TestCase00888 extends AbstractScriptQueryRuleDefinition {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		String moduleName = ruleExecContext.getBusinessComponent().getModule();

		// 查询出所有需要注册全局级编码规则的编码对象
		String sqlBcrGlobal = "Select bn.Code,bb.billtypename From pub_bcr_nbcr bn ,Bd_Billtype bb Where "
				+ " bn.codescope='global' and bn.Code = bb.pk_billtypecode and bb.Istransaction = 'N'  And bb.Systemcode = '"
				+ moduleName.toUpperCase() + "'";
		// 查询出当前模块下所有的编码规则
		String sqlRuleBase = "select nbcrcode,pk_group from pub_bcr_rulebase where nbcrcode in ("
				+ " Select Pk_Billtypecode From Bd_Billtype Where Istransaction = 'N'  And Systemcode = '"
				+ moduleName.toUpperCase() + "')";

		DataSet dataBcrGlobal = executeQuery(ruleExecContext, sqlBcrGlobal);

		DataSet dataRuleBase = executeQuery(ruleExecContext, sqlRuleBase);

		StringBuffer noteString = new StringBuffer();
		if (!dataRuleBase.isEmpty()) {
			
			// key：单据号  value：集团集合（此处不用）
			Map<String, String> bcrCodesGroup = new HashMap<String, String>();
			
			// key:单据号 value:集团列 默认编码规则信息
			Map<String, List<String>> mapRuleBase = new HashMap<String, List<String>>();
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
			
			// 全局级：将查询的默认编码规则信息处理以单据号作为关键字，此单据号的所有集团作为value
			Map<String, String> bcrCodesGlobal = new HashMap<String, String>();
			
			// 全局级
			for (DataRow row : dataBcrGlobal.getRows()) {
				bcrCodesGlobal.put(row.getValue("code").toString(), row.getValue("billtypename").toString());
			}
			Iterator<String> iteratorCodesGlobal = bcrCodesGlobal.keySet().iterator();
			// 全局级
			while (iteratorCodesGlobal.hasNext()) {
				String code = iteratorCodesGlobal.next();
				if (!mapRuleBase.containsKey(code)) {
					noteString.append("单据：" + code + bcrCodesGroup.get(code) + "需注册全局级的默认编码规则         \n");
				}
			}
		}

		if (noteString.length() > 0) {
			return new ErrorRuleExecuteResult(getIdentifier(), noteString.toString());
		}
		return new SuccessRuleExecuteResult(getIdentifier());
	}

}
