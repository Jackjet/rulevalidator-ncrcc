package com.yonyou.nc.codevalidator.plugin.domain.am.script;

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
import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

/**
 * @author xiepch
 * 
 */
@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "功能注册中帮助文件注册检查", relatedIssueId = "546",
		subCatalog = SubCatalogEnum.PS_CONTENTCHECK, coder = "xiepch", repairLevel = RepairLevel.SUGGESTREPAIR,
		executeLayer = ExecuteLayer.MODULE, executePeriod = ExecutePeriod.DEPLOY)
public class TestCase00546 extends AbstractScriptQueryRuleDefinition {

	public static final int LENGTH = ".html".length();

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		String moduleName = ruleExecContext.getBusinessComponent().getModule();
		String sqlHelp = "select funcode,fun_name,help_name from sm_funcregister " + "  where own_module "
				+ " in(select moduleid from dap_dapsystem where devmodule = '" + moduleName + "')";
		DataSet dataHelp = executeQuery(ruleExecContext, sqlHelp);
		StringBuilder noteString = new StringBuilder();
		if (!dataHelp.isEmpty()) {
			for (DataRow data : dataHelp.getRows()) {
				if (data.getValue("help_name") == null) {
					noteString.append("功能节点号: ").append(data.getValue("funcode") + "" + data.getValue("fun_name") + "没有注册帮助文档     \n");
				} else {
					String helpName = (String) data.getValue("help_name");
					if (!helpName.subSequence(0, helpName.length() - LENGTH).equals(data.getValue("funcode"))) {
						noteString.append(helpName + "与功能号：" + data.getValue("funcode") + "不匹配        \n");
					}
				}
			}
		}
		if (StringUtils.isNotBlank(noteString.toString())) {
			return new ErrorRuleExecuteResult(getIdentifier(), noteString.toString());
		}
		return new SuccessRuleExecuteResult(getIdentifier());
	}

}
