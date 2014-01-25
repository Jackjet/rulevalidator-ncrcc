package com.yonyou.nc.codevalidator.rule;

/**
 * ����ִ��������
 * @author mazhqa
 * @since V1.0
 */
public interface RuntimeContext {

	/**
	 * ��ǰ�����Ƿ��ڲ��������ִ��
	 * @return
	 */
	@Deprecated
	boolean isRunInPlug();

	/**
	 * �õ���ǰִ��������NCHOME·��
	 * @return
	 */
	String getNcHome();

	/**
	 * ��ǰִ�е�����Դ
	 * @return
	 */
	String getDataSource();

	/**
	 * ִ�м�鶥���ҵ�����
	 * @return
	 */
	BusinessComponent getBusinessComponents();
	
	/**
	 * ��ǰ����ִ�е�ҵ�����
	 * @return
	 */
	BusinessComponent getCurrentExecuteBusinessComponent();
	
	void setCurrentExecuteBusinessComponent(BusinessComponent businessComponent);
	
	SystemRuntimeContext getSystemRuntimeContext();
	
	/**
	 * ��ǰִ������id�����������ļ������ƣ�ÿ��ִ�е�����ִ���ļ���
	 * @return
	 */
	String getTaskExecuteId();
	
	String getProjectName();
	
}
