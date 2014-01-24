package com.yonyou.nc.codevalidator.resparser.bpf;

import java.util.List;

import com.yonyou.nc.codevalidator.resparser.md.IType;

/**
 * ҵ��
 * @author mazhqa
 * @since V2.3
 */
public interface IBusiActivity {
	
	String getVersion();
	
	/**
	 * ������Դid
	 * @return
	 */
	String getResId();
	
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
	 * ��չ��ǩ
	 * @return
	 */
	String getExtendTag();
	
	/**
	 * ����ʵ��
	 * @return
	 */
	IType getOwnType();
	
	/**
	 * �Ƿ���Ȩ
	 * @return
	 */
	boolean isAuthorization();
	
	/**
	 * �Ƿ����
	 * @return
	 */
	boolean isService();
	
	/**
	 * ҵ����Ӧ��ҵ���������
	 * @return
	 */
	List<IBusiOperator> getBusiOperators();

}
