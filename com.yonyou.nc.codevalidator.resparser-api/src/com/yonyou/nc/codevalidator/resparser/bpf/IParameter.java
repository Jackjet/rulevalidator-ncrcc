package com.yonyou.nc.codevalidator.resparser.bpf;

import com.yonyou.nc.codevalidator.resparser.md.IType;

/**
 * ҵ�����-����-��������
 * @author mazhqa
 * @since V2.1
 */
public interface IParameter {
	
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
	 * �Ƿ�ۺ�vo
	 * @return
	 */
	boolean isAggVO();
	
	/**
	 * ����
	 * @return
	 */
	String getHelp();
	
	/**
	 * ������ʽ
	 * @return
	 */
	String getTypeStyle();

	/**
	 * ��������
	 * @return
	 */
	IType getParaType();
	
	/**
	 * �Զ�������
	 * @return
	 */
	String getParamDefClassName();
}
