package com.yonyou.nc.codevalidator.rule.vo;

import java.util.List;

import com.yonyou.nc.codevalidator.rule.annotation.RepairLevel;
import com.yonyou.nc.codevalidator.rule.annotation.RuleDefinitionAnnotationVO;

/**
 * 配置之后的规则配置VO
 * 
 * @author mazhqa
 * 
 */
public class RuleItemConfigVO {
	
	private RuleExecuteLevel ruleExecuteLevel;

	private final RuleDefinitionAnnotationVO ruleDefinitionVO;

	private final PrivateParamConfiguration privateParamConfigration;

	public RuleItemConfigVO(RuleDefinitionAnnotationVO ruleDefinitionVO) {
		this.ruleDefinitionVO = ruleDefinitionVO;
		this.privateParamConfigration = new PrivateParamConfiguration();

		PrivateParamConfiguration originConfiguration = ruleDefinitionVO.getPrivateParamConfiguration();
		for (ParamConfiguration paramConfiguration : originConfiguration.getParamConfigurationList()) {
			privateParamConfigration.addParamName(paramConfiguration);
		}
		
		if(ruleDefinitionVO.getRepairLevel() == RepairLevel.SUGGESTREPAIR) {
			ruleExecuteLevel = RuleExecuteLevel.Major;
		} else {
			ruleExecuteLevel = RuleExecuteLevel.Critical;
		}
	}

	public RuleDefinitionAnnotationVO getRuleDefinitionVO() {
		return ruleDefinitionVO;
	}

	public PrivateParamConfiguration getPrivateParamConfiguration() {
		return privateParamConfigration;
	}

	/**
	 * 设置私有的参数配置，如果已经设置过，更新paramValue
	 * @param privateParamConfiguration1
	 */
	public void setPrivateParamConfiguration(PrivateParamConfiguration privateParamConfiguration1) {
		List<ParamConfiguration> paramConfigurationList = privateParamConfiguration1.getParamConfigurationList();
		for (ParamConfiguration paramConfiguration : paramConfigurationList) {
			privateParamConfigration.addParamName(paramConfiguration);
		}
	}

	public RuleExecuteLevel getRuleExecuteLevel() {
		return ruleExecuteLevel;
	}

	public void setRuleExecuteLevel(RuleExecuteLevel ruleExecuteLevel) {
		this.ruleExecuteLevel = ruleExecuteLevel;
	}
	
}
