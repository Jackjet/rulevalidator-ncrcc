package com.yonyou.nc.codevalidator.runtime.plugin.viewer.result;

import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;

/**
 * ��Ӧ�����ϸչʾ����
 * @author mazhqa
 *
 */
public interface IResultDetailComposite<T extends IRuleExecuteResult> {
	
	void loadRuleExecuteResult(T executeResult);

}
