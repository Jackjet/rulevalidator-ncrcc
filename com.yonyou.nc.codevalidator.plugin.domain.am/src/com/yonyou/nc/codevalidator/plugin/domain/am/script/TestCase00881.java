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

@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "��ѯ�������Ĭ��ҵ��Ԫ   �ڸ��Ի��������ú����ʹ��sql������Ƿ��и�ֵ",
		relatedIssueId = "881", subCatalog = SubCatalogEnum.PS_CONTENTCHECK, coder = "xiepch")
public class TestCase00881 extends AbstractScriptQueryRuleDefinition {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		String moduleName = ruleExecContext.getBusinessComponent().getModule();
		/*
		 * Select Model_Name From Pub_Query_Templet Where Node_Code In ( select
		 * funcode from sm_funcregister where own_module in(select moduleid from
		 * dap_dapsystem where devmodule = 'eom') ) and id not in ( Select Qt.Id
		 * From Pub_Query_Condition Qc ,Pub_Query_Templet Qt,Sm_Funcregister Sf
		 * Where Qc.Pk_Templet = Qt.Id And Sf.Funcode = Qt.Node_Code And
		 * Qc.Value ='#mainorg#' And Qt.Node_Code In (select funcode from
		 * sm_funcregister where own_module in(select moduleid from
		 * dap_dapsystem where devmodule = 'eom')) )
		 */
		// ʵ�ַ�ʽ�����ҳ����д���#mainorg#�Ĳ�ѯģ�壬ʣ�µľ���û��ע���
		StringBuffer sql = new StringBuffer();
		sql.append("Select Model_Name From Pub_Query_Templet Where Node_Code In (");
		sql.append("select funcode from sm_funcregister where own_module in(select moduleid from dap_dapsystem where devmodule = '"
				+ moduleName + "')");
		sql.append(") and id not in (");
		sql.append("Select Qt.Id ");
		sql.append(" From Pub_Query_Condition Qc ,Pub_Query_Templet Qt,Sm_Funcregister Sf ");
		sql.append(" Where Qc.Pk_Templet = Qt.Id And Sf.Funcode = Qt.Node_Code And Qc.Value ='#mainorg#' ");
		sql.append(" And Qt.Node_Code  In(");
		sql.append(" select funcode from sm_funcregister where own_module in(select moduleid from dap_dapsystem where devmodule = '"
				+ moduleName + "'))");
		sql.append(")");

		DataSet data = executeQuery(ruleExecContext, sql.toString());
		StringBuffer noteString = new StringBuffer();
		if (!data.isEmpty()) {
			noteString.append("���²�ѯģ���ڳ�ʼ��ʱ����֯û������Ĭ��ֵ#mainorg#��\n");
			for (DataRow row : data.getRows()) {
				noteString.append(row.getValue("model_Name") + "      \n");
			}
			return new ErrorRuleExecuteResult(getIdentifier(), noteString.toString());
		}
		return new SuccessRuleExecuteResult(getIdentifier());
	}

}
