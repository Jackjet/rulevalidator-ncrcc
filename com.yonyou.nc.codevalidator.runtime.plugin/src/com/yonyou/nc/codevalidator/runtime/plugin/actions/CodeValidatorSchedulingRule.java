package com.yonyou.nc.codevalidator.runtime.plugin.actions;

import java.util.Arrays;

import org.eclipse.core.runtime.jobs.ISchedulingRule;

import com.yonyou.nc.codevalidator.runtime.plugin.PluginRuntimeContext;

/**
 * 规则验证调度规则，规则检查任务不能同时执行
 * 
 * @author mazhqa
 * 
 */
public class CodeValidatorSchedulingRule implements ISchedulingRule {

	private final PluginRuntimeContext pluginRuntimeContext;

	public CodeValidatorSchedulingRule(PluginRuntimeContext pluginRuntimeContext) {
		this.pluginRuntimeContext = pluginRuntimeContext;
	}

	public PluginRuntimeContext getPluginRuntimeContext() {
		return pluginRuntimeContext;
	}

	@Override
	public boolean contains(ISchedulingRule rule) {
		if (rule instanceof CodeValidatorSchedulingRule) {
			CodeValidatorSchedulingRule otherCodeValidatorRule = (CodeValidatorSchedulingRule) rule;
			PluginRuntimeContext otherPluginRuntimeContext = otherCodeValidatorRule.getPluginRuntimeContext();
			return Arrays.asList(pluginRuntimeContext.getBusinessComponents()).containsAll(
					Arrays.asList(otherPluginRuntimeContext.getBusinessComponents()));
		}
		return false;
	}

	@Override
	public boolean isConflicting(ISchedulingRule rule) {
//		if (rule instanceof CodeValidatorSchedulingRule) {
//			CodeValidatorSchedulingRule otherCodeValidatorRule = (CodeValidatorSchedulingRule) rule;
//			PluginRuntimeContext otherPluginRuntimeContext = otherCodeValidatorRule.getPluginRuntimeContext();
//			List<BusinessComponent> otherBusinessComponentList = Arrays.asList(otherPluginRuntimeContext
//					.getBusinessComponents());
//			for (BusinessComponent businessComponent : pluginRuntimeContext.getBusinessComponents()) {
//				if (otherBusinessComponentList.contains(businessComponent)) {
//					return true;
//				}
//			}
//		}
		return rule instanceof CodeValidatorSchedulingRule;
	}

}
