package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.ArrayList;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;

/**
 * ����İ���ĳЩ�����������й��˵����ڵ�����
 * @author mazhqa
 * @since V2.6
 */
public abstract class AbstractSingleConditionTreeNode extends AbstractConfigTreeNode {
	
	/**
	 * ���ݹ���ִ�е������ĶԹ���ļ��Ͻ��й���
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
	 * ���ݹ�����VO�ж��Ƿ�����ָ��������
	 * @param ruleDefinitionVO
	 * @return
	 */
	protected abstract boolean fulfillCondition(RuleDefinitionAnnotationVO ruleDefinitionVO);


}
