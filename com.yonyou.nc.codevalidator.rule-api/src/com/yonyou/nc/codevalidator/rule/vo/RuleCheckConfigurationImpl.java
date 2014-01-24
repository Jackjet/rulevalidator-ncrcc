package com.yonyou.nc.codevalidator.rule.vo;

import java.util.Collections;
import java.util.List;

/**
 * ���������ö���, ����̬����
 * 
 * @author mazhqa
 * 
 */
public class RuleCheckConfigurationImpl extends AbstractRuleCheckConfiguration {
	
	private int priority ;

	public CommonParamConfiguration getCommonParamConfiguration() {
		return commonParamConfiguration;
	}

	public List<RuleItemConfigVO> getRuleItemConfigVoList() {
		return Collections.unmodifiableList(ruleItemConfigVoList);
	}

	@Override
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}

}
