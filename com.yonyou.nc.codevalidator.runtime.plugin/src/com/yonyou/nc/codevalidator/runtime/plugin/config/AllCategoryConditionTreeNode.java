package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.CatalogEnum;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;

/**
 * ���й������͵����ڵ�
 * 
 * @author mazhqa
 * @since V2.6
 */
public class AllCategoryConditionTreeNode extends AbstractConfigTreeNode {

	@Override
	public String getDisplayText() {
		return "���й�������";
	}

	@Override
	public String getImageIcon() {
		return "/images/ruletype.gif";
	}

	@Override
	public List<IConfigTreeNode> actualGetChildrenNode() {
		List<IConfigTreeNode> result = new ArrayList<IConfigTreeNode>();
		for (CatalogEnum catalog : CatalogEnum.values()) {
			result.add(new SingleCategoryTreeNode(catalog));
		}
		return result;
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public List<RuleDefinitionAnnotationVO> filterRuleDefinitions(RuleDefinitionFilterContext filterContext) {
		return filterContext.getRuleDefinitionVoList();
	}

}
