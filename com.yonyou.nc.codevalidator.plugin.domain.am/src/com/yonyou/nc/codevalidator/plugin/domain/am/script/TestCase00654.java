package com.yonyou.nc.codevalidator.plugin.domain.am.script;

import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.DefaultFormatStringScriptExportStrategy;
import com.yonyou.nc.codevalidator.resparser.executeresult.ScriptRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ����ѯģ���layer�ֶ��Ƿ�Ϊˮƽ��Ʒ
 * 
 * @author ZG
 */
@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "����ѯģ���layer�ֶ��Ƿ�Ϊˮƽ��Ʒ", relatedIssueId = "654",
		subCatalog = SubCatalogEnum.PS_CONTENTCHECK, coder = "zhangguang", executeLayer = ExecuteLayer.MODULE,
		executePeriod = ExecutePeriod.DEPLOY, repairLevel = RepairLevel.SUGGESTREPAIR)
public class TestCase00654 extends AbstractScriptQueryRuleDefinition {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		BusinessComponent bussinessComponent = ruleExecContext.getBusinessComponent();
		String module = bussinessComponent.getModule();
		// ��ѯģ����
		String ownModule = module.toUpperCase();
		String queryFunCodeSql = " select MODEL_NAME,MODEL_CODE from pub_query_templet where MODEL_CODE  in (select funcode from sm_funcregister left join dap_dapsystem on own_module = moduleid where systypecode = '"
				+ ownModule + "') and LAYER <> 0 ";
		DataSet queryds = SQLQueryExecuteUtils.executeQuery(queryFunCodeSql, ruleExecContext.getRuntimeContext());
		ScriptRuleExecuteResult result = new ScriptRuleExecuteResult();
		result.setDataSet(queryds);
		result.setScriptExportStrategy(new DefaultFormatStringScriptExportStrategy(
				"��ѯģ��Layer�ֶβ�Ϊ0�ļ�¼�У���ѯģ��ģ���Ϊ��%s �Ĳ�ѯģ�棡����ѯģ����Ϊ��%s)", "MODEL_CODE", "MODEL_NAME"));
		return result;
	}

}
