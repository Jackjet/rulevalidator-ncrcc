package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.Collections;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;
import com.yonyou.nc.codevalidator.rule.annotation.ScopeEnum;

/**
 * @author mazhqa
 * @since V2.6
 */
public class SingleScopeConditionTreeNode extends AbstractSingleConditionTreeNode{
	
	private final ScopeEnum scope;
	
	public SingleScopeConditionTreeNode(ScopeEnum scope) {
		super();
		this.scope = scope;
	}

	@Override
	public String getDisplayText() {
		return scope.getName();
	}

	@Override
	public String getImageIcon() {
		return null;
	}

	@Override
	public List<IConfigTreeNode> actualGetChildrenNode() {
		return Collections.emptyList();
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	protected boolean fulfillCondition(RuleDefinitionAnnotationVO ruleDefinitionVO) {
		return scope.equals(ruleDefinitionVO.getScope());
	}

}
