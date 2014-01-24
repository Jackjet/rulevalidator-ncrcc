package com.yonyou.nc.codevalidator.rule;

/**
 * ����ִ�е������ģ��������Ŀ�������������������ͬ
 * <p>
 * ������̬��˽�����ԵĻ�ȡ���ܻḲ�ǵ���������
 * @author mazhqa
 * @since V1.0
 */
public interface IRuleExecuteContext {

	/**
	 * �����ִ��״̬ʱ��������ʵ������
	 * @param parameter
	 * @return
	 */
	String getParameter(String parameter);

	/**
	 * ���ݲ���ֵ��ö�Ӧ�Ĳ�������ֵ������������, ����
	 * @param parameter
	 * @return
	 */
	String[] getParameterArray(String parameter);

	/**
	 * �õ���ǰִ�е�ҵ�����
	 * @return
	 */
	BusinessComponent getBusinessComponent();
	
	/**
	 * ������ִ��ʱ������ִ��������
	 * @param runtimeContext
	 */
	void setRuntimeContext(RuntimeContext runtimeContext);
	
	/**
	 * ��������ִ�й������ܹ���ø�����ִ��������
	 * @return
	 */
	RuntimeContext getRuntimeContext();
	
	/**
	 * �õ���������̬��������ݣ���������̬����ز������ǴӸ������������л��
	 * @return
	 */
	IRuleConfigContext getRuleConfigContext();

}
