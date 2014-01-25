package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;

/**
 * 抽象的按照某些过滤条件进行过滤的树节点配置
 * @author mazhqa
 * @since V2.6
 */
public abstract class AbstractSingleConditionTreeNode extends AbstractConfigTreeNode {
	
	/**
	 * 根据规则执行的上下文对规则的集合进行过滤
	 * @param filterContext
	 * @return
	 */
	public final List<RuleDefinitionAnnotationVO> filterRuleDefinitions(RuleDefinitionFilterContext filterContext){
		List<RuleDefinitionAnnotationVO> result = new ArrayList<RuleDefinitionAnnotationVO>();
		List<RuleDefinitionAnnotationVO> ruleDefinitionVoList = filterContext.getRuleDefinitionVoList();
		for (RuleDefinitionAnnotationVO ruleDefinitionVO : ruleDefinitionVoList) {
			if(fulfillCondition(ruleDefinitionVO)){
				result.add(ruleDefinitionVO);
			}
		}
		return result;
	}
	
	/**
	 * 根据规则定义VO判断是否满足指定的条件
	 * @param ruleDefinitionVO
	 * @return
	 */
	protected abstract boolean fulfillCondition(RuleDefinitionAnnotationVO ruleDefinitionVO);


}
