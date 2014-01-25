package com.yonyou.nc.codevalidator.plugin.domain.platform.i18n;

import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractLangRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.DefaultFormatStringScriptExportStrategy;
import com.yonyou.nc.codevalidator.resparser.executeresult.ScriptRuleExecuteResult;
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
 * ���������Ƿ������ﴦ��
 * 
 */
@RuleDefinition(catalog = CatalogEnum.LANG, executeLayer = ExecuteLayer.GLOBAL, executePeriod = ExecutePeriod.DEPLOY,
		subCatalog = SubCatalogEnum.LANG_COMMON, description = "�����������Ƿ���ж��ﴦ��", specialParamDefine = {},
		solution = "", coder = "songhchb", relatedIssueId = "465", memo = "")
public class TestCase00465 extends AbstractLangRuleDefinition implements IGlobalRuleDefinition {

	@Override
	protected IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		// ResourceRuleExecuteResult result = new ResourceRuleExecuteResult();
		DataSet ds = this.executeQuery(ruleExecContext, this.getSql());
		ScriptRuleExecuteResult result = new ScriptRuleExecuteResult();
		result.setDataSet(ds);
		result.setScriptExportStrategy(new DefaultFormatStringScriptExportStrategy("�����飺%s δ�����ﴦ��!", "groupname"));
		// String allGroupName = "";
		// for (int i = 0; i < ds.getRowCount(); i++) {
		// String groupname = (String) ds.getValue(i, "groupname");
		// if (i == 0) {
		// allGroupName += groupname;
		// } else {
		// allGroupName += "," + groupname;
		// }
		// }
		//
		// if (allGroupName != null && !"".equals(allGroupName)) {
		// result.addResultElement("", "���²�����:" + allGroupName + " δ�����ﴦ��");
		// }
		return result;
	}

	// ��ѯδ���ж��ﴦ��Ĳ�����
	private String getSql() {
		return "select (select  systypename from DAP_DAPSYSTEM where DAP_DAPSYSTEM.MODULEID=pub_sysinittemp.domainflag) ,domainflag ,groupname  from pub_sysinittemp  where upper( 'Dgroupname'|| GROUPCODE)   not in (select upper(id) from mlt where path like 'lang/simpchn/sysinit/groupname%' )   and groupname is not null";
	}

}
