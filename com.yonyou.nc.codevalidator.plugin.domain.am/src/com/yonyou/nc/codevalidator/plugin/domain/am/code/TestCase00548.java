package com.yonyou.nc.codevalidator.plugin.domain.am.code;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceParserException;
import com.yonyou.nc.codevalidator.resparser.dataset.DataRow;
import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.defaultrule.AbstractJavaQueryRuleDefinition;
import com.yonyou.nc.codevalidator.resparser.executeresult.ErrorRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.executeresult.SuccessRuleExecuteResult;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.ExecuteLayer;
import com.yonyou.nc.codevalidator.rule.annotation.ExecutePeriod;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinition;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.sdk.code.JavaResPrivilege;

/**
 * 
 * @author zhangnane
 * 
 */
@RuleDefinition(catalog = CatalogEnum.JAVACODE, description = "检查动作脚本是否插入数据库", relatedIssueId = "548",
		subCatalog = SubCatalogEnum.JC_PREBUILDCHECK, coder = "zhangnane", executeLayer = ExecuteLayer.BUSICOMP,
		executePeriod = ExecutePeriod.DEPLOY)
public class TestCase00548 extends AbstractJavaQueryRuleDefinition {

	private static final String PREFIX = "nc.bs.pub.action.n_";

	@Override
	protected JavaResourceQuery getJavaResourceQuery(IRuleExecuteContext ruleExecContext)
			throws ResourceParserException {
		JavaResourceQuery javaResourceQuery = new JavaResourceQuery();
		javaResourceQuery.setBusinessComponent(ruleExecContext.getBusinessComponent());
		javaResourceQuery.setResPrivilege(JavaResPrivilege.PRIVATE);
		return javaResourceQuery;
	}

	@Override
	protected IRuleExecuteResult processJavaRules(IRuleExecuteContext ruleExecContext, List<JavaClassResource> resources)
			throws RuleBaseException {
		// 获取所有脚本名
		List<String> actionScriptNames = new ArrayList<String>();
		for (JavaClassResource classresource : resources) {
			if (classresource.getJavaCodeClassName().startsWith(PREFIX)) {
				actionScriptNames.add(classresource.getLastJavaClassName());
			}
		}
		if(actionScriptNames.isEmpty()) {
			return new SuccessRuleExecuteResult(getIdentifier());
		}
		String sql = "select classname from pub_busiclass where "
				+ SQLQueryExecuteUtils.buildSqlForIn("classname", actionScriptNames.toArray(new String[0]));
		DataSet dataSet = SQLQueryExecuteUtils.executeQuery(sql.toString(), ruleExecContext.getRuntimeContext());
		if (!dataSet.isEmpty()) {
			for (DataRow dr : dataSet.getRows()) {
				actionScriptNames.remove(dr.getValue("classname"));
			}
		}
		StringBuilder resultBuilder = new StringBuilder();
		if (!actionScriptNames.isEmpty()) {
			for (String actionScriptName : actionScriptNames) {
				resultBuilder.append(String.format("动作脚本：%s 未在数据库中注册\n", actionScriptName));
			}
		}
		return actionScriptNames.isEmpty() ? new SuccessRuleExecuteResult(getIdentifier())
				: new ErrorRuleExecuteResult(getIdentifier(), resultBuilder.toString());
	}
}
