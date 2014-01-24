package com.yonyou.nc.codevalidator.export.api;

import com.yonyou.nc.codevalidator.rule.IIdentifier;
import com.yonyou.nc.codevalidator.rule.SessionRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 全部规则结果输出策略
 * <P>
 * 此接口不能直接使用，适用于给其他人实现
 * 
 * @author mazhqa
 * @since V2.7
 */
public interface ITotalRuleExportStrategy extends IIdentifier {

	/**
	 * 一次的执行结果全部输出
	 * 
	 * @param sessionRuleExecuteResult
	 * @throws RuleBaseException
	 */
	void totalResultExport(SessionRuleExecuteResult sessionRuleExecuteResult) throws RuleBaseException;

}
