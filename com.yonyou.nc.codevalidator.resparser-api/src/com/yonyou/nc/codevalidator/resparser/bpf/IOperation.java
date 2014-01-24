package com.yonyou.nc.codevalidator.resparser.bpf;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.md.IType;

/**
 * ҵ������ӿ�-��������
 * @author mazhqa
 * @since V2.1
 */
public interface IOperation {
	
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
	 * ����
	 * @return
	 */
	String getDescription();
	
	/**
	 * ������ʽ
	 * @return
	 */
	String getTypeStyle();
	
	/**
	 * �ɼ���
	 * @return
	 */
	String getVisibility();
	
	/**
	 * �쳣
	 * @return
	 */
	String getMethodException();
	
	/**
	 * ��������
	 * @return
	 */
	IType getReturnType();
	
	/**
	 * ҵ�����
	 * @return
	 */
	boolean isBusiActivity();
	
	/**
	 * ���ؾۺ�VO
	 * @return
	 */
	boolean isAggVOReturn();
	
	/**
	 * ��������
	 * @return
	 */
	String getTransKind();
	
	/**
	 * ��������
	 * @return
	 */
	List<IParameter> getParameters();

}
