package com.yonyou.nc.codevalidator.export.api;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * �˽ӿ�רΪִ�м�¼�ı���͸������
 * 
 * @author mazhqa
 * @since V2.9
 */
public interface IRecordExecutePersistence {

	/**
	 * ��ִ�п�ʼ�󣬻���и÷����Ļص����ã�������db������������
	 * @param ruleExportContext
	 * @return
	 * @throws RuleBaseException
	 */
	String insertRecord(RuleExportContext ruleExportContext) throws RuleBaseException;

	/**
	 * ��ִ�н����󣬻���и÷����Ļص����ã�����������ִ�н���ʱ�䣬����������
	 * @param ruleExportContext
	 * @return
	 * @throws RuleBaseException
	 */
	String updateRecord(RuleExportContext ruleExportContext) throws RuleBaseException;

}
