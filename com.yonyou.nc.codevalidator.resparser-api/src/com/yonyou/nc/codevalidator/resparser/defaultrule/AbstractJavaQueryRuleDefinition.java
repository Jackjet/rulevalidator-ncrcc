package com.yonyou.nc.codevalidator.resparser.defaultrule;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.JavaResourceQuery;
import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.resource.JavaClassResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.CommonParamProcessUtils;
import com.yonyou.nc.codevalidator.resparser.resource.utils.CommonRuleParams;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.PublicRuleDefinitionParam;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.impl.AbstractRuleDefinition;

/**
 * Abstract java rule defintion, the main flow is set.
 * 
 * @author mazhqa
 * 
 */
@PublicRuleDefinitionParam(params = CommonRuleParams.CODECONTAINEXTRAFOLDER)
public abstract class AbstractJavaQueryRuleDefinition extends AbstractRuleDefinition {

	@Override
	public final IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		JavaResourceQuery javaResourceQuery = getJavaResourceQuery(ruleExecContext);
		javaResourceQuery.setContainExtraWrapper(CommonParamProcessUtils.getCodeContainsExtraFolder(ruleExecContext));
		List<JavaClassResource> resources = ResourceManagerFacade.getResource(javaResourceQuery);
		return processJavaRules(ruleExecContext, resources);
	}

	/**
	 * Build java resource query from context.
	 * 
	 * @param ruleExecContext
	 * @return
	 */
	protected abstract JavaResourceQuery getJavaResourceQuery(IRuleExecuteContext ruleExecContext)
			throws RuleBaseException;

	/**
	 * Process Java Resources, Return execute result.
	 * 
	 * @param resources
	 * @return
	 */
	protected abstract IRuleExecuteResult processJavaRules(IRuleExecuteContext ruleExecContext,
			List<JavaClassResource> resources) throws RuleBaseException;
	
}
