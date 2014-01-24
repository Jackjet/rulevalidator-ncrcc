package com.yonyou.nc.codevalidator.resparser.bpf;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.md.IType;

/**
 * ҵ�����
 * @author mazhqa
 * @since V2.1
 */
public interface IBusiOperator {
	
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
	 * ������ԴID
	 * @return
	 */
	String getResId();
	
	/**
	 * ��չ��ǩ
	 * @return
	 */
	String getExtendTag();
	
	/**
	 * ����
	 * @return
	 */
	String getDescription();
	
	/**
	 * �Ƿ���Ȩ
	 * @return
	 */
	boolean isAuthorization();
	
	/**
	 * �Ƿ�ҵ��
	 * @return
	 */
	boolean isBusiActivity();
	
	/**
	 * �Ƿ���־
	 * @return
	 */
	boolean isNeedLog();
	
	/**
	 * ��־����
	 * @return
	 */
	String getLogType();
	
	/**
	 * ����ʵ��
	 * @return
	 */
	IType getOwnType();
	
	/**
	 * ���ò�������
	 * @return
	 */
	List<IRefOperation> getRefOperations();

}
