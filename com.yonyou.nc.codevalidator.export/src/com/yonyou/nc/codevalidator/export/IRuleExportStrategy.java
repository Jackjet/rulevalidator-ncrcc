package com.yonyou.nc.codevalidator.export;

import java.io.File;
import java.util.List;

import com.yonyou.nc.codevalidator.export.api.RuleExportContext;
import com.yonyou.nc.codevalidator.rule.BusinessComponent;
import com.yonyou.nc.codevalidator.rule.IRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.SessionRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ������е����Ĳ��Խӿ�
 * @author mazhqa
 * @since V2.7
 */
public interface IRuleExportStrategy {

	/**
	 * һ�ε�ִ�н��ȫ�����
	 * 
	 * @param sessionRuleExecuteResult
	 * @throws RuleBaseException
	 */
	void totalResultExport(SessionRuleExecuteResult sessionRuleExecuteResult) throws RuleBaseException;

	/**
	 * ��������ִ�н�����ɣ����б������������ĸ�ʽ����
	 * 
	 * @param businessComponent
	 *            - ��ǰִ�е�Ԫ
	 * @param ruleResultList
	 *            - ִ�е�Ԫ�ϵĹ������б�
	 * @param context
	 *            - ִ�е�Ԫ�ϵ�������
	 *            
	 * @throws RuleBaseException
	 */
	void batchExportResult(BusinessComponent businessComponent, List<IRuleExecuteResult> ruleResultList,RuleExportContext context)
			throws RuleBaseException;
	
	/**
	 * ����ļ��г�ʼ������
	 * 
	 * @param resultFolder
	 * @throws RuleBaseException
	 */
	void resultFolderInitialize(File resultFolder) throws RuleBaseException;

}
