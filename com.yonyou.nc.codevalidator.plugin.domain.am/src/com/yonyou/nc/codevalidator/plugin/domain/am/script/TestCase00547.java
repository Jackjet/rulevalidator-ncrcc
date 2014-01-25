package com.yonyou.nc.codevalidator.plugin.domain.am.script;

import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ErrorRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.executeresult.SuccessRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.utils.StringUtils;

/**
 * @author xiepch
 * 
 */
@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "Ԥ�ò�ѯ�������", relatedIssueId = "547",
		subCatalog = SubCatalogEnum.PS_CONTENTCHECK, coder = "xiepch")
public class TestCase00547 extends AbstractScriptQueryRuleDefinition {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		String moduleName = ruleExecContext.getBusinessComponent().getModule();
		// ����ģ������ѯ����ģ�������еĹ��ܺ�
		String sqlFuncode = "select billtypename,nodecode from bd_billtype " + "where  istransaction = 'N' and "
				+ "nodecode in(select funcode from sm_funcregister "
				+ " where own_module in(select moduleid from dap_dapsystem where devmodule = '" + moduleName + "')) ";
		DataSet dataFuncode = executeQuery(ruleExecContext, sqlFuncode);
		StringBuffer noteString = new StringBuffer("\n" + moduleName + "ģ����:      \n");
		if (!dataFuncode.isEmpty()) {
			int number = 1;
			for (DataRow data : dataFuncode.getRows()) {
				String sqlScheme = "select name from pub_queryscheme where funcode = '" + data.getValue("nodecode")
						+ "' " + " and cuserid='~' and pk_org='~' ";
				DataSet dataScheme = executeQuery(ruleExecContext, sqlScheme);
				if (dataScheme.isEmpty()) {
					noteString.append(number + "��  " + data.getValue("billtypename") + "û��Ԥ�ò�ѯ����,��Ԥ��     \n");
				} else {
					noteString.append(number + "��  " + data.getValue("billtypename") + "��Ԥ�ò�ѯ����:    \n");
					for (DataRow scheme : dataScheme.getRows()) {
						noteString.append("    " + scheme.getValue("name") + "    \n");
					}
					noteString.append(" ;��ȷ���Ƿ�����©\n");
				}
				number++;
			}
		} else {
			noteString.append("�޽ڵ�       \n");
		}
		if (StringUtils.isNotBlank(noteString.toString())) {
			return new ErrorRuleExecuteResult(getIdentifier(), noteString.toString());
		}
		return new SuccessRuleExecuteResult(getIdentifier());
	}

}
