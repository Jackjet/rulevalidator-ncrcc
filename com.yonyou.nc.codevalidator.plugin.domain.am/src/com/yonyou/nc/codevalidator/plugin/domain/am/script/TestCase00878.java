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
import com.yonyou.nc.codevalidator.rule.annotation.ScopeEnum;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;

@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "列表下组织要显示在第一列", relatedIssueId = "878",
		subCatalog = SubCatalogEnum.PS_CONTENTCHECK, coder = "zhangnane", scope = ScopeEnum.AM,
		executeLayer = ExecuteLayer.MODULE, executePeriod = ExecutePeriod.DEPLOY)
public class TestCase00878 extends AbstractSingleScriptQueryRuleDefinition {

	@Override
	protected final IRuleExecuteResult processScriptRules(ScriptDataSetResource scriptDataSetResource, IRuleExecuteContext ruleExecContext)
			throws ResourceParserException{
		ScriptRuleExecuteResult result = new ScriptRuleExecuteResult();
		DataSet dataSet = scriptDataSetResource.getDataSet();
		result.setDataSet(dataSet);
		result.setScriptExportStrategy(getScriptExportStrategy());
		return result;
	}

	private IScriptExportStrategy getScriptExportStrategy() {
		return new DefaultFormatStringScriptExportStrategy("单据模板：%s 模板主键：%s 列表状态下第一列显示为：%s不是主组织请检查"
				,"templetname","pk_billtemplet","metadataproperty");
	}

	@Override
	protected ScriptResourceQuery getScriptResourceQuery(
			IRuleExecuteContext ruleExecContext) throws ResourceParserException {
		String moduleCode = ruleExecContext.getBusinessComponent().getModule().toUpperCase();
		StringBuilder checkSql = new StringBuilder();
		
				checkSql.append("select pub_billtemplet.bill_templetcaption templetname,pub_billtemplet_b.pk_billtemplet pk_billtemplet,pub_billtemplet_b.listshowflag listshowflag, ordernum,pub_billtemplet_b.metadataproperty ");
		
				checkSql.append("from pub_billtemplet_b ");
		
				checkSql.append("inner join ");
		
				checkSql.append("(select pub_billtemplet_b.pk_billtemplet pk_billtemplet,min(showorder)  ordernum from pub_billtemplet_b left join pub_billtemplet on pub_billtemplet_b.pk_billtemplet =pub_billtemplet.pk_billtemplet  where  pub_billtemplet_b.listshowflag = 'Y' and pub_billtemplet_b.pos = '0' and pub_billtemplet.modulecode = '"
						+ moduleCode + "'  group by pub_billtemplet_b.pk_billtemplet) ordertable ");
		
				checkSql.append("on (pub_billtemplet_b.pk_billtemplet=ordertable.pk_billtemplet and  pub_billtemplet_b.showorder = ordertable.ordernum) ");
		
				checkSql.append("left join ");
		
				checkSql.append("pub_billtemplet ");
		
				checkSql.append("on pub_billtemplet_b.pk_billtemplet = pub_billtemplet.pk_billtemplet ");
		
				checkSql.append("where  pub_billtemplet_b.listshowflag = 'Y' and pub_billtemplet_b .pos = '0' and metadataproperty not like '%pk_org_v'");
		
				return new ScriptResourceQuery (checkSql.toString());
	}

	
	
	
	
	
	
	
	
//	@Override
//	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
//
//		String moduleCode = ruleExecContext.getBusinessComponent().getModule();
//
//		String checkSql = getCheckSql(moduleCode.toUpperCase());
//
//		DataSet dataset = SQLQueryExecuteUtils.executeQuery(checkSql, ruleExecContext.getRuntimeContext());
//
//		StringBuilder noteBuilder = new StringBuilder();
//
//		if (!dataset.isEmpty()) {
//
//			for (DataRow dr : dataset.getRows()) {
//
//				noteBuilder.append(String.format("单据模板：%s 模板主键：%s 列表状态下第一列显示为：%s不是主组织请检查", dr.getValue("templetname"),
//						dr.getValue("pk_billtemplet"), dr.getValue("metadataproperty")));
//
//				// noteBuilder.append(String.format("所属模块：%s 所属集团主键：%s 单据类型：s%\n",moduleCode,dr.getValue("pk_group"),dr.getValue("nbcrcode")));
//			}
//		}
//		return noteBuilder.toString().equals("") ? new SuccessRuleExecuteResult(getIdentifier())
//				: new ErrorRuleExecuteResult(getIdentifier(), noteBuilder.toString());
//	}
//
//	public String getCheckSql(String modulename) {
//		StringBuilder checkSql = new StringBuilder();
//
//		checkSql.append("select pub_billtemplet.bill_templetcaption templetname,pub_billtemplet_b.pk_billtemplet pk_billtemplet,pub_billtemplet_b.listshowflag listshowflag, ordernum,pub_billtemplet_b.metadataproperty ");
//
//		checkSql.append("from pub_billtemplet_b ");
//
//		checkSql.append("inner join ");
//
//		checkSql.append("(select pub_billtemplet_b.pk_billtemplet pk_billtemplet,min(showorder)  ordernum from pub_billtemplet_b left join pub_billtemplet on pub_billtemplet_b.pk_billtemplet =pub_billtemplet.pk_billtemplet  where  pub_billtemplet_b.listshowflag = 'Y' and pub_billtemplet_b.pos = '0' and pub_billtemplet.modulecode = '"
//				+ modulename + "'  group by pub_billtemplet_b.pk_billtemplet) ordertable ");
//
//		checkSql.append("on (pub_billtemplet_b.pk_billtemplet=ordertable.pk_billtemplet and  pub_billtemplet_b.showorder = ordertable.ordernum) ");
//
//		checkSql.append("left join ");
//
//		checkSql.append("pub_billtemplet ");
//
//		checkSql.append("on pub_billtemplet_b.pk_billtemplet = pub_billtemplet.pk_billtemplet ");
//
//		checkSql.append("where  pub_billtemplet_b.listshowflag = 'Y' and pub_billtemplet_b .pos = '0' and metadataproperty not like '%pk_org_v'");
//
//		return checkSql.toString();
//
//	}

	// public String getInSql(List<String> fieldnames){
	//
	// StringBuilder inSql = new StringBuilder();
	//
	// for(int i=0; i < fieldnames.size() ; i++){
	// if(i<fieldnames.size()-1){
	// inSql.append("'"+fieldnames.get(i)+"',");
	// }else{
	// inSql.append("'"+fieldnames.get(i)+"'");
	// }
	// }
	// return inSql.toString();
	// }


}
