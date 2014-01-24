package com.yonyou.nc.codevalidator.runtime.plugin.config;

import java.util.Collections;
import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;

/**
 * ���ڽ���������й��˵������Ķ���
 * @author mazhqa
 * @since V2.6
 */
public class RuleDefinitionFilterContext {
	
	private final List<RuleDefinitionAnnotationVO> ruleDefinitionVoList;
	private final List<RuleDefinitionAnnotationVO> checkedRuleDefinitionVoList;
	private final List<RuleDefinitionAnnotationVO> selectedRuleDefinitionList;
	
	public RuleDefinitionFilterContext(List<RuleDefinitionAnnotationVO> ruleDefinitionVoList, List<RuleDefinitionAnnotationVO> checkedRuleDefinitionVoList, List<RuleDefinitionAnnotationVO> selectedRuleDefinitionList){
		this.ruleDefinitionVoList = ruleDefinitionVoList;
		this.checkedRuleDefinitionVoList = checkedRuleDefinitionVoList;
		this.selectedRuleDefinitionList = selectedRuleDefinitionList;
	}
	
	public List<RuleDefinitionAnnotationVO> getRuleDefinitionVoList() {
		return Collections.unmodifiableList(ruleDefinitionVoList);
	}
	
	public List<RuleDefinitionAnnotationVO> getCheckedRuleDefinitionVoList() {
		return Collections.unmodifiableList(checkedRuleDefinitionVoList);
	}
	
	public List<RuleDefinitionAnnotationVO> getSelectedRuleDefinitionList() {
		return Collections.unmodifiableList(selectedRuleDefinitionList);
	}

}
