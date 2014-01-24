package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;

/**
 * 对应结果详细展示界面
 * @author mazhqa
 *
 */
public interface IResultDetailComposite<T extends IRuleExecuteResult> {
	
	void loadRuleExecuteResult(T executeResult);

}
