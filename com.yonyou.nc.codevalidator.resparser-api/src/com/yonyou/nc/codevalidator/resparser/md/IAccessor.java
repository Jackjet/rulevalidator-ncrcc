package com.yonyou.nc.codevalidator.resparser.md;

/**
 * ����Ԫ����ʵ���еķ������ͷ���������
 * @author mazhqa
 * @since V2.5
 */
public interface IAccessor {
	
	/**
	 * ������-����������(��д)
	 * @return
	 */
	String getAccessorType();
	
	/**
	 * ������-���������ͣ�ȫ�ƣ�
	 * @return
	 */
	String getAccessorTypeFullName();
	
	/**
	 * ����������-��װ����(�������AggVO���ͣ�����null)
	 * @return
	 */
	String getAccessorWrapperClassName();

}
