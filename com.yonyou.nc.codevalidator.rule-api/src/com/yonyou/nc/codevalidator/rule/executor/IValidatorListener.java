package com.yonyou.nc.codevalidator.rule.executor;

import com.yonyou.nc.codevalidator.rule.BusinessComponent;

/**
 * ������֤�ļ�������������ִ�к��������ʱ�ͻ����ͳ��
 * <p>
 * V2.6�����¹��ܣ���������cancel����ÿ��ִ�н�����һ������󣬶�Ҫ����cancel���ж�
 * @author mazhqa
 * @since V1.0
 */
public interface IValidatorListener {

	/**
	 * ��ִ�е�Ԫ������Ӧ���¼�ʱ������֪ͨ
	 * @param businessComponent - ִ�е�Ԫ
	 * @param event - �¼�����
	 */
	void notifyBusiCompEvent(BusinessComponent businessComponent, ValidatorEvent event);

	/**
	 * ��ִ�е�Ԫ��ĳ�������ִ�п�ʼ/����ʱ�����¼�֪ͨ
	 * @param businessComponent
	 * @param ruleIdentifier
	 * @param event
	 */
	void notifyRuleEvent(BusinessComponent businessComponent, String ruleIdentifier, ValidatorEvent event);
	
	/**
	 * �ж��Ƿ��û�������ȡ������
	 * @return
	 */
	boolean requireCancel();
	
	/**
	 * ִ��ȡ���Ĳ���
	 */
	void executeCancelOperation();
	
	

}
