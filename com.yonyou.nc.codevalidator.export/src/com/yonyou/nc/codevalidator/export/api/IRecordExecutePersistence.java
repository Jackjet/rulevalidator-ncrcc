package com.yonyou.nc.codevalidator.export.api;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 此接口专为执行记录的保存和更新设计
 * 
 * @author mazhqa
 * @since V2.9
 */
public interface IRecordExecutePersistence {

	/**
	 * 在执行开始后，会进行该方法的回调调用，保存至db并返回其主键
	 * @param ruleExportContext
	 * @return
	 * @throws RuleBaseException
	 */
	String insertRecord(RuleExportContext ruleExportContext) throws RuleBaseException;

	/**
	 * 在执行结束后，会进行该方法的回调调用，可以设置其执行结束时间，返回其主键
	 * @param ruleExportContext
	 * @return
	 * @throws RuleBaseException
	 */
	String updateRecord(RuleExportContext ruleExportContext) throws RuleBaseException;

}
