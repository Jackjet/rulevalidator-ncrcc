package com.yonyou.nc.codevalidator.rule;

import com.yonyou.nc.codevalidator.rule.except.RuleBaseException;

/**
 * ͨ�õĹ����壬�κεĹ���Ҫ�Ӹýӿ�����
 * <p>
 * ���ڹ�����ִ�������ж��ǵ����ģ�Ҫȷ����������״̬���󣬲�Ҫ�����ж����κγ�Ա����
 * 
 * @author clamaa
 * @since V1.0
 */
public interface IRuleDefinition extends IIdentifier {

	/**
	 * ʹ��ִ�������Ļ���ִ�й��򣬲�����ִ�н��
	 * 
	 * @param ruleExecContext
	 *            -
	 * @return
	 * @throws RuleExecuteException
	 */
	IRuleExecuteResult actualExecute(IRuleExecuteContext ruleExecContext) throws RuleBaseException;

	/**
	 * �õ�������creator�����ڹ���ִ��ǰ��ִ�е�һЩ��ʼ������ ����ȥȡ��CreatorConstants�еĳ���
	 * 
	 * @return
	 */
	String[] getDependentCreator();

}
