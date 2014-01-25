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
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ����ӡģ���layer�ֶ��Ƿ�Ϊˮƽ��Ʒ
 * 
 * @author ZG
 */
@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "����ӡģ���layer�ֶ��Ƿ�Ϊˮƽ��Ʒ", relatedIssueId = "656",
		subCatalog = SubCatalogEnum.PS_CONTENTCHECK, coder = "zhangguang", executeLayer = ExecuteLayer.MODULE,
		executePeriod = ExecutePeriod.DEPLOY)
public class TestCase00656 extends AbstractScriptQueryRuleDefinition {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		BusinessComponent bussinessComponent = ruleExecContext.getBusinessComponent();
		String module = bussinessComponent.getModule();
		String ownModule = module.toUpperCase();
		String printtempletsql = " select a.MODULEID,b.FUN_NAME from pub_systemplate_base a,SM_FUNCREGISTER b where a.FUNNODE = b.FUNCODE  and  a.MODULEID = (select moduleid from dap_dapsystem where systypecode = '"
				+ ownModule + "')  and LAYER <> 0 ";
		DataSet printds = SQLQueryExecuteUtils.executeQuery(printtempletsql, ruleExecContext.getRuntimeContext());
		ScriptRuleExecuteResult result = new ScriptRuleExecuteResult();
		result.setDataSet(printds);
		result.setScriptExportStrategy(new DefaultFormatStringScriptExportStrategy(
				"��ӡģ����Layer�ֶβ�Ϊ0�ļ�¼�У���ӡģ��ģ���Ϊ��%s �Ĳ�ѯģ��, �ҹ��ܽڵ���Ϊ��%s �Ĵ�ӡģ��", "MODULEID", "FUN_NAME"));
		return result;
	}

}
