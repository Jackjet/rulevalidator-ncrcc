package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result.tree;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionsReader;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;

public class RuleCatalogTreeNode extends AbstractFilteredResultTreeNode {

	private final CatalogEnum catalogEnum;

	public RuleCatalogTreeNode(CatalogEnum catalogEnum) {
		this.catalogEnum = catalogEnum;
	}

	@Override
	public String getImageIcon() {
		return "/images/ruletype.gif";
	}

	@Override
	public String getDisplayText() {
		return catalogEnum.getName();
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	protected boolean filterResult(IRuleExecuteResult result) {
		String ruleDefinitionIdentifier = result.getRuleDefinitionIdentifier();
		RuleDefinitionAnnotationVO ruleDefinitionVO = RuleDefinitionsReader.getInstance().getRuleDefinitionVO(
				ruleDefinitionIdentifier);
		return ruleDefinitionVO != null && catalogEnum.equals(ruleDefinitionVO.getCatalog());
	}

	@Override
	protected List<IResultTreeNode> getChildrenNodes() {
		SubCatalogEnum[] subCatalogs = SubCatalogEnum.getSubCatalogByCatalog(catalogEnum);
		List<IResultTreeNode> result = new ArrayList<IResultTreeNode>();
		for (SubCatalogEnum subCatalogEnum : subCatalogs) {
			result.add(new RuleSubCatalogTreeNode(subCatalogEnum));
		}
		return result;
	}

}
