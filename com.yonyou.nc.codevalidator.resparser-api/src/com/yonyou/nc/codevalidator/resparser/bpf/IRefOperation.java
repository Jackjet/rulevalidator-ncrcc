package com.yonyou.nc.codevalidator.resparser.bpf;

/**
 * ҵ��-ҵ�����
 * @author mazhqa
 * @since V2.1
 */
public interface IRefOperation {
	
	String getID();
	
	/**
	 * ����
	 * @return
	 */
	String getName();
	
	/**
	 * ��ʾ����
	 * @return
	 */
	String getDisplayName();
	
	/**
	 * ������ص�ҵ������ӿ�
	 * @return
	 */
	IOpInterface getRefOptInterface();
	
	/**
	 * ������ص�ҵ���������
	 * @return
	 */
	IOperation getRefOperation();

}
