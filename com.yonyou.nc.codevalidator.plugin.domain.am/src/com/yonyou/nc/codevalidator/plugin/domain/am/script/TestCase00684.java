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
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;

/**
 * 
 * @author ZG
 * 
 */
@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "语义模型业务联查注册的校验", relatedIssueId = "684",
		subCatalog = SubCatalogEnum.CS_CONTENTCHECK, coder = "ZG")
public class TestCase00684 extends AbstractSingleScriptQueryRuleDefinition {

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
		return new DefaultFormatStringScriptExportStrategy("已注册的业务联查中不存在节点名为：%s 的语义模型","nameintrace");
	}

	@Override
	protected ScriptResourceQuery getScriptResourceQuery(
			IRuleExecuteContext ruleExecContext) throws ResourceParserException {
		String moduleCode = ruleExecContext.getBusinessComponent().getModule().toUpperCase();
		
		StringBuilder querySql = new StringBuilder();
		querySql.append("select CLASSNAME,MODULENAME,PK_TRACEDATA,trace .tracedata, iufo_tracedataregister .TRACEDATANAME ANAMEINIUFO,trace.tracedataname  nameintrace ");
		querySql.append("from iufo_tracedataregister  ");
		querySql.append("right join (");
		querySql.append("select dis.PK_TRACEDATA  tracedata ,reg.TRACEDATANAME tracedataname ");
		querySql.append("from  iufo_tracedatadispatch dis,iufo_tracedataregister reg ");
		querySql.append("where dis.PK_TRACEDATA = reg.PK_TRACEDATA  )  trace  ");
		querySql.append("on trace.tracedata = PK_TRACEDATA ");
		querySql.append("where  iufo_tracedataregister.PK_TRACEDATA = null ");
		querySql.append("and MODULENAME = '"+moduleCode+" '");
		
		return new ScriptResourceQuery (querySql.toString());
	}

//	@Override
//	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
//		String moduleCode = ruleExecContext.getBusinessComponent().getModule();
//		StringBuilder notebuilder = new StringBuilder();
//
//		// REG
//		String regSQL = "select PK_TRACEDATA,TRACEDATANAME " + "from iufo_tracedataregister "
//				+ "where upper(MODULENAME) in ('" + moduleCode.toUpperCase() + "')";
//
//		DataSet regdataset = executeQuery(ruleExecContext, regSQL.toString());
//		List<DataRow> regdatarow = regdataset.getRows();
//		List<String> listdistemp = new ArrayList<String>();
//		for (DataRow dr : regdatarow) {
//			listdistemp.add((String) dr.getValue("PK_TRACEDATA"));
//		}
//
//		// reg --- > 语义模型注册 个数
//		String sqllistdistemp = "select dis.PK_TRACEDATA,reg.TRACEDATANAME  "
//				+ "from  iufo_tracedatadispatch dis,iufo_tracedataregister reg "
//				+ "where dis.PK_TRACEDATA = reg.PK_TRACEDATA  " + "and reg.MODULENAME in ('" + moduleCode.toLowerCase()
//				+ "','" + moduleCode.toUpperCase() + "')";
//		DataSet tempdisdataset = executeQuery(ruleExecContext, sqllistdistemp.toString());
//		List<DataRow> tempdisdatarow = tempdisdataset.getRows();
//		List<String> listdistemp2 = new ArrayList<String>();
//		for (DataRow dr : tempdisdatarow) {
//			listdistemp2.add((String) dr.getValue("PK_TRACEDATA"));
//		}
//
//		List<String> list = new ArrayList<String>();
//		if (listdistemp.size() != listdistemp2.size()) {
//			for (DataRow dr : regdatarow) {
//				if (!listdistemp2.contains(dr.getValue("PK_TRACEDATA"))) {
//					list.add((String) dr.getValue("TRACEDATANAME"));
//				}
//			}
//		}
//
//		if (list.size() >= 1) {
//			for (String s : list) {
//				notebuilder.append(s + " | ");
//			}
//		}
//
//		return notebuilder.toString().equals("") ? new SuccessRuleExecuteResult(getIdentifier())
//				: new ErrorRuleExecuteResult(getIdentifier(), "缺少节点名为" + notebuilder.toString() + "的业务联查注册");
//
//	}
}
