package com.yonyou.nc.codevalidator.export;

import java.io.File;
import java.util.List;

import com.yonyou.nc.codevalidator.export.api.RuleExportContext;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.SessionRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * 规则进行导出的策略接口
 * @author mazhqa
 * @since V2.7
 */
public interface IRuleExportStrategy {

	/**
	 * 一次的执行结果全部输出
	 * 
	 * @param sessionRuleExecuteResult
	 * @throws RuleBaseException
	 */
	void totalResultExport(SessionRuleExecuteResult sessionRuleExecuteResult) throws RuleBaseException;

	/**
	 * 批量规则执行结果生成，进行保存操作，保存的格式不限
	 * 
	 * @param businessComponent
	 *            - 当前执行单元
	 * @param ruleResultList
	 *            - 执行单元上的规则结果列表
	 * @param context
	 *            - 执行单元上的上下文
	 *            
	 * @throws RuleBaseException
	 */
	void batchExportResult(BusinessComponent businessComponent, List<IRuleExecuteResult> ruleResultList,RuleExportContext context)
			throws RuleBaseException;
	
	/**
	 * 结果文件夹初始化操作
	 * 
	 * @param resultFolder
	 * @throws RuleBaseException
	 */
	void resultFolderInitialize(File resultFolder) throws RuleBaseException;

}
