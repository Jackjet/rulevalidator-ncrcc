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
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;


@RuleDefinition(catalog = CatalogEnum.PRESCRIPT, description = "检查单据转换规则中集团下有没有重复的VO对照（A-B），有则不对"
	, relatedIssueId = "883", subCatalog = SubCatalogEnum.PS_CONTENTCHECK,coder="zhangnane",
		executeLayer = ExecuteLayer.MODULE)
public class TestCase00883 extends AbstractSingleScriptQueryRuleDefinition{

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

		return new DefaultFormatStringScriptExportStrategy("所属集团主键：%s 集团名称：%s 来源单据类型：%s 名称：%s 来源交易类型：%s 名称：%s 目的单据类型：%s 名称：%s 目的交易类型：%s 名称：%s\n"
				,"groupname","srctypename","srctanstypename","desttransname","desttransname");
	}

	@Override
	protected ScriptResourceQuery getScriptResourceQuery(
			IRuleExecuteContext ruleExecContext) throws ResourceParserException {
		
		String moduleCode = ruleExecContext.getBusinessComponent().getModule().toUpperCase();
		StringBuilder checkSql = new StringBuilder();
		//按照集团和单据类型分组查询时判断分组结果中个数是否大于1
		//查询的字段有集团，集团名称，来源单据类型，来源单据类型名称，来源交易类型，来源交易类型名称，目的单据类型，目的单据类型名称，目的交易类型，目的交易类型名称，重复个数
		checkSql.append("SELECT org_group.pk_group pk_group,org_group.name groupname,src_billtype,srcbilltable.billtypename srctypename,src_transtype , srctanstable.billtypename srctanstypename,dest_billtype,destbilltable.billtypename desttypename,dest_transtype,desttranstable.billtypename desttransname,num FROM ");
		//根据字段：分组查询出重复个数
		checkSql.append("(SELECT pk_group,src_billtype,dest_billtype,dest_transtype,src_transtype,count(*) num FROM pub_vochange GROUP BY pk_group,src_billtype,dest_billtype ,dest_transtype,src_transtype " );
		checkSql.append("HAVING src_billtype in (SELECT pk_billtypecode FROM bd_billtype WHERE systemcode like '"+moduleCode+"') or dest_billtype in (SELECT pk_billtypecode FROM bd_billtype WHERE systemcode like '"+moduleCode+"')) counttable ");	
		//左联接查询集团名称
		checkSql.append("LEFT JOIN  org_group ON org_group.pk_group = counttable.pk_group ");
		//左联接查询来源单据类型名称
		checkSql.append("LEFT JOIN bd_billtype srcbilltable ON counttable.src_billtype = srcbilltable.pk_billtypecode ");
		//左联接查询来源交易类型名称
		checkSql.append("LEFT JOIN bd_billtype srctanstable ON counttable.src_transtype = srctanstable.pk_billtypecode ");
		//左联接查询目的单据类型名称
		checkSql.append("LEFT JOIN bd_billtype destbilltable ON counttable.dest_billtype = destbilltable.pk_billtypecode ");
		//左联接查询目的交易类型名称
		checkSql.append("LEFT JOIN bd_billtype desttranstable ON counttable.dest_transtype = desttranstable.pk_billtypecode ");
		
		checkSql.append("WHERE num> 1");
	
		return new ScriptResourceQuery (checkSql.toString());
	}

	//这些字段必须是pub_vochange表中的字段，判定是否重复的字段:集团，来源单据类型，目的单据类型，来源交易类型，目的交易类型
//	private final String [] checkRepetList = {"pk_group","src_billtype","dest_billtype","dest_transtype","src_transtype"};
	
//	@Override
//	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext)
//			throws RuleBaseException {
//		//TODO: 重构 模块级，问题状态
//		String moduleCode = ruleExecContext.getBusinessComponent().getModule();
//		DataSet dataset = SQLQueryExecuteUtils.executeQuery(getCheckSql(moduleCode.toUpperCase()),ruleExecContext.getRuntimeContext());
//		
//		StringBuilder noteBuilder = new StringBuilder();
//		if(!dataset.isEmpty()){
//			noteBuilder.append("以下vo交换规则重复:\n");
//			for(DataRow dr : dataset.getRows()){
//				noteBuilder.append(String.format("所属集团主键：%s 集团名称：%s 来源单据类型：%s 名称：%s 来源交易类型：%s 名称：%s 目的单据类型：%s 名称：%s 目的交易类型：%s 名称：%s\n",
//				dr.getValue("pk_group"),dr.getValue("groupname"),
//				dr.getValue("src_billtype"),dr.getValue("srctypename"),
//				dr.getValue("src_transtype"),dr.getValue("srctanstypename"),
//				dr.getValue("dest_billtype"),dr.getValue("desttransname"),
//				dr.getValue("dest_transtype"),dr.getValue("desttransname")));
//			}
//		}
//		return noteBuilder.toString().equals("") ? new SuccessRuleExecuteResult(getIdentifier())
//		: new ErrorRuleExecuteResult(getIdentifier(), noteBuilder.toString());
//	}
//	
//	private String getCheckSql(String moduleCode){
//		
//		StringBuilder checkSql = new StringBuilder();
//		//按照集团和单据类型分组查询时判断分组结果中个数是否大于1
//		//查询的字段有集团，集团名称，来源单据类型，来源单据类型名称，来源交易类型，来源交易类型名称，目的单据类型，目的单据类型名称，目的交易类型，目的交易类型名称，重复个数
//		checkSql.append("SELECT org_group.pk_group pk_group,org_group.name groupname,src_billtype,srcbilltable.billtypename srctypename,src_transtype , srctanstable.billtypename srctanstypename,dest_billtype,destbilltable.billtypename desttypename,dest_transtype,desttranstable.billtypename desttransname,num FROM ");
//		//根据字段：分组查询出重复个数
//		checkSql.append("(SELECT pk_group,src_billtype,dest_billtype,dest_transtype,src_transtype,count(*) num FROM pub_vochange GROUP BY pk_group,src_billtype,dest_billtype ,dest_transtype,src_transtype " );
//		checkSql.append("HAVING src_billtype in (SELECT pk_billtypecode FROM bd_billtype WHERE systemcode like '"+moduleCode+"') or dest_billtype in (SELECT pk_billtypecode FROM bd_billtype WHERE systemcode like '"+moduleCode+"')) counttable ");	
//		//左联接查询集团名称
//		checkSql.append("LEFT JOIN  org_group ON org_group.pk_group = counttable.pk_group ");
//		//左联接查询来源单据类型名称
//		checkSql.append("LEFT JOIN bd_billtype srcbilltable ON counttable.src_billtype = srcbilltable.pk_billtypecode ");
//		//左联接查询来源交易类型名称
//		checkSql.append("LEFT JOIN bd_billtype srctanstable ON counttable.src_transtype = srctanstable.pk_billtypecode ");
//		//左联接查询目的单据类型名称
//		checkSql.append("LEFT JOIN bd_billtype destbilltable ON counttable.dest_billtype = destbilltable.pk_billtypecode ");
//		//左联接查询目的交易类型名称
//		checkSql.append("LEFT JOIN bd_billtype desttranstable ON counttable.dest_transtype = desttranstable.pk_billtypecode ");
//		
//		checkSql.append("WHERE num> 1");
//	
//		return checkSql.toString();
//	}
//
//	
	

}
