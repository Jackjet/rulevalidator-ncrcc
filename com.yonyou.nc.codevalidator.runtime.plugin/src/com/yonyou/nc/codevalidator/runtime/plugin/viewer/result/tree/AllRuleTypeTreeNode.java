package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result.tree;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;

public class AllRuleTypeTreeNode extends AbstractResultTreeNode {

	@Override
	public String getDisplayText() {
		return "所有规则类型";
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public List<IRuleExecuteResult> getRuleExecResultList() {
		return getAllExecResults();
	}

	@Override
	protected List<IResultTreeNode> getChildrenNodes() {
		List<IResultTreeNode> result = new ArrayList<IResultTreeNode>();
		for (CatalogEnum catelogEnum : CatalogEnum.values()) {
			result.add(new RuleCatalogTreeNode(catelogEnum));
		}
		return result;
	}

	@Override
	public String getImageIcon() {
		return "/images/ruletype.gif";
	}

}
