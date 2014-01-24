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
 * ����Ķ�������壬ִ��ǰ��Ҫ����������ʱ��ĳ�ʼ��
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

	public static final String DOMAINKEY = "����ID";

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		return processScriptRules(ruleExecContext);
	}

	protected abstract IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext)
			throws RuleBaseException;

	/**
	 * ȡ����OID��ʶ
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
	 * ִ����ͨ��Ĳ�ѯ
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
	 * ִ�ж������ʱ���Ĳ�ѯ
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
