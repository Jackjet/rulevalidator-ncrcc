package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;

/**
 * 配置对话框中的树节点，每个值对应一个具体的树节点
 * @author mazhqa
 * @since V2.6
 */
public interface IConfigTreeNode {
	
	/**
	 * 节点的显示名称
	 * @return
	 */
	String getDisplayText();

	/**
	 * 节点图标
	 * @return
	 */
	String getImageIcon();

	/**
	 * 得到子节点
	 * @return
	 */
	List<IConfigTreeNode> actualGetChildrenNode();

	/**
	 * 是否有子节点
	 * @return
	 */
	boolean hasChildren();

	/**
	 * 根据规则执行的上下文对规则的集合进行过滤
	 * @param filterContext
	 * @return
	 */
	List<RuleDefinitionAnnotationVO> filterRuleDefinitions(RuleDefinitionFilterContext filterContext);
}
