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

@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "检查单据接口定义中有没有重复的定义", relatedIssueId = "875",
		subCatalog = SubCatalogEnum.MD_FUNCCHECK, coder = "zhangnane", executeLayer = ExecuteLayer.MODULE,
		executePeriod = ExecutePeriod.DEPLOY)
public class TestCase00875 extends AbstractSingleScriptQueryRuleDefinition {

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
		return new DefaultFormatStringScriptExportStrategy("所属集团主键：%s 集团名称：%s 来源单据类型：%s 名称：%s 来源交易类型：%s 名称：%s 目的单据类型：%s 名称：%s 目的交易类型：%s 名称：%s\n",
				"pk_group", "groupname", "src_billtype", "srctypename", "src_transtype", "srctanstypename","dest_billtype","desttransname","dest_transtype","desttransname");

	}
	@Override
	protected ScriptResourceQuery getScriptResourceQuery(
			IRuleExecuteContext ruleExecContext) throws ResourceParserException {
		
		String moduleCode = ruleExecContext.getBusinessComponent().getModule().toUpperCase();
		// 按照集团和单据类型分组查询时判断分组结果中个数是否大于1
		StringBuilder checkSql = new StringBuilder();
		// 查询的字段有集团，集团名称，来源单据类型，来源单据类型名称，来源交易类型，来源交易类型名称，目的单据类型，目的单据类型名称，目的交易类型，目的交易类型名称，重复个数
		checkSql.append("select org_group.pk_group pk_group,org_group.name groupname,src_billtype,srcbilltable.billtypename srctypename,src_transtype , srctanstable.billtypename srctanstypename,dest_billtype,destbilltable.billtypename desttypename,dest_transtype,desttranstable.billtypename desttransname,num from ");
		// 根据字段：分组查询出重复个数
		checkSql.append("(select pub_billitfdef.pk_group pk_group,src_billtype,src_transtype,dest_billtype,dest_transtype,count(*) num from pub_billitfdef  ");
		checkSql.append("group by pk_group,src_billtype,src_transtype,dest_billtype,dest_transtype)  counttable");
		// 左联接查询集团名称
		checkSql.append(" left join org_group on org_group.pk_group = counttable.pk_group ");
		// 左联接查询来源单据类型名称
		checkSql.append("left join bd_billtype srcbilltable on counttable.src_billtype = srcbilltable.pk_billtypecode  ");
		// 左联接查询来源交易类型名称
		checkSql.append("left join bd_billtype srctanstable on counttable.src_transtype = srctanstable.pk_billtypecode ");
		// 左联接查询目的单据类型名称
		checkSql.append("left join bd_billtype destbilltable on counttable.dest_billtype = destbilltable.pk_billtypecode ");
		// 左联接查询目的交易类型名称
		checkSql.append("left join bd_billtype desttranstable on counttable.dest_transtype = desttranstable.pk_billtypecode ");
		// 以模块作为过滤条件
		checkSql.append("where (srcbilltable.systemcode ='" + moduleCode + "' or srctanstable.systemcode ='" + moduleCode
				+ "' or destbilltable.systemcode ='" + moduleCode + "' or desttranstable.systemcode = '" + moduleCode
				+ "' ) and num>1");

		return new ScriptResourceQuery (checkSql.toString());

	}


	
	
	
	
	
	
	
	
	//	@Override
//	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
//		String moduleCode = ruleExecContext.getBusinessComponent().getModule();
//		// 使用大写的查询
//		DataSet dataset = SQLQueryExecuteUtils.executeQuery(getCheckSql(moduleCode.toUpperCase()),
//				ruleExecContext.getRuntimeContext());
//
//		StringBuilder noteBuilder = new StringBuilder();
//		if (!dataset.isEmpty()) {
//			noteBuilder.append("以下单据接口定义重复:\n");
//			for (DataRow dr : dataset.getRows()) {
//				noteBuilder.append(String.format(
//						"所属集团主键：%s 集团名称：%s 来源单据类型：%s 名称：%s 来源交易类型：%s 名称：%s 目的单据类型：%s 名称：%s 目的交易类型：%s 名称：%s\n",
//						dr.getValue("pk_group"), dr.getValue("groupname"), dr.getValue("src_billtype"),
//						dr.getValue("srctypename"), dr.getValue("src_transtype"), dr.getValue("srctanstypename"),
//						dr.getValue("dest_billtype"), dr.getValue("desttransname"), dr.getValue("dest_transtype"),
//						dr.getValue("desttransname")));
//			}
//		}
//		return noteBuilder.toString().equals("") ? new SuccessRuleExecuteResult(getIdentifier())
//				: new ErrorRuleExecuteResult(getIdentifier(), noteBuilder.toString());
//	}
//
//	private String getCheckSql(String module) {
//		// 按照集团和单据类型分组查询时判断分组结果中个数是否大于1
//		StringBuilder checkSql = new StringBuilder();
//		// 查询的字段有集团，集团名称，来源单据类型，来源单据类型名称，来源交易类型，来源交易类型名称，目的单据类型，目的单据类型名称，目的交易类型，目的交易类型名称，重复个数
//		checkSql.append("select org_group.pk_group pk_group,org_group.name groupname,src_billtype,srcbilltable.billtypename srctypename,src_transtype , srctanstable.billtypename srctanstypename,dest_billtype,destbilltable.billtypename desttypename,dest_transtype,desttranstable.billtypename desttransname,num from ");
//		// 根据字段：分组查询出重复个数
//		checkSql.append("(select pub_billitfdef.pk_group pk_group,src_billtype,src_transtype,dest_billtype,dest_transtype,count(*) num from pub_billitfdef  ");
//		checkSql.append("group by pk_group,src_billtype,src_transtype,dest_billtype,dest_transtype)  counttable");
//		// 左联接查询集团名称
//		checkSql.append(" left join org_group on org_group.pk_group = counttable.pk_group ");
//		// 左联接查询来源单据类型名称
//		checkSql.append("left join bd_billtype srcbilltable on counttable.src_billtype = srcbilltable.pk_billtypecode  ");
//		// 左联接查询来源交易类型名称
//		checkSql.append("left join bd_billtype srctanstable on counttable.src_transtype = srctanstable.pk_billtypecode ");
//		// 左联接查询目的单据类型名称
//		checkSql.append("left join bd_billtype destbilltable on counttable.dest_billtype = destbilltable.pk_billtypecode ");
//		// 左联接查询目的交易类型名称
//		checkSql.append("left join bd_billtype desttranstable on counttable.dest_transtype = desttranstable.pk_billtypecode ");
//		// 以模块作为过滤条件
//		checkSql.append("where (srcbilltable.systemcode ='" + module + "' or srctanstable.systemcode ='" + module
//				+ "' or destbilltable.systemcode ='" + module + "' or desttranstable.systemcode = '" + module
//				+ "' ) and num>1");
//
//		return checkSql.toString();
//
//	}
}
