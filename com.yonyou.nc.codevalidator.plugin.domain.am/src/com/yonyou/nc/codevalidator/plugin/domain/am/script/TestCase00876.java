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
 * @author ZG 7.15
 */
@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "语义模型业务联查注册的深化校验", relatedIssueId = "876",
		subCatalog = SubCatalogEnum.CS_CONTENTCHECK, coder = "zhangguang")
public class TestCase00876 extends AbstractSingleScriptQueryRuleDefinition {

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
		return new DefaultFormatStringScriptExportStrategy("缺少节点名为：%s 的业务联查注册","nameintrace");
	}
	@Override
	protected ScriptResourceQuery getScriptResourceQuery(
			IRuleExecuteContext ruleExecContext) throws ResourceParserException {
		
		String moduleCode = ruleExecContext.getBusinessComponent().getModule().toUpperCase();
		
		StringBuilder querySql = new StringBuilder();
		querySql.append("select CLASSNAME,MODULENAME,PK_TRACEDATA,trace .tracedata, iufo_tracedataregister .TRACEDATANAME ANAMEINIUFO,trace.tracedataname  nameintrace ");
		querySql.append("from iufo_tracedataregister  ");
		querySql.append("left join (");
		querySql.append("select dis.PK_TRACEDATA  tracedata ,reg.TRACEDATANAME tracedataname ");
		querySql.append("from  iufo_tracedatadispatch dis,iufo_tracedataregister reg ");
		querySql.append("where dis.PK_TRACEDATA = reg.PK_TRACEDATA  )  trace  ");
		querySql.append("on trace.tracedata = PK_TRACEDATA ");
		querySql.append("where  trace.tracedata = null ");
		querySql.append("and MODULENAME = '"+moduleCode +"'");
		
		return new ScriptResourceQuery (querySql.toString());
	}

	
	
	
	
	
	
	
	
	
	
	//
//	@Override
//	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws ResourceParserException {
//		//TODO: 全连接 查找左或右为空
//		String moduleCode = ruleExecContext.getBusinessComponent().getModule();
//		StringBuilder notebuilder = new StringBuilder();
//		// REG
//		String regSQL = "select CLASSNAME,MODULENAME,PK_TRACEDATA,TRACEDATANAME " + "from iufo_tracedataregister "
//				+ "where MODULENAME ='" + moduleCode + "'";
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
//				+ "where dis.PK_TRACEDATA = reg.PK_TRACEDATA  " + "and reg.MODULENAME = '" + moduleCode + "'";
//		DataSet tempdisdataset = executeQuery(ruleExecContext, sqllistdistemp.toString());
//		List<DataRow> tempdisdatarow = tempdisdataset.getRows();
//		List<String> listdistemp2 = new ArrayList<String>();
//		for (DataRow dr : tempdisdatarow) {
//			listdistemp2.add((String) dr.getValue("PK_TRACEDATA"));
//		}
//
//		
//		ListEqualVO<String> judgeVO = CollectionUtils.isListEqual(listdistemp, listdistemp2);
//		
//		
//		List<String> list = new ArrayList<String>();
//		if (!judgeVO.getIsEqual()) {
//
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
//	}
}
