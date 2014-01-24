package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result.tree;

import java.util.List;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionsReader;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;

public class RuleSubCatalogTreeNode extends AbstractFilteredResultTreeNode {

	private final SubCatalogEnum subCatalogEnum;

	public RuleSubCatalogTreeNode(SubCatalogEnum subCatalogEnum) {
		this.subCatalogEnum = subCatalogEnum;
	}

	@Override
	public String getImageIcon() {
		return "/images/ruletype.gif";
	}

	@Override
	public String getDisplayText() {
		return subCatalogEnum.getName();
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	protected boolean filterResult(IRuleExecuteResult result) {
		String ruleDefinitionIdentifier = result.getRuleDefinitionIdentifier();
		RuleDefinitionAnnotationVO ruleDefinitionVO = RuleDefinitionsReader.getInstance().getRuleDefinitionVO(
				ruleDefinitionIdentifier);
		return ruleDefinitionVO != null && subCatalogEnum.equals(ruleDefinitionVO.getSubCatalog());
	}

	@Override
	protected List<IResultTreeNode> getChildrenNodes() {
		return null;
	}

}
