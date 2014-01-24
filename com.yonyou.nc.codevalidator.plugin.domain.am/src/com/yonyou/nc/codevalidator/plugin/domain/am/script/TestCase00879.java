package com.yonyou.nc.codevalidator.plugin.domain.am.script;

import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.ScriptResourceQuery;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractSingleScriptQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.DefaultFormatStringScriptExportStrategy;
import com.yonyou.nc.codevalidator.resparser.executeresult.IScriptExportStrategy;
import com.yonyou.nc.codevalidator.resparser.executeresult.ScriptRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.ScriptDataSetResource;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;

@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "����������У��������������Ԥ�ƹ����Ƿ����ظ��ģ�һ��ֻ��һ��Ĭ�ϵĹ��� ",
		relatedIssueId = "879", subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "zhangnane",
		executeLayer = ExecuteLayer.MODULE, executePeriod = ExecutePeriod.DEPLOY)
public class TestCase00879 extends AbstractSingleScriptQueryRuleDefinition {

	@Override
	protected final IRuleExecuteResult processScriptRules(ScriptDataSetResource scriptDataSetResource, IRuleExecuteContext ruleExecContext)
			throws ResourceParserException{
		ScriptRuleExecuteResult result = new ScriptRuleExecuteResult();
		DataSet dataSet = scriptDataSetResource.getDataSet();
		result.setDataSet(dataSet);
		result.setScriptExportStrategy(getScriptExportStrategy(ruleExecContext));
		return result;
	}

	private IScriptExportStrategy getScriptExportStrategy(IRuleExecuteContext ruleExecContext) {
		String moduleCode = ruleExecContext.getBusinessComponent().getModule();
		return new DefaultFormatStringScriptExportStrategy("����ģ�飺%s ��������������%s �������ͣ�s%\n", moduleCode, "pk_group","nbcrcode");
	}

	@Override
	protected ScriptResourceQuery getScriptResourceQuery(
			IRuleExecuteContext ruleExecContext) throws ResourceParserException {
		String moduleCode = ruleExecContext.getBusinessComponent().getModule();
		// ���ռ��ź͵������ͷ����ѯʱ�жϷ������и����Ƿ����1
		StringBuilder checkSql = new StringBuilder();
		checkSql.append("select pk_group, nbcrcode,num from ");
		checkSql.append("(select pk_group, nbcrcode,count(*) num from PUB_BCR_RULEBASE ");
		checkSql.append("left join pub_bcr_nbcr on pub_bcr_nbcr.code = PUB_BCR_RULEBASE.nbcrcode ");
		checkSql.append("where metaid in (");
		checkSql.append("select md_class.id from md_class left join md_component on md_class.componentid = md_component.id ");
		checkSql.append("where md_component.ownmodule = '" + moduleCode + "' and isprimary = 'Y')");
		checkSql.append("group by pk_group,nbcrcode )");
		checkSql.append("where num>1");
		return new ScriptResourceQuery (checkSql.toString());
	}

	
	
	
	
//	@Override
//	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
//		
//		//TODO : LIKE 878
//		// ������Сд��moduleCode
//		String moduleCode = ruleExecContext.getBusinessComponent().getModule();
//
//		DataSet dataset = SQLQueryExecuteUtils.executeQuery(getCheckSql(moduleCode),
//				ruleExecContext.getRuntimeContext());
//
//		StringBuilder noteBuilder = new StringBuilder();
//
//		if (!dataset.isEmpty()) {
//			noteBuilder.append("����Ĭ�ϱ�������ظ�:\n");
//			for (DataRow dr : dataset.getRows()) {
//				noteBuilder.append(String.format("����ģ�飺%s ��������������%s �������ͣ�s%\n", moduleCode, dr.getValue("pk_group"),
//						dr.getValue("nbcrcode")));
//			}
//		}
//
//		return noteBuilder.toString().equals("") ? new SuccessRuleExecuteResult(getIdentifier())
//				: new ErrorRuleExecuteResult(getIdentifier(), noteBuilder.toString());
//	}
//
//	private String getCheckSql(String module) {
//		// ���ռ��ź͵������ͷ����ѯʱ�жϷ������и����Ƿ����1
//		StringBuilder checkSql = new StringBuilder();
//		checkSql.append("select pk_group, nbcrcode,num from ");
//		checkSql.append("(select pk_group, nbcrcode,count(*) num from PUB_BCR_RULEBASE ");
//		checkSql.append("left join pub_bcr_nbcr on pub_bcr_nbcr.code = PUB_BCR_RULEBASE.nbcrcode ");
//		checkSql.append("where metaid in (");
//		checkSql.append("select md_class.id from md_class left join md_component on md_class.componentid = md_component.id ");
//		checkSql.append("where md_component.ownmodule = '" + module + "' and isprimary = 'Y')");
//		checkSql.append("group by pk_group,nbcrcode )");
//		checkSql.append("where num>1");
//		return checkSql.toString();
//
//	}

}
