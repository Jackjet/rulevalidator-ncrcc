package com.yonyou.nc.codevalidator.resparser.defaultrule;

import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.resparser.resource.utils.CommonRuleParams;
import com.yonyou.nc.codevalidator.resparser.resource.utils.SQLQueryExecuteUtils;
import com.yonyou.nc.codevalidator.rule.CreatorConstants;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.PublicRuleDefinitionParam;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.impl.AbstractRuleDefinition;

/**
 * 抽象的多语规则定义，执行前需要依赖多语临时表的初始化
 * 
 * @author mazhqa
 * @since V1.0
 */
@PublicRuleDefinitionParam(
		params = {
				CommonRuleParams.DOMAINPARAM,
				CommonRuleParams.FUNCNODEPARAM
						+ "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor" })
public abstract class AbstractLangRuleDefinition extends AbstractRuleDefinition {

	public static final String DOMAINKEY = "领域ID";

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		return processScriptRules(ruleExecContext);
	}

	protected abstract IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext)
			throws RuleBaseException;

	/**
	 * 取领域OID标识
	 * 
	 * @param context
	 * @return
	 */
	protected String getDomainKey(IRuleExecuteContext ruleExecContext) {
		String domainKey = "";
		if (ruleExecContext.getParameterArray(DOMAINKEY) != null) {
			domainKey = ruleExecContext.getParameterArray(DOMAINKEY)[0].toString();
		}
		return domainKey;
	}

	/**
	 * 执行普通表的查询
	 * 
	 * @param ruleExecuteContext
	 * @param sqlString
	 * @return
	 * @throws RuleBaseException
	 */
	protected DataSet executeQuery(IRuleExecuteContext ruleExecuteContext, String sqlString) throws RuleBaseException {
		return SQLQueryExecuteUtils.executeQuery(sqlString, ruleExecuteContext.getRuntimeContext());
	}

	/**
	 * 执行多语表（临时表）的查询
	 * 
	 * @param ruleExecuteContext
	 * @param sqlString
	 * @return
	 * @throws RuleBaseException
	 */
	protected DataSet executeMultiLangQuery(IRuleExecuteContext ruleExecuteContext, String sqlString)
			throws RuleBaseException {
		return SQLQueryExecuteUtils.executeMultiLangQuery(sqlString, ruleExecuteContext.getRuntimeContext());
	}

	@Override
	public String[] getDependentCreator() {
		return new String[] { CreatorConstants.I18N };
	}

}
