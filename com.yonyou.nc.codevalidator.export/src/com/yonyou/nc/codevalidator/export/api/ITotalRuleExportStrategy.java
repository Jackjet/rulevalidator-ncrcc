package com.yonyou.nc.codevalidator.export.api;

import com.yonyou.nc.codevalidator.rule.IIdentifier;
import com.yonyou.nc.codevalidator.rule.SessionRuleExecuteResult;
import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ȫ���������������
 * <P>
 * �˽ӿڲ���ֱ��ʹ�ã������ڸ�������ʵ��
 * 
 * @author mazhqa
 * @since V2.7
 */
public interface ITotalRuleExportStrategy extends IIdentifier {

	/**
	 * һ�ε�ִ�н��ȫ�����
	 * 
	 * @param sessionRuleExecuteResult
	 * @throws RuleBaseException
	 */
	void totalResultExport(SessionRuleExecuteResult sessionRuleExecuteResult) throws RuleBaseException;

}
