package com.yonyou.nc.codevalidator.resparser.defaultrule;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.ResourceManagerFacade;
import com.yonyou.nc.codevalidator.resparser.XmlResourceQuery;
import com.yonyou.nc.codevalidator.resparser.resource.XmlResource;
import com.yonyou.nc.codevalidator.resparser.resource.utils.CommonRuleParams;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteContext;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.PublicRuleDefinitionParam;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;
import com.yonyou.nc.codevalidator.rule.impl.AbstractRuleDefinition;

@PublicRuleDefinitionParam(params = { CommonRuleParams.DOMAINPARAM, CommonRuleParams.OIDMARKPARAM,
		CommonRuleParams.FUNCNODEPARAM + "=com.yonyou.nc.codevalidator.runtime.plugin.celleditor.descriptor.FuncNodeSelectCellEditorDescriptor"})
public abstract class AbstractXmlRuleDefinition extends AbstractRuleDefinition {

	@Override
	public IRuleExecuteResult execute(IRuleExecuteContext ruleExecContext) throws RuleBaseException {
		String funcNodes = ruleExecContext.getParameter(CommonRuleParams.FUNCNODEPARAM);
		if (funcNodes == null || funcNodes.trim().length() == 0) {
			throw new RuleBaseException("没有设置相应的功能节点，无法继续进行检查操作!");
		}
		XmlResourceQuery xmlResourceQuery = getXmlResourceQuery(funcNodes.split(XmlResourceQuery.FUNCSPLITTOKEN),
				ruleExecContext);
		xmlResourceQuery.setRuntimeContext(ruleExecContext.getRuntimeContext());
		List<XmlResource> resources = ResourceManagerFacade.getResource(xmlResourceQuery);
		return processScriptRules(ruleExecContext, resources);
	}

	protected abstract XmlResourceQuery getXmlResourceQuery(String[] functionNodes, IRuleExecuteContext ruleExecContext)
			throws RuleBaseException;

	protected abstract IRuleExecuteResult processScriptRules(IRuleExecuteContext ruleExecContext,
			List<XmlResource> resources) throws RuleBaseException;

}
