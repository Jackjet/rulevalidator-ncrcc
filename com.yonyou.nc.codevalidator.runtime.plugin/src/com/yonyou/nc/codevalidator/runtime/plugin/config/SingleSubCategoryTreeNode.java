package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.Collections;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;

/**
 * 单个子分类的树节点
 * @author mazhqa
 * @since V2.6
 */
public class SingleSubCategoryTreeNode extends AbstractSingleConditionTreeNode {
	
	private final SubCatalogEnum subCatalog;
	
	public SingleSubCategoryTreeNode(SubCatalogEnum subCatalog) {
		this.subCatalog = subCatalog;
	}

	@Override
	public String getDisplayText() {
		return subCatalog.getName();
	}

	@Override
	public String getImageIcon() {
		return "/images/ruletype.gif";
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
		return subCatalog.equals(ruleDefinitionVO.getSubCatalog());
	}

}
