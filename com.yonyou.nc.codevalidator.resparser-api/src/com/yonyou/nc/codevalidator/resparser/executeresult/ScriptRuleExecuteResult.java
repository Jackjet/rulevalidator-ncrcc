package com.yonyou.nc.codevalidator.resparser.executeresult;

import com.yonyou.nc.codevalidator.resparser.dataset.DataSet;
import com.yonyou.nc.codevalidator.rule.RuleExecuteStatus;
import com.yonyou.nc.codevalidator.rule.impl.AbstractRuleExecuteResult;

/**
 * 脚本规则执行的结果，用于将查出的数据库结果显示
 * @author mazhqa
 * @since V1.0
 */
public class ScriptRuleExecuteResult extends AbstractRuleExecuteResult {

	private DataSet dataSet;
	private IScriptExportStrategy scriptExportStrategy = IScriptExportStrategy.DEFAULT_EXPORT_STRATEGY;
	
	public void setScriptExportStrategy(IScriptExportStrategy scriptExportStrategy) {
		this.scriptExportStrategy = scriptExportStrategy;
	}

	public void setDataSet(DataSet dataSet) {
		this.dataSet = dataSet;
	}

	public DataSet getDataSet() {
		return dataSet;
	}

	@Override
	public String getNote() {
		return dataSet == null || dataSet.isEmpty() ? RESULT_SUCCESS : scriptExportStrategy.exportDataSet(dataSet);
	}

	@Override
	public RuleExecuteStatus getRuleExecuteStatus() {
		return dataSet == null || dataSet.isEmpty() ? RuleExecuteStatus.SUCCESS : RuleExecuteStatus.FAIL;
	} 
 
}
