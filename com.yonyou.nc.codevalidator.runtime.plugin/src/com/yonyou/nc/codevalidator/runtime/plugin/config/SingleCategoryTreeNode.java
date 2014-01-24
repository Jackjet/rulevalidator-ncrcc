package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;
import com.yonyou.nc.codevalidator.rule.annotation.SubCatalogEnum;

/**
 * 单个分类的树节点
 * @author mazhqa
 * @since V2.6
 */
public class SingleCategoryTreeNode extends AbstractSingleConditionTreeNode {
	
	private final CatalogEnum catalog;
	
	public SingleCategoryTreeNode(CatalogEnum catalogEnum){
		this.catalog = catalogEnum;
	}

	@Override
	public String getDisplayText() {
		return catalog.getName();
	}

	@Override
	public String getImageIcon() {
		return "/images/ruletype.gif";
	}

	@Override
	public List<IConfigTreeNode> actualGetChildrenNode() {
		SubCatalogEnum[] subCatalogs = SubCatalogEnum.getSubCatalogByCatalog(catalog);
		List<IConfigTreeNode> result = new ArrayList<IConfigTreeNode>();
		for (SubCatalogEnum subCatalog : subCatalogs) {
			result.add(new SingleSubCategoryTreeNode(subCatalog));
		}
		return result;
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	protected boolean fulfillCondition(RuleDefinitionAnnotationVO ruleDefinitionVO) {
		return catalog.equals(ruleDefinitionVO.getCatalog());
	}

}
